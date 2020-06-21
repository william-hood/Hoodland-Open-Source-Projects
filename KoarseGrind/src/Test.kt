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

import rockabilly.memoir.Memoir
import rockabilly.memoir.ShowThrowable
import rockabilly.memoir.UNSET_STRING
import rockabilly.toolbox.forceParentDirectoryExistence
import rockabilly.toolbox.stdout
import java.io.File
import java.io.PrintWriter

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

abstract class Test (Name: String, DetailedDescription: String = "(no details)", ID: String = "", vararg Categories: String) {
    // Client code must implement or override
    open fun Setup(): Boolean { return true }
    open fun Cleanup(): Boolean { return true }
    abstract fun PerformTest();

    // Class Members
    internal var topLevelMemoir: Memoir? = null
    internal var setupMemoir: Memoir? = null
    internal var cleanupMemoir: Memoir? = null
    private var parentArtifactsDirectory = UNSET_STRING
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
    // Basically this needs to be thread safe
    internal val Progress: Float
    get() {
        var result: Float = 0.toFloat()
        if (wasSetup) result += 0.33.toFloat()
        if (wasRun) result += 0.34.toFloat()
        if (wasCleanedUp) result += 0.33.toFloat()
        return result
    }

    // For some reason in the C# version this was open/virtual
    fun AddResult(thisResult: TestResult) {
        Log.ShowTestResult((thisResult))
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

        // This is a direct translation from C#. Spacing looks suspicious... ??? Also, it insisted on topLevelMemoir instead of the Log property???
        Log.Error("$IdentifiedName$CLEANUP: An unanticipated failure occurred$additionalMessage.")
        Log.ShowThrowable(thisFailure)
    }

    private val indicateSetup: Memoir
        get() = Memoir("Setup - $echelonName $IdentifiedName", stdout)

    private val indicateCleanup: Memoir
        get() = Memoir("Cleanup - $echelonName $IdentifiedName", stdout)

    private val indicateBody: Memoir
        get() = Memoir("$echelonName $IdentifiedName", stdout)

    fun WaitSeconds(howMany: Long) {
        Log.Info("Waiting $howMany seconds...", INFO_ICON)
        Thread.sleep(1000 * howMany)
    }

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
    // TODO: Not yet finished.  Needs some functions in the CoarseGrind object in C#
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
            topLevelMemoir!!.SkipLine()

            // Left off Test.cs Line 194 - if (detailedDescription != default(string))
        }
    }
}