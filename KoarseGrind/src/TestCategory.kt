// Copyright (c) 2020, 2023 William Arthur Hood
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights to
// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
// of the Software, and to permit persons to whom the Software is furnished
// to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.

package hoodland.opensource.koarsegrind

import hoodland.opensource.memoir.Memoir
import hoodland.opensource.memoir.showThrowable
import hoodland.opensource.toolbox.*
import java.io.File
import java.io.PrintWriter
import kotlin.concurrent.thread

/**
 * An Inquiry is any Test or TestCategory in the KoarseGrind system. It may be used
 * if a field or variable must contain anything that is a derivative of either of those classes.
 */
interface Inquiry {
    val name: String
    val overallStatus: TestStatus
}

/**
 * TestCategory:
 * This is the root-level container for a suite of tests to run. It is also used for a subgroup of
 * related tests, as typically produced by a TestFactory. The order in which it runs the tests or
 * subordinate TestCategories it contains is not guaranteed.
 *
 * @property name A human-readable name for this collection of tests. At the root level this should be
 * the overall name for the entire suite (the same "name" passed into the TestProgram.run() method).
 * It may also apply to a subgroup of related tests (the same "collectionName" passed into a TestFectory).
 * All human readable names of either TestCategories or TestCases must be unique.
 * @property categoryPath The human readable name of the TestCollection or TestFactory that this should be
 * immediately subordinate to. Leave it as null to make this top level.
 */

// Making this internal. Collections are created by naming them as the owner in a test or factory.
// TestCategory (formerly TestCollection) is now explicitly the branch node of an N-ary Tree structure
// with Tests as the leaves.
internal class TestCategory(override val name: String): ArrayList<Test>(), Inquiry {
    val subCategories = HashMap<String, TestCategory>()
    var outfitter: Outfitter? = null
    private var _currentTest: Test? = null
    private var currentArtifactsDirectory = UNSET_STRING
    private var executionThread: Thread? = null

    // This appears to be a hold-out from the old C# Webserver GUI. May not be needed.
    private var currentCount = Int.MAX_VALUE
    internal var rootDirectory: String = UNSET_STRING
    private var filterSet: FilterSet? = null

    /* For debugging use if needed
    init {
        System.err.println("Ran Init: ${this::class.simpleName}")
    }
     */

    internal fun getCategory(fullPath: List<String>): TestCategory? {
        if (fullPath.size > 1) {
            val nextCategory = fullPath.take(1)[0]
            val remainingCategories = fullPath.drop(1)

            val check = subCategories[nextCategory]
            if (check == null) return null
            return check.getCategory(remainingCategories)
        }

        return subCategories[fullPath[0]]
    }

    internal fun addCategory(fullPath: List<String>): TestCategory {
        if (fullPath.size > 1) {
            val nextCategory = fullPath.take(1)[0]
            val remainingCategories = fullPath.drop(1)

            var check = subCategories[nextCategory]
            if (check == null) {
                check = TestCategory(nextCategory)
            }
            subCategories[nextCategory] = check
            return check.addCategory(remainingCategories)
        }

        subCategories.putIfAbsent(fullPath[0], TestCategory(fullPath[0]))
        return subCategories[fullPath[0]]!!
    }

    internal fun deleteCategory(fullPath: List<String>): TestCategory? {
        val deltedCategory = fullPath.takeLast(1)[0]
        val remainingCategories = fullPath.dropLast(1)
        val check = getCategory(remainingCategories)
        if (check != null) {
            return check.subCategories.remove(deltedCategory)
        } else {
            // TODO: Either throw an exception or NO-OP
        }

        return null
    }

    // TODO: TestCategories are now in a HashSet separate from the tests.
    internal fun run(filters: FilterSet? = null, preclusiveFailures: ArrayList<Throwable>? = null) : Memoir {
        filterSet = filters
        var logFileName = "$name.html"
        if (rootDirectory === UNSET_STRING) {
            rootDirectory = "$DEFAULT_PARENT_FOLDER${File.separatorChar}$quickTimestamp $name"
            logFileName = "All tests.html"
        }

        currentArtifactsDirectory = rootDirectory
        val logFileFullPath = "$currentArtifactsDirectory${File.separatorChar}$logFileName"
        // Force the parent directory to exist...
        File(logFileFullPath).parentFile.mkdirs()

        val overlog = Memoir(name, stdout, PrintWriter(logFileFullPath), true, true, ::logHeader)
        if (preclusiveFailures != null) {
            if (preclusiveFailures.size > 0) {
                overlog.error("Failures were indicated while starting Koarse Grind! The suite will not be allowed to run.")
                KILL_SWITCH = true
                preclusiveFailures.forEach {
                    overlog.showThrowable(it)
                }
            }
        }

        // Collection Setup
        if (KILL_SWITCH) {
            // Decline to run
        } else {
            outfitter?.let {
                it.runSetup(rootDirectory)
                it.setupContext.showWithStyle(overlog)
                if (! it.setupContext.overallStatus.isPassing()) {
                    val thisResult = TestResult(TestStatus.INCONCLUSIVE, "Declining to perform all tests in category $name because category-level setup failed.")
                    it.setupContext.results.add(thisResult)
                    overlog.showTestResult((thisResult))
                }
            }
        }

        var attemptTests = true

        outfitter?.let {
            attemptTests = it.setupContext.overallStatus.isPassing()
        }

        if (attemptTests) {
            for (indexCount in 0..(this.count() - 1)) {
                currentCount = indexCount
                if (KILL_SWITCH) {
                    // Decline to run
                    break
                } else {
                    val nextItem = this[currentCount]
                    if (shouldRun(nextItem)) {
                        _currentTest = nextItem
                        try {
                            executionThread = thread(start = true) { _currentTest!!.runTest(currentArtifactsDirectory) } // C# used the public Run() function
                            executionThread!!.join()
                        } catch (thisFailure: Throwable) {
                            // Uncertain why specifically calling GetResultForPreclusionInSetup()
                            _currentTest!!.addResult(_currentTest!!.getResultForPreclusionInSetup(thisFailure))
                        } finally {
                            executionThread = null
                            overlog.showMemoir(_currentTest!!.testContext!!.memoir, _currentTest!!.overallStatus.memoirIcon, _currentTest!!.overallStatus.memoirStyle)

                            // Prefix the test's artifacts directory with its overall status
                            try {
                                File(_currentTest!!.artifactsDirectory).renameTo(File(currentArtifactsDirectory + File.separatorChar + _currentTest!!.prefixedName))
                            } catch (loggedThrowable: Throwable) {
                                overlog.error("Koarse Grind was unable to rename the current test's artifact directory to their permanent location")
                                overlog.showThrowable(loggedThrowable)
                            }
                        }
                    }
                }
            }

            // Attempt subordinate categories
            this.subCategories.values.forEach {
                if (KILL_SWITCH) {
                    // Decline to run
                } else {
                    it.rootDirectory =
                        "$currentArtifactsDirectory${File.separatorChar}(collection '${it.name}' in progress)"

                    val subLog = it.run(filterSet)
                    if (subLog.wasUsed) {
                        overlog.showMemoir(
                            subLog,
                            it.overallStatus.memoirIcon,
                            it.overallStatus.memoirStyle
                        )
                    }

                    // Prefix the test's artifacts directory with its overall status
                    try {
                        File(it.rootDirectory).renameTo(File(currentArtifactsDirectory + File.separatorChar + it.prefixedName))
                    } catch (loggedThrowable: Throwable) {
                        overlog.error("Koarse Grind was unable to rename the current test collection's root directory to their permanent location")
                        overlog.showThrowable(loggedThrowable)
                    }

                }
            }

        }

        // Collection Cleanup
        if (KILL_SWITCH) {
            // Decline to run
        } else {
            outfitter?.let {
                it.runCleanup()
                it.cleanupContext.showWithStyle(overlog)
            }
        }

        return overlog
    }

    private fun shouldRun(candidate: Test): Boolean {
        filterSet?.let {
            return it.shouldRun(candidate)
        }

        return true
    }

    /**
     * overallStatus: This is a direct counterpart to the overallStatus property for each individual test. It is a conglomeration
     * of all statuses of all tests and subordinate collections that this collection contains, excluding any tests that were filtered out.
     */
    override val overallStatus: TestStatus
        get() {
            if (size < 1) { return TestStatus.INCONCLUSIVE }
            var result = TestStatus.PASS
            outfitter?.let {
                result += it.overallStatus
            }
            this.forEach {
                if (it is Test) {
                    if (shouldRun(it)) {
                        result = result + it.overallStatus
                    }
                } else {
                    result = result + it.overallStatus
                }
            }

            return result
    }

    private val prefixedName: String
        get() = "$overallStatus - $name"

    internal fun gatherForReport(summaryReport: MatrixFile<String>) {
        this.forEach {
            if (it.wasSetup) {
                summaryReport.addDataRow(it.summaryDataRow)
            }
        }
        this.subCategories.values.forEach {
            it.gatherForReport(summaryReport)
        }
    }
}