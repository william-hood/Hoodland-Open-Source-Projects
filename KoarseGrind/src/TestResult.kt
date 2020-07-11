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

// C#/Java Log() is replaced by extension function Memoir.ShowTestResult()
// Workflow Change: In Java/C# you would use TestResult.From<whatever>. In Kotlin use <whatever>.toTestResult()

package rockabilly.koarsegrind

import rockabilly.memoir.Memoir
import rockabilly.memoir.showThrowable

val SUMMARY_HEADERS = arrayOf("Criterion", "Status", "Artifacts", "Failures")

enum class TestConditionalType {
    // In older versions of Coarse Grind, these were
    // Unspecified, Prerequisite, PassCriterion
    CONSIDERATION, REQUIREMENT, ASSERTION;

    fun toTestResult(condition: Boolean, conditionDescription: String) : TestResult {
        var prefix = ""

        when (this) {
            ASSERTION -> prefix = "(Assertion) "
            REQUIREMENT -> prefix = "(Requirement) "
        }

        val result = TestResult()

        if (condition) {
            result.status = TestStatus.Pass
        } else {
            when (this) {
                ASSERTION -> result.status = TestStatus.Fail
                CONSIDERATION -> result.status = TestStatus.Subjective
                // Otherwise leave the default status of inconclusive in-place.
            }
        }

        result.description = prefix + conditionDescription
        return result
    }
}

class TestResult (var status: TestStatus = TestStatus.Inconclusive, var description: String = "(no description provided)", vararg associatedFailures: Throwable) {
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

fun Memoir.showTestResult(thisResult: TestResult) {
    // Should this be a subordinate memoir???
    this.showTestStatus(thisResult.status, thisResult.description)
    thisResult.failures.forEach {
        this.showThrowable(it)
    }
}