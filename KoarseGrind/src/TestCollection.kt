// Copyright (c) 2020 William Arthur Hood
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

import hoodland.opensource.memoir.EMOJI_CLEANUP
import hoodland.opensource.memoir.Memoir
import hoodland.opensource.memoir.showThrowable
import hoodland.opensource.toolbox.*
import java.io.File
import java.io.PrintWriter
import kotlin.concurrent.thread

/**
 * An Inquiry is any Test or TestCollection in the KoarseGrind system. It may be used
 * if a field or variable must contain anything that is a derivative of either of those classes.
 */
interface Inquiry {
    val name: String
    val overallStatus: TestStatus
}

/**
 * TestCollection:
 * This is the root-level container for a suite of tests to run. It is also used for a subgroup of
 * related tests, as typically produced by a TestFactory. The order in which it runs the tests or
 * subordinate TestCollections it contains is not guaranteed.
 *
 * @property name A human-readable name for this collection of tests. At the root level this should be
 * the overall name for the entire suite (the same "name" passed into the TestProgram.run() method).
 * It may also apply to a subgroup of related tests (the same "collectionName" passed into a TestFectory).
 */
public class TestCollection(override val name: String): ArrayList<Inquiry>(), Inquiry {
    var outfitter: Outfitter? = null
    private var _currentTest: Test? = null
    private var currentArtifactsDirectory = UNSET_STRING
    private var executionThread: Thread? = null
    private var currentCount = Int.MAX_VALUE
    internal var rootDirectory: String = UNSET_STRING
    private var filterSet: FilterSet? = null

    /* For debugging use if needed
    init {
        System.err.println("Ran Init: ${this::class.simpleName}")
    }
     */

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

        val overlog = Memoir(name, null, PrintWriter(logFileFullPath), true, true, ::logHeader)
        if (preclusiveFailures != null) {
            if (preclusiveFailures.size > 0) {
                overlog.error("Failures were indicated while starting Koarse Grind!")
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
                    val thisResult = TestResult(TestStatus.INCONCLUSIVE, "Declining to perform all tests in collection $name because collection-level setup failed.")
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
                    if (nextItem is Test) {
                        if (shouldRun(nextItem)) {
                            _currentTest = nextItem as Test
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
                    } else if (nextItem is TestCollection) {
                        val subCollection = nextItem as TestCollection
                        subCollection.rootDirectory = "$currentArtifactsDirectory${File.separatorChar}(collection '${subCollection.name}' in progress)"

                        val subLog = subCollection.run(filterSet)
                        if (subLog.wasUsed) {
                            overlog.showMemoir(subLog, subCollection.overallStatus.memoirIcon, subCollection.overallStatus.memoirStyle)
                        }

                        // Prefix the test's artifacts directory with its overall status
                        try {
                            File(subCollection.rootDirectory).renameTo(File(currentArtifactsDirectory + File.separatorChar + subCollection.prefixedName))
                        } catch (loggedThrowable: Throwable) {
                            overlog.error("Koarse Grind was unable to rename the current test collection's root directory to their permanent location")
                            overlog.showThrowable(loggedThrowable)
                        }
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
            if (it is Test){
                if (it.wasSetup) {
                    summaryReport.addDataRow(it.summaryDataRow)
                }
            } else if (it is TestCollection) {
                it.gatherForReport(summaryReport)
            }
        }
    }

    /*
     * These are for future use by a test runner program.
    fun reset() {
        currentCount = Int.MAX_VALUE
        _currentTest?.interrupt() // C# code did not check if the test was present and did not call Interrupt()
        _currentTest = null
        executionThread?.interrupt() // C# code tried to Abort() three times in a row if not null
        executionThread = null
        currentArtifactsDirectory = UNSET_STRING
    }

    val currentTest: String
        get() {
            if (_currentTest == null) { return UNKNOWN }
            return _currentTest!!.identifiedName
        }

    // In C#: [MethodImpl(MethodImplOptions.Synchronized)]
    val progress: Int
        get() {
            synchronized(this) {
                if (this.count() < 1) { return 100 }
                if (currentCount >= this.count()) { return 100 }
                var effectiveCount = currentCount.toFloat()

                try {
                    effectiveCount += _currentTest!!.progress
                } catch (dontCare: Throwable) {
                    // DELIBERATE NO-OP
                }

                return round((effectiveCount / this.count()) * 100).toInt()
            }
        }

    // Omitting public functions Run() & InterruptCurrentTest() from C#
    // which appears to be unnecessary in Kotlin. These might be relics
    // from when Coarse Grind had a web interface.

    // This may have only been used by the web UI in the old Coarse Grind.
    // Might be needed for an external runner, which also might need the
    // function InterruptCurrentTest() put back. Possibly also Run().
    fun haltAllTesting() {
        KILL_SWITCH = true
        currentCount = Int.MAX_VALUE
        _currentTest!!.interrupt() // C# used the public InterruptCurrentTest() function

        try {
            executionThread?.interrupt()
            // C# version was followed by executionThread.Abort() three times in a row.
        } catch (dontCare: Exception) {
            // Deliberate NO-OP
        } finally {
            executionThread = null
        }
    }
     *
     */
}