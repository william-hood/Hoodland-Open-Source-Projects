// Copyright (c) 2020, 2023, 2025 William Arthur Hood
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

// C#/Java Log() is replaced by extension function Boolog.ShowTestResult()
// Workflow Change: In Java/C# you would use TestResult.From<whatever>. In Kotlin use <whatever>.toTestResult()

package hoodland.opensource.koarsegrind

import hoodland.opensource.boolog.Boolog
import hoodland.opensource.boolog.showThrowable

val SUMMARY_HEADERS = arrayOf("Criterion", "Status", "Artifacts", "Failures")

/**
 * TestConditionalType is primarily for use internally by Koarse Grind to determine
 * what kind of check is being made.
 */
enum class TestConditionalType {
    // In older versions of Coarse Grind, these were
    // Unspecified, Prerequisite, PassCriterion
    CONSIDERATION, REQUIREMENT, ASSERTION;

    fun toTestResult(condition: Boolean, conditionDescription: String) : TestResult {
        var prefix = ""

        when (this) {
            ASSERTION -> prefix = "(Assertion) "
            REQUIREMENT -> prefix = "(Requirement) "
            else -> {}// DELIBERATE NO-OP
        }

        val result = TestResult()

        if (condition) {
            result.status = TestStatus.PASS
        } else {
            when (this) {
                ASSERTION -> result.status = TestStatus.FAIL
                CONSIDERATION -> result.status = TestStatus.SUBJECTIVE
                // Otherwise leave the default status of inconclusive in-place.
                else -> {}// DELIBERATE NO-OP
            }
        }

        result.description = prefix + conditionDescription
        return result
    }
}

/**
 * TestResult: This is the result of any conditional check, such as assertions, requirements, or unexpected exceptions.
 * A test may generate as many test results as it needs. A failing result overrides passing results and makes the test
 * fail. An inconclusive result overrides both passes and failures and makes the test inconclusive. A subjective
 * result overrides everything. If a test is subjective, it requires evaluation by a human.
 *
 * @property status Sets the status of this result to passing, failing, inconclusive or subjective.
 * @property description Provides general information explaining the result.
 *
 * @param associatedFailures If exceptions are relevant to this result, put them in here so they will be properly logged.
 */
class TestResult (var status: TestStatus = TestStatus.INCONCLUSIVE, var description: String = "(no description provided)", vararg associatedFailures: Throwable) {
    val failures = ArrayList<Throwable>()
    val artifacts = ArrayList<Any>()

    init {
        associatedFailures.toCollection(failures)
    }

    val hasArtifacts: Boolean
        get() = this.artifacts.count() > 0

    val hasFailures: Boolean
        get() = this.failures.count() > 0
}

/**
 * showTestResult:
 * An extension method that allows any Boolog (the HTML logger) in Koarse Grind to properly display a TestResult.
 * If there are exceptions, or other throwables, associated with the result it will show as a subsection rather than at
 * the root level.
 */
fun Boolog.showTestResult(thisResult: TestResult) {
    // If there are throwables associated with this, use a subordinate boolog
    if (thisResult.failures.size > 0) {
        val subordinate = Boolog(thisResult.description)
        thisResult.failures.forEach {
            subordinate.showThrowable(it)
        }

        this.showBoolog(subordinate, thisResult.status.boologIcon, thisResult.status.boologStyle)
    } else {
        this.showTestStatus(thisResult.status, thisResult.description)
    }
}