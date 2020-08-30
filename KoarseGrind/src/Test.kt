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

abstract class Test (
        internal val name: String,
        private val detailedDescription: String = UNSET_DESCRIPTION,
        internal val identifier: String = "",
        vararg categories: String) {
        internal var categories: Array<out String> = categories
        internal val setupContext = TestPhaseContext(Memoir("Setup - Test $identifiedName", stdout))
        internal val cleanupContext = TestPhaseContext(Memoir("Cleanup -  Test $identifiedName", stdout))
        internal var testContext: TestPhaseContext? = null
        private var parentArtifactsDirectory = UNSET_STRING
        private var executionThread: Thread? = null
        internal var wasSetup = false
        internal var wasRun = false
        internal var wasCleanedUp = false

        // Assertions
        protected val assert = Enforcer(TestConditionalType.ASSERTION, this)
        protected val require = Enforcer(TestConditionalType.REQUIREMENT, this)
        protected val consider = Enforcer(TestConditionalType.CONSIDERATION, this)

    // Client code must implement or override
    open fun setup() { setupContext.results.add(TestResult(TestStatus.PASS, "(no user-supplied setup)")) }
    open fun cleanup() { setupContext.results.add(TestResult(TestStatus.PASS, "(no user-supplied cleanup)")) }
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

    val log: Memoir
     get() = currentContext.memoir

    val results: ArrayList<TestResult>
            get() = currentContext.results

    internal val currentContext: TestPhaseContext
        get() {
            if (wasSetup) {
                if (! setupContext.overallStatus.isPassing()) {
                    return cleanupContext
                }

                testContext?.let {
                    return it
                }

                return cleanupContext
            }

            if (wasRun) { return cleanupContext }
            return setupContext
        }

    // In C#: [MethodImpl(MethodImplOptions.Synchronized)]
    // TODO: This needs to be thread safe.
    internal val progress: Float
    get() {
        // According to https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/synchronized.html
        // "Deprecated: Synchronization on any object is not supported on every platform and will be removed from the common standard library soon."
        synchronized(this) {
            var result: Float = 0.toFloat()
            val check = currentContext
            if (check === testContext) result += 0.33.toFloat()
            if (check === cleanupContext) result += 0.34.toFloat()
            if (wasCleanedUp) result += 0.33.toFloat()
            return result
        }
    }

    // For some reason in the C# version this was open/virtual
    fun addResult(thisResult: TestResult) {
        val context = currentContext
        context.memoir.showTestResult((thisResult))
        context.results.add(thisResult)
    }

    val overallStatus: TestStatus
        get() {
            if (! setupContext.overallStatus.isPassing()) return TestStatus.INCONCLUSIVE
            return testContext!!.overallStatus
        }

    // Is virtual/open in C#
    val artifactsDirectory: String
        get() = parentArtifactsDirectory + File.separatorChar + IN_PROGRESS_NAME

    // Is virtual/open in C#
    val identifiedName: String
        get() {
            if (identifier.length < 1) return name
            return "$identifier - $name"
        }

    // Is virtual/open in C#
    val prefixedName: String
        get() = "$overallStatus - $identifiedName"

    // Is virtual/open in C#
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
    fun getResultForFailure(thisFailure: Throwable, section: String = "") = getResultForIncident(TestStatus.FAIL, section, thisFailure)

    // Is virtual/open in C#
    fun getResultForPreclusionInSetup(thisPreclusion: Throwable) = getResultForIncident(TestStatus.INCONCLUSIVE, SETUP, thisPreclusion)

    // Is virtual/open in C#
    fun getResultForPreclusion(thisPreclusion: Throwable) = getResultForIncident(TestStatus.INCONCLUSIVE, "", thisPreclusion)

    // Is virtual/open in C# and called "reportFailureInCleanup()"
    fun getResultForFailureInCleanup(thisPreclusion: Throwable) = getResultForIncident(TestStatus.SUBJECTIVE, CLEANUP, thisPreclusion)

    fun waitSeconds(howMany: Long) {
        log.info("Waiting $howMany seconds...", INFO_ICON)
        Thread.sleep(1000 * howMany)
    }

    fun waitMilliseconds(howMany: Long) {
        log.info("Waiting $howMany milliseconds...", INFO_ICON)
        Thread.sleep(howMany)
    }

    fun interrupt() {
        try {
            executionThread?.interrupt()
            // C# version was followed by executionThread.Abort() three times in a row.
        } catch (dontCare: Exception) {
            // Deliberate NO-OP
        }
    }

    fun makeSubjective() {
        testContext!!.results.add(TestResult(TestStatus.SUBJECTIVE, "This test case requires analysis by appropriate personnel to determine pass/fail status"))
    }

    // This was virtual/open in the C# version
    fun runTest(rootDirectory: String) {
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
            val before = SetupEnforcement(this)

            // SETUP
            try {
                try {
                    setup()
                } finally {
                    wasSetup = true
                    if (setupContext.memoir.wasUsed) {
                        var style = "decaf_orange_light_roast"
                        if (setupContext.overallStatus.isPassing()) { style = "decaf_green_light_roast" }
                        testContext!!.memoir.showMemoir(setupContext.memoir, EMOJI_SETUP, style)
                    }
                }
            } catch (thisFailure: Throwable) {
                addResult(getResultForPreclusionInSetup(thisFailure))
            } finally {
                if (!SetupEnforcement(this).matches(before)) {
                    addResult(TestResult(TestStatus.INCONCLUSIVE, "PROGRAMMING ERROR: It is illegal to change the identifier, name, or priority in Setup.  This must happen in the constructor."))
                }
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
                addResult(TestResult(TestStatus.INCONCLUSIVE, "Declining to perform test case $identifiedName because setup method failed."))
            }

            // CLEANUP
            try {
                cleanup()
                wasCleanedUp = true
            } catch (thisFailure: Throwable) {
                addResult(getResultForFailureInCleanup(thisFailure))
            } finally {
                if (cleanupContext.memoir.wasUsed) {
                    var style = "decaf_orange_light_roast"
                    if (cleanupContext.overallStatus.isPassing()) { style = "decaf_green_light_roast" }
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