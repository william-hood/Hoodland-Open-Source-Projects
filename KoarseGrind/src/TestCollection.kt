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

// TODO: TestCollection needs to be nestable so that there can be sub-groupings of tests.

private const val FOLDER_FOR_ALL_TESTS = "All Tests"

public object TestCollection: ArrayList<Test>() {
    init {
    System.err.println("Ran Init: ${this::class.simpleName}")
    }
    private var _currentTest: Test? = null
    private var currentArtifactsDirectory = UNSET_STRING
    private var executionThread: Thread? = null
    private var currentCount = Int.MAX_VALUE

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

    // The C# version contains a large #region "Old way that uses category folders"
    // C# version also took no parameters and logged to console.
    // I'm changing this to require a memoir so it can be logged properly.
    private fun copyResultsToCategories(memoir: Memoir) {
        try {
            //copyCompletely(_currentTest!!.artifactsDirectory, currentArtifactsDirectory + File.separatorChar + _currentTest!!.prefixedName)
            File(_currentTest!!.artifactsDirectory).copyRecursively(File(currentArtifactsDirectory + File.separatorChar + _currentTest!!.prefixedName), true)
        } catch (loggedThrowable: Throwable) {
            memoir.error("Koarse Grind was unable to copy the current test results to their permanent location")
            memoir.showThrowable(loggedThrowable)
        } finally {
            //hardDelete(currentTest!!.ArtifactsDirectory)
            File(_currentTest!!.artifactsDirectory).deleteRecursively()
        }
    }

    // TODO: Alter this to provide for
    //         * You want certain/batch of files excluded. Maybe by category
    //         * You only want to run centain IDs or categories
    //
    // Properly doing this might require a custom data structure that a test-runner program would pass in.
    //
    fun run(name: String, rootDirectory: String  = "$DEFAULT_PARENT_FOLDER${File.separatorChar}$quickTimeStamp ${name}", preclusiveFailures: ArrayList<Throwable>? = null) {
        currentArtifactsDirectory = rootDirectory
        val expectedFileName = currentArtifactsDirectory + File.separatorChar + "All tests.html"
        //forceParentDirectoryExistence(expectedFileName)
        // Force the parent directory to exist...
        File(expectedFileName).parentFile.mkdirs()

        val overlog = Memoir(name, null, PrintWriter(expectedFileName), ::logHeader)
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
                _currentTest = this[currentCount]
                /*
                if (false)//(exclusions.matchesCaseInspecific(currentTest!!.IdentifiedName)) {
                    // Decline to run
                    break
                } else {
                }
                */
                try {
                    executionThread = thread(start = true) { _currentTest!!.runTest(currentArtifactsDirectory) } // C# used the public Run() function
                    executionThread!!.join()
                } catch (thisFailure: Throwable) {
                    // Uncertain why specifically calling GetResultForPreclusionInSetup()
                    _currentTest!!.addResult(_currentTest!!.getResultForPreclusionInSetup(thisFailure))
                } finally {
                    executionThread = null
                    overlog.showMemoir(_currentTest!!.testContext!!.memoir, _currentTest!!.overallStatus.memoirIcon, _currentTest!!.overallStatus.memoirStyle)
                    this.copyResultsToCategories(overlog)
                }
            }
        }

        createSummaryReport(rootDirectory, overlog)
        overlog.conclude()
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

    val overallStatus: TestStatus
        get() {
        var tally = 0
            var result = TestStatus.PASS
            this.forEach {
                if (it.wasRun) {
                    tally++
                    result = result + it.overallStatus
                }
            }

            if (tally < 1) { return TestStatus.INCONCLUSIVE }
            return result
    }

    fun createSummaryReport(rootDirectory: String, memoir: Memoir = Memoir(forPlainText = stdout)) {
        val fullyQulaifiedSummaryFileName = rootDirectory + File.separatorChar + SUMMARY_FILE_NAME
        memoir.info("Creating Test Suite Summary Report ($fullyQulaifiedSummaryFileName)")
        var summaryReport = MatrixFile<String>("Categorization", "Test ID", "Name", "Description", "Status", "Reasons")

        try {
            // Try to append to an existing one
            summaryReport = MatrixFile.read(fullyQulaifiedSummaryFileName, StringParser)
        } catch(dontCare: Throwable) {
            // Deliberate NO-OP
            // Leave the summaryReport as created above
        }

        this.forEach {
            if (it.wasSetup) {
                summaryReport.addDataRow(it.summaryDataRow)
            }
        }

        summaryReport.write(fullyQulaifiedSummaryFileName)


        // Section below creates a single line file stating the overall status
        val fullyQulaifiedSummaryTextFileName = rootDirectory + File.separatorChar + SUMMARY_TEXTFILE_NAME

        try {
            val textFile = QuantumTextFile(fullyQulaifiedSummaryTextFileName)
            textFile.println(overallStatus.toString())
            textFile.flush()
            textFile.close() // Shouldn't need a getter because this is a val
        } catch (thisFailure: Throwable) {
            memoir.error("Did not successfully create the overall status text file $fullyQulaifiedSummaryTextFileName")
            memoir.showThrowable(thisFailure)
        }
    }
}