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

import hoodland.opensource.memoir.Memoir
import hoodland.opensource.memoir.showThrowable
import hoodland.opensource.memoir.UNKNOWN
import hoodland.opensource.toolbox.*
import java.io.File
import java.io.PrintWriter
import kotlin.concurrent.thread
import kotlin.math.round

interface TestEchelon {
    val name: String
    val overallStatus: TestStatus
}

public class TestCollection(override val name: String): ArrayList<TestEchelon>(), TestEchelon {
    init {
    System.err.println("Ran Init: ${this::class.simpleName}")
    }
    private var _currentTest: Test? = null
    private var currentArtifactsDirectory = UNSET_STRING
    private var executionThread: Thread? = null
    private var currentCount = Int.MAX_VALUE
    internal var rootDirectory: String = UNSET_STRING

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

    // TODO: Alter this to provide for
    //         * You want certain/batch of files excluded. Maybe by category
    //         * You only want to run centain IDs or categories
    //
    // Properly doing this might require a custom data structure that a test-runner program would pass in.
    //
    fun run(preclusiveFailures: ArrayList<Throwable>? = null) : Memoir {
        var logFileName = "$name.html"
        if (rootDirectory === UNSET_STRING) {
            rootDirectory = "$DEFAULT_PARENT_FOLDER${File.separatorChar}$quickTimeStamp $name"
            logFileName = "All tests.html"
        }

        currentArtifactsDirectory = rootDirectory
        val logFileFullPath = "$currentArtifactsDirectory${File.separatorChar}$logFileName"
        // Force the parent directory to exist...
        File(logFileFullPath).parentFile.mkdirs()

        val overlog = Memoir(name, null, PrintWriter(logFileFullPath), ::logHeader)
        if (preclusiveFailures != null) {
            if (preclusiveFailures.size > 0) {
                overlog.error("Failures were indicated while starting Koarse Grind!")
                preclusiveFailures.forEach {
                    overlog.showThrowable(it)
                }
            }
        }

        for (indexCount in 0..(this.count() - 1)) {
            currentCount = indexCount

            if (KILL_SWITCH) {
                // Decline to run
                break
            } else {
                val nextItem = this[currentCount]
                if (nextItem is Test) {
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
                } else if (nextItem is TestCollection) {
                    val subCollection = nextItem as TestCollection
                    subCollection.rootDirectory = "$currentArtifactsDirectory${File.separatorChar}(collection '${subCollection.name}' in progress)"
                    overlog.showMemoir(subCollection.run(), subCollection.overallStatus.memoirIcon, subCollection.overallStatus.memoirStyle)

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

        return overlog
    }

    // Omitting public functions Run() & InterruptCurrentTest() from C#
    // which appears to be unnecessary in Kotlin. These might be relics
    // from when Coarse Grind had a web interface.

    // This may have only been used by the web UI in the old Coarse Gring.
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

    override val overallStatus: TestStatus
        get() {
            if (size < 1) { return TestStatus.INCONCLUSIVE }
            var result = TestStatus.PASS
            this.forEach {
                result = result + it.overallStatus
            }

            return result
    }

    val prefixedName: String
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
}