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

import hoodland.opensource.memoir.*
import hoodland.opensource.toolbox.UNSET_STRING
import hoodland.opensource.toolbox.stdout
import java.io.File
import java.io.PrintWriter
import kotlin.concurrent.thread

internal class TestPhaseContext(val memoir: Memoir) {
    val results = ArrayList<TestResult>()

    val overallStatus: TestStatus
        get() {
            if ((results.size < 1)) return TestStatus.INCONCLUSIVE
            var finalValue = TestStatus.PASS
            results.forEach {
                finalValue = finalValue + it.status
            }

            return finalValue;
        }
}

internal const val INFO_ICON = "ℹ️"
internal const val IN_PROGRESS_NAME = "(test in progress)"
internal const val SETUP = "setup"
internal const val CLEANUP = "cleanup"
internal const val UNSET_DESCRIPTION = "(no details)"

/**
 * Test:
 * The base class for any kind of Koarse Grind Test. Any class that extends this, but is not also a derivative of
 * the ManufacturedTest class, will be instantiated and run. If an optional setup() method exists it will be run first.
 * Failed assertions in setup() render the test inconclusive. If an optional cleanup() method exists it will always
 * be run, even if setup failed. The performTest() method will be run, unless setup failed.
 *
 * @property name A human-readable name for the test. This should not be too long. You may use a full sentence if you wish, but that might be better as the detailedDescription parameter.
 * @property detailedDescription This should be used to explain, in plain english, anything the test does that is not obvious to someone reviewing the results or editing the code. This is important if someone who did not write the test is assigned to write code that makes it pass.
 * @property identifier You may omit this if you wish. If you are using a test tracking system that assigns a test case ID, this is the place it goes.
 *
 * @param categories This is an optional list of categories the test falls into. Examples might be "Short", "Long", "REST", "GUI", "HighPriority", "HappyPath", etc.
 */
abstract class Test (
        override val name: String,
        private val detailedDescription: String = UNSET_DESCRIPTION,
        internal val identifier: String = "",
        vararg categories: String): TestEchelon {
        internal var categories: Array<out String> = categories
        internal val setupContext = TestPhaseContext(Memoir("Setup - Test $identifiedName", stdout))
        internal val cleanupContext = TestPhaseContext(Memoir("Cleanup -  Test $identifiedName", stdout))
        internal var testContext: TestPhaseContext? = null
        private var parentArtifactsDirectory = UNSET_STRING
        private var executionThread: Thread? = null
        internal var wasSetup = false
        internal var wasRun = false
        internal var wasCleanedUp = false

    /**
     * assert:
     * Use an assertion to check criteria which should fail the entire test if they do not succeed. If a
     * failed assertion occurs in setup, the test is rendered inconclusive rather than failing. Failed
     * assertions in cleanup are for information only and do not affect the test's pass/fail status.
     */
    protected val assert = Enforcer(TestConditionalType.ASSERTION, this)

    /**
     * require:
     * Requirements are used to verify prerequisites or other conditions that would invalidate the test if not true.
     * Failing a requirement makes the test inconclusive, and overrides any other checks that would normally
     * cause a test to become passing or failing.
     */
    protected val require = Enforcer(TestConditionalType.REQUIREMENT, this)

    /**
     * consider:
     * Considerations are used to check conditions that realy ought to be true, but do not necessarily fail
     * the test unless further analysis decides that should be the case. Failing a consideration makes
     * the test status become subjective. ("Subjective" means that a human must make the final call.)
     */
    protected val consider = Enforcer(TestConditionalType.CONSIDERATION, this)

    /**
     * setup:
     *
     * Override this method to perform anything necessary prior to starting the actual test. This might be
     * creating test data on-the-fly or verifying that the test target is in the right state.
     * If an assertion fails in setup(), the test becomes inconclusive.
     *
     */
    open fun setup() { setupContext.results.add(TestResult(TestStatus.PASS, "(no user-supplied setup)")) }

    /**
     * cleanup:
     *
     * Override this method and carry out any steps necessary to properly clean up anything the test did.
     * Examples might be erasing test data inserted into a database or freeing up resources that can't be done automatically.
     * If an assertion, requirement, or consideration fails in cleanup(), it does not affect the status of the test,
     * or any collection it's in.
     *
     */
    open fun cleanup() { setupContext.results.add(TestResult(TestStatus.PASS, "(no user-supplied cleanup)")) }

    /**
     * performTest:
     *
     * Override this method and put the test's business logic in it. If an assertion in this method fails, the test
     * is considered failing. If a requirement in this method fails, the test is considered inconclusive. If a
     * consideration in this method fails, the test is considered subjective. ("Subjective" means that a human must make the final call.)
     */
    abstract fun performTest()

    private val categorization: String
        get() {
            val result = StringBuilder()
            categories.forEach {
                if (result.length > 0 ) { result.append('/') }
                result.append(it)
            }

            return result.toString()
        }

    /**
     * log:
     * Used to write information to Koarse Grind's HTML-based log file, as well as to the console. It is automatically
     * determined whether to use the setup, cleanup, or normal portion of the test's log. Log files will appear in a
     * time-stamped folder in a directory named "Test Results" which is automatically created off of your Documents folder.
     */
    val log: Memoir
     get() = currentContext.memoir

    /**
     * results: This contains the results of every assertion, requirement, or consideration. Results should also
     * be added if exceptions are thrown. It is not recommended you directly manipulate this unless you
     * know what you're doing. Use the addResult() function as the normal way to populate this.
     */
    val results: ArrayList<TestResult>
            get() = currentContext.results

    internal val currentContext: TestPhaseContext
        get() {
            if (wasSetup) {
                if (! setupContext.overallStatus.isPassing()) {
                    return cleanupContext
                }

                testContext?.let {
                    if (! wasRun) { return it }
                }

                return cleanupContext
            }

            if (wasRun) { return cleanupContext }
            return setupContext
        }

    /* For future use by a test runner program
    // In C#: [MethodImpl(MethodImplOptions.Synchronized)]
    internal val progress: Float
    get() {
        synchronized(this) {
            var result: Float = 0.toFloat()
            val check = currentContext
            if (check === testContext) result += 0.33.toFloat()
            if (check === cleanupContext) result += 0.34.toFloat()
            if (wasCleanedUp) result += 0.33.toFloat()
            return result
        }
    }

    fun interrupt() {
        try {
            executionThread?.interrupt()
            // C# version was followed by executionThread.Abort() three times in a row.
        } catch (dontCare: Exception) {
            // Deliberate NO-OP
        }
    }
    */

    /**
     * addResult:
     * Puts a single test result into the results list, while properly logging it.
     *
     */
    fun addResult(thisResult: TestResult) {
        // For some reason in the C# version this was open/virtual
        val context = currentContext
        context.memoir.showTestResult((thisResult))
        context.results.add(thisResult)
    }

    /**
     * overallStatus: This produces the overall status of all results this test has produced. It
     * should normally be called after the test is run. It is not for general use, and should be
     * called by other parts of Koarse Grind or an external test runner program.
     */
    override val overallStatus: TestStatus
        get() {
            // Previous version also used INCONCLUSIVE if !wasRun
            if (! setupContext.overallStatus.isPassing()) return TestStatus.INCONCLUSIVE
            return testContext!!.overallStatus
        }

    // Is virtual/open in C#
    /**
     * artifactsDirectory: This gives the full path where this particular test should put any
     * screenshots, data files, or other artifacts produced by the test. The artifacts directory
     * will also contain a single log file exclusively for the one test that owns it. (The same
     * log will appear in a subsection of the "All Tests" log file.
     */
    val artifactsDirectory: String
        get() = parentArtifactsDirectory + File.separatorChar + IN_PROGRESS_NAME

    // Is virtual/open in C#
    /**
     * identifiedName: This is the human-readable name of the test prefixed by the test case identifier.
     */
    val identifiedName: String
        get() {
            if (identifier.length < 1) return name
            return "$identifier - $name"
        }

    // Is virtual/open in C#
    /**
     * prefixedName: Is the same as the identifiedName property, with an additional prefix of the overall pass/fail status.
     */
    val prefixedName: String
        get() = "$overallStatus - $identifiedName"

    // Is virtual/open in C#
    /**
     * logFileName: Produces the relative file name of the HTML-based log file. It does NOT include the directory path.
     */
    val logFileName: String
        get() = "$identifiedName Log.html"

    private fun filterForSummary(it: String): String {
        // C# return Regex.Replace(Regex.Replace(it, "[,\r\f\b]", ""), "[\t\n]", " ");
        return it.replace(Regex("[,\r\b]"), "").replace(Regex("[\t\n]"), "")
    }

    internal val summaryDataRow: ArrayList<String>
        get() {
            val result = ArrayList<String>()
            result.add(filterForSummary(categorization))
            result.add(filterForSummary(identifier))
            result.add(filterForSummary(name))
            result.add(filterForSummary(detailedDescription))
            result.add(filterForSummary(overallStatus.toString()))

            val reasoning = StringBuilder()

            arrayOf(this.setupContext, this.testContext, this.cleanupContext).forEach { thisPhase ->
                thisPhase?.results?.forEach {
                    if (! it.status.isPassing()) {
                        if (reasoning.length > 0) {
                            reasoning.append("; ")
                        }
                        reasoning.append(it.description)

                        if (it.hasFailures) {
                            it.failures.forEach { thisFailure ->
                                if (reasoning.length > 0) {
                                    reasoning.append("; ")
                                }

                                reasoning.append(thisFailure.javaClass.simpleName)
                            }
                        }
                    }
                }
            }

            result.add(filterForSummary(reasoning.toString()))
            return result
        }

    private fun getResultForIncident(status: TestStatus, section: String, failure: Throwable): TestResult {
        var reportedSection = section
        if (section.length > 0) {
            reportedSection = "($section)"
        }

        val result = TestResult(status, "$identifiedName$section: An unanticipated failure occurred.")
        result.failures.add(failure)
        return result
    }


    // Is virtual/open in C#
    /**
     * getResultForFailure: Pass the output of this into the addResult() function to preoperly log an exception
     * (or other throwable). Do not use this for an exception that your test EXPECTED to get. Use this only
     * for unexpected exceptions that should fail the test.
     *
     * @param thisFailure: The Exception (or throwable) relevant to this result.
     */
    //fun getResultForFailure(thisFailure: Throwable, section: String = "") = getResultForIncident(TestStatus.FAIL, section, thisFailure)
    fun getResultForFailure(thisFailure: Throwable) = getResultForIncident(TestStatus.FAIL, "", thisFailure)

    // Is virtual/open in C#
    /**
     * getResultForPreclusionInSetup: Pass the output of this into the addResult() function for unintended exceptions,
     * or other throwables, that occur in setup(). Do not use this for an exception that your test EXPECTED to get. Use this only
     * for unexpected exceptions that should make the test inconclusive.
     *
     * @param thisPreclusion: The Exception (or throwable) relevant to this result.
     */
    fun getResultForPreclusionInSetup(thisPreclusion: Throwable) = getResultForIncident(TestStatus.INCONCLUSIVE, SETUP, thisPreclusion)

    // Is virtual/open in C#
    /**
     * getResultForPreclusion: Pass the output of this into the addResult() function for unintended exceptions,
     * or other throwables, that occur in the main portion of the test. Do not use this for an exception that your test EXPECTED to get. Use this only
     * for unexpected exceptions that should make the test inconclusive.
     *
     * @param thisPreclusion: The Exception (or throwable) relevant to this result.
     */
    fun getResultForPreclusion(thisPreclusion: Throwable) = getResultForIncident(TestStatus.INCONCLUSIVE, "", thisPreclusion)

    // Is virtual/open in C# and called "reportFailureInCleanup()"
    /**
     * getResultForPreclusion: Pass the output of this into the addResult() function for unintended exceptions,
     * or other throwables, that occur in the main portion of the test. Do not use this for an exception that your test EXPECTED to get. Use this only
     * for unexpected exceptions that should make the test inconclusive.
     *
     * @param thisPreclusion: The Exception (or throwable) relevant to this result.
     */
    fun getResultForFailureInCleanup(thisPreclusion: Throwable) = getResultForIncident(TestStatus.SUBJECTIVE, CLEANUP, thisPreclusion)

    /**
     * waitSeconds: This will wait the desired number of seconds, and properly log that the delay took place.
     *
     */
    fun waitSeconds(howMany: Long) {
        log.info("Waiting $howMany seconds...", INFO_ICON)
        Thread.sleep(1000 * howMany)
    }

    /**
     * waitMilliseconds: This will wait the desired number of milliseconds, and properly log that the delay took place.
     *
     */
    fun waitMilliseconds(howMany: Long) {
        log.info("Waiting $howMany milliseconds...", INFO_ICON)
        Thread.sleep(howMany)
    }

    /**
     * makeSubjective: Calling this forces the test to become subjective. (A subjective TestResult is added to the results list.)
     *
     */
    fun makeSubjective() {
        testContext!!.results.add(TestResult(TestStatus.SUBJECTIVE, "This test case requires analysis by appropriate personnel to determine pass/fail status"))
    }

    // This was virtual/open in the C# version
    internal fun runTest(rootDirectory: String) {
        if (KILL_SWITCH) {
            // Decline to run
            // Deliberate NO-OP
        } else {
            parentArtifactsDirectory = rootDirectory
            val expectedFileName = artifactsDirectory + File.separatorChar + logFileName

            //forceParentDirectoryExistence(expectedFileName)
            // Force the parent directory to exist...
            File(expectedFileName).parentFile.mkdirs()

            testContext = TestPhaseContext(Memoir(name, stdout, PrintWriter(expectedFileName), ::logHeader))

            if (detailedDescription != UNSET_DESCRIPTION) {
                testContext!!.memoir.writeToHTML("<small><i>$detailedDescription</i></small>", EMOJI_TEXT_BLANK_LINE)
            }

            testContext!!.memoir.skipLine()

            // SETUP
            try {
                try {
                    setupContext.results.add(TestResult(TestStatus.PASS, "Setup was run"))
                    setup()
                } finally {
                    wasSetup = true
                    if (setupContext.memoir.wasUsed) {
                        var style = "implied_bad"
                        if (setupContext.overallStatus.isPassing()) { style = "implied_good" }
                        testContext!!.memoir.showMemoir(setupContext.memoir, EMOJI_SETUP, style)
                    }
                }
            } catch (thisFailure: Throwable) {
                addResult(getResultForPreclusionInSetup(thisFailure))
            }

            // RUN THE ACTUAL TEST
            if (setupContext.overallStatus.isPassing() && (! KILL_SWITCH)) {
                try {
                    executionThread = thread(start = true) { performTest() }
                    executionThread!!.join()
                } catch (thisFailure: Throwable) {
                    addResult(getResultForFailure(thisFailure))
                } finally {
                    wasRun = true
                    executionThread = null
                }
            } else {
                val thisResult = TestResult(TestStatus.INCONCLUSIVE, "Declining to perform test case $identifiedName because setup method failed.")
                testContext!!.memoir.showTestResult((thisResult))
                testContext!!.results.add(thisResult)
            }

            // CLEANUP
            try {
                cleanup()
                wasCleanedUp = true
            } catch (thisFailure: Throwable) {
                addResult(getResultForFailureInCleanup(thisFailure))
            } finally {
                if (cleanupContext.memoir.wasUsed) {
                    var style = "implied_bad"
                    if (cleanupContext.overallStatus.isPassing()) { style = "implied_good" }
                    testContext!!.memoir.showMemoir(cleanupContext.memoir, EMOJI_CLEANUP, style)
                }
            }

            val overall = overallStatus.toString()
            val emoji = overallStatus.memoirIcon
            testContext!!.memoir.writeToHTML("<h2>Overall Status: $overall</h2>", emoji)
            testContext!!.memoir.echoPlainText("Overall Status: $overall", emoji)
            testContext!!.memoir.conclude()
        }
    }
}