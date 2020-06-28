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

package rockabilly.koarsegrind

import rockabilly.memoir.*
import rockabilly.toolbox.UnsetString
import rockabilly.toolbox.forceParentDirectoryExistence
import rockabilly.toolbox.stdout
import java.io.File
import java.io.PrintWriter
import kotlin.concurrent.thread

enum class TestPriority {
    HappyPath, Critical, Normal, Ancillary
}

fun String.toTestPriority(): TestPriority {
    if (this.toUpperCase().startsWith("N")) { return TestPriority.Normal }
    if (this.toUpperCase().startsWith("A")) { return TestPriority.Ancillary }
    if (this.toUpperCase().startsWith("C")) { return TestPriority.Critical }
    return TestPriority.HappyPath
}

internal const val INFO_ICON = "ℹ️"
internal const val inProgressName = "(test in progress)"
internal const val SETUP = "setup"
internal const val CLEANUP = "cleanup"
internal const val UNSET_DESCRIPTION = "(no details)"

abstract class Test (Name: String, DetailedDescription: String = UNSET_DESCRIPTION, ID: String = "", vararg Categories: String) {
    // Client code must implement or override
    open fun Setup(): Boolean { return true }
    open fun Cleanup(): Boolean { return true }
    abstract fun PerformTest();

    // Class Members
    internal var topLevelMemoir: Memoir? = null
    internal var setupMemoir: Memoir? = null
    internal var cleanupMemoir: Memoir? = null
    private var parentArtifactsDirectory = UnsetString
    var Priority = TestPriority.Normal
    private var executionThread: Thread? = null
    internal var wasSetup = false
    internal var wasRun = false
    internal var wasCleanedUp = false

    //Should this be internal???
    val Results = ArrayList<TestResult>()

    // For readability these are now passed into the base constructor
    // and are no longer abstract properties
    internal var identifier = ID
    internal var name = Name
    private var detailedDescription = DetailedDescription
    private var categories: Array<out String> = Categories

    // TODO: Assert enforcer
    // TODO: Require enforcer
    // TODO: Consider enforcer

    // If there's ever some reason to call it something other than a test, change this.
    open protected val echelonName = "Test"

    private val categorization: String
        get() {
            val result = StringBuilder()
            categories.forEach {
                if (result.length > 0 ) { result.append('/') }
                result.append(it)
            }

            return result.toString()
        }

    val Log: Memoir
     get() = // https://stackoverflow.com/questions/4065518/java-how-to-get-the-caller-function-name/46590924
         when (Thread.currentThread().stackTrace[1].methodName) { // slot [2]???
             "Setup" -> this.setupMemoir!!
             "Cleanup" -> this.cleanupMemoir!!
             else -> this.topLevelMemoir!!
         }

    // In C#: [MethodImpl(MethodImplOptions.Synchronized)]
    // Basically this needs to be thread safe.
    internal val Progress: Float
    get() {
        // According to https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/synchronized.html
        // "Deprecated: Synchronization on any object is not supported on every platform and will be removed from the common standard library soon."
        synchronized(this) {
            var result: Float = 0.toFloat()
            if (wasSetup) result += 0.33.toFloat()
            if (wasRun) result += 0.34.toFloat()
            if (wasCleanedUp) result += 0.33.toFloat()
            return result
        }
    }

    // For some reason in the C# version this was open/virtual
    fun AddResult(thisResult: TestResult) {
        topLevelMemoir!!.ShowTestResult((thisResult)) // Should be Log instead of topLevelMemoir???
        Results.add(thisResult)
    }

    val OverallStatus: TestStatus
        get() {
            if ((Results.size < 1)) return TestStatus.Inconclusive
            var finalValue = TestStatus.Pass
            Results.forEach {
                finalValue = finalValue + it.Status
            }

            return finalValue;
        }

    // Is virtual/open in C#
    val ArtifactsDirectory: String
        get() = parentArtifactsDirectory + File.separatorChar + inProgressName

    // Is virtual/open in C#
    val IdentifiedName: String
        get() {
            if (identifier.length < 1) return name
            return "$identifier - $name"
        }

    // Is virtual/open in C#
    val PrefixedName: String
        get() = "$OverallStatus - $IdentifiedName"

    // Is virtual/open in C#
    val LogFileName: String
        get() = "$IdentifiedName Log.html"

    private fun filterForSummary(it: String): String {
        // C# return Regex.Replace(Regex.Replace(it, "[,\r\f\b]", ""), "[\t\n]", " ");
        return it.replace(Regex("[,\r\b]"), "").replace(Regex("[\t\n]"), "")
    }

    internal val SummaryDataRow: ArrayList<String>
        get() {
            val result = ArrayList<String>()
            result.add(filterForSummary(categorization))
            result.add(filterForSummary(Priority.toString()))
            result.add(filterForSummary(identifier))
            result.add(filterForSummary(name))
            result.add(filterForSummary(detailedDescription))
            result.add(filterForSummary(OverallStatus.toString()))

            val reasoning = StringBuilder()
            Results.forEach {
                if (! it.Status.isPassing()) {
                    if (reasoning.length > 0) {
                        reasoning.append("; ")
                    }
                        reasoning.append(it.Description)

                        if (it.hasFailures) {
                            it.Failures.forEach { thisFailure ->
                                if (reasoning.length > 0) {
                                    reasoning.append("; ")
                                }

                                reasoning.append(thisFailure.javaClass.simpleName)
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

        val result = TestResult(status, "$IdentifiedName$section: An unanticipated failure occurred.")
        result.Failures.add(failure)
        return result
    }


    // Is virtual/open in C#
    fun GetResultForFailure(thisFailure: Throwable, section: String = "") = getResultForIncident(TestStatus.Fail, section, thisFailure)

    // Is virtual/open in C#
    fun GetResultForPreclusionInSetup(thisPreclusion: Throwable) = getResultForIncident(TestStatus.Inconclusive, SETUP, thisPreclusion)

    // Is virtual/open in C#
    fun GetResultForPreclusion(thisPreclusion: Throwable) = getResultForIncident(TestStatus.Inconclusive, "", thisPreclusion)

    // Is virtual/open in C#
    fun ReportFailureInCleanup(thisFailure: Throwable, additionalMessage: String = "") {
        var message = StringBuilder()
        if (additionalMessage.length > 0) { message.append(" ") }
        message.append(additionalMessage)

        // This is a direct translation from C#. Spacing looks suspicious... ???
        topLevelMemoir!!.Error("$IdentifiedName$CLEANUP: An unanticipated failure occurred$additionalMessage.") // Should be Log instead of topLevelMemoir???
        topLevelMemoir!!.ShowThrowable(thisFailure)
    }

    private val indicateSetup: Memoir
        get() = Memoir("Setup - $echelonName $IdentifiedName", stdout)

    private val indicateCleanup: Memoir
        get() = Memoir("Cleanup - $echelonName $IdentifiedName", stdout)

    private val indicateBody: Memoir
        get() = Memoir("$echelonName $IdentifiedName", stdout)

    // C# version used topLevelMemoir. This would not work during Setup() or Cleanup()
    fun WaitSeconds(howMany: Long) {
        Log.Info("Waiting $howMany seconds...", INFO_ICON)
        Thread.sleep(1000 * howMany)
    }

    // C# version used topLevelMemoir. This would not work during Setup() or Cleanup()
    fun WaitMilliseconds(howMany: Long) {
        Log.Info("Waiting $howMany milliseconds...", INFO_ICON)
        Thread.sleep(howMany)
    }

    fun Interrupt() {
        try {
            executionThread?.interrupt()
            // C# version was followed by executionThread.Abort() three times in a row.
        } catch (dontCare: Exception) {
            // Deliberate NO-OP
        }
    }

    // This was virtual/open in the C# version
    fun RunTest(rootDirectory: String) {
        if (KILL_SWITCH) {
            // Decline to run
            // Deliberate NO-OP
        } else {
            var setupResult = true
            var cleanupResult = true
            parentArtifactsDirectory = rootDirectory
            val expectedFileName = ArtifactsDirectory + File.separatorChar + LogFileName

            forceParentDirectoryExistence(expectedFileName)
            topLevelMemoir = Memoir(name, stdout, PrintWriter(expectedFileName), ::LogHeader)

            if (detailedDescription != UNSET_DESCRIPTION) {
                topLevelMemoir!!.WriteToHTML("<small><i>$detailedDescription</i></small>", EMOJI_TEXT_BLANK_LINE)
            }

            topLevelMemoir!!.SkipLine()
            val before = SetupEnforcement(this)

            // SETUP
            try {
                setupMemoir = indicateSetup
                try {
                    setupResult = Setup()
                } finally {
                    wasSetup = true
                    if (setupMemoir!!.wasUsed) {
                        var style = "decaf_orange_light_roast"
                        if (setupResult) { style = "decaf_green_light_roast" }
                        topLevelMemoir!!.ShowMemoir(setupMemoir!!, EMOJI_SETUP, style)
                    }
                }
            } catch (thisFailure: Throwable) {
                setupResult = false
                AddResult(GetResultForPreclusionInSetup(thisFailure))
            } finally {
                if (!SetupEnforcement(this).matches(before)) {
                    setupResult = false
                    AddResult(TestResult(TestStatus.Inconclusive, "PROGRAMMING ERROR: It is illegal to change the identifier, name, or priority in Setup.  This must happen in the constructor. Setup may also not add Test Results."))
                }
            }

            // RUN THE ACTUAL TEST
            if (setupResult && (! KILL_SWITCH)) {
                try {
                    executionThread = thread(start = true) { PerformTest() }
                    executionThread!!.join()
                } catch (thisFailure: Throwable) {
                    AddResult(GetResultForFailure(thisFailure))
                } finally {
                    wasRun = true
                    executionThread = null
                }
            } else {
                AddResult(TestResult(TestStatus.Inconclusive, "Declining to perform test case $IdentifiedName because setup method failed."))
            }

            // CLEANUP
            cleanupMemoir = indicateCleanup
            try {
                cleanupResult = Cleanup()
                wasCleanedUp = true
            } catch (thisFailure: Throwable) {
                ReportFailureInCleanup(thisFailure)
            } finally {
                if (cleanupMemoir!!.wasUsed) {
                    var style = "decaf_orange_light_roast"
                    if (cleanupResult) { style = "decaf_green_light_roast" }
                    topLevelMemoir!!.ShowMemoir(cleanupMemoir!!, EMOJI_CLEANUP, style)
                }
            }

            val overall = OverallStatus.toString()
            val emoji = OverallStatus.memoirIcon
            topLevelMemoir!!.WriteToHTML("<h2>Overall Status: $overall</h2>", emoji)
            topLevelMemoir!!.EchoPlainText("Overall Status: $overall", emoji)
            topLevelMemoir!!.Conclude()
        }
    }
}