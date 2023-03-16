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

// C#/Java Log() is replaced by extension function Memoir.ShowTestStatus()

package hoodland.opensource.koarsegrind

import hoodland.opensource.memoir.*

/**
 * TestStatus: Used to indicate whether a test result is passing, failing, inconclusive or subjective.
 *
 */
enum class TestStatus {
    /**
     * INCONCLUSIVE indicates that a test can neither be passing nor failing. Tests should be made
     * inconclusive if there is a failure during the setup() function, or if any condition is
     * detected that invalidates the test results. Using "FAIL" for tests that are in actuality
     * inconclusive is strongly discouraged. The "require" check (a "requirement") will make a
     * test inconclusive if it fails. A test with no results at all (no "assert", "require", or "consider"
     * checks) will also be considered INCONCLUSIVE.
     */
    INCONCLUSIVE {
        override val memoirIcon = EMOJI_INCONCLUSIVE_TEST
        override val memoirStyle = "inconclusive_test_result"
    },

    /**
     * FAIL indicates, well, that the test failed. Fail should only be used if a condition is found
     * that explicitly fails the test. (Example: The output of a function was supposed to be 42 and it wasn't.)
     * Do not use FAIL for conditions that make it impossible to get a passing result in the first place,
     * such as an invalid configuartion. Use INCONCLUSIVE instead for conditions that make a valid result impossible.
     * Use the "assert" check (an "assertion") to produce FAIL status if the check does not succeed.
     */
    FAIL {
        override val memoirIcon = EMOJI_FAILING_TEST
        override val memoirStyle = "failing_test_result"
    },

    /**
     * SUBJECTIVE means that it is beyond mechanical means to determine if the result is good. In other
     * words, it means a human needs to evaluate the result. A test can be made subjective by calling
     * the makeSubjective() function at any time. A failed "consider" check (a "consideration") also
     * renders the test inconclusive.
     */
    SUBJECTIVE {
        override val memoirIcon = EMOJI_SUBJECTIVE_TEST
        override val memoirStyle = "old_parchment"
    },

    /**
     * PASS means that everything is good. A test must have at least one result in order to pass,
     * and must have no results that are FAIL, INCONCLUSIVE, or SUBJECTIVE. If a test has no
     * results at all, it is INCONCLUSIVE rather than passing.
     */
    PASS {
        override val memoirIcon = EMOJI_PASSING_TEST
        override val memoirStyle = "passing_test_result"
    };

    abstract val memoirIcon: String
    abstract val memoirStyle: String

    fun isPassing(): Boolean {
        return this == PASS
    }

    fun isFailing(): Boolean {
        return this == FAIL
    }

    fun isInconclusive(): Boolean {
        return this == INCONCLUSIVE
    }

    // Using the + operator in place of CombineWith()
    operator fun plus(theOther: TestStatus): TestStatus {
        when (this) {
            SUBJECTIVE -> return SUBJECTIVE
            INCONCLUSIVE -> {
                if (theOther == SUBJECTIVE) return SUBJECTIVE
                return INCONCLUSIVE
            }
            FAIL -> {
                if (theOther == SUBJECTIVE) return SUBJECTIVE
                if (theOther == INCONCLUSIVE) return INCONCLUSIVE
                return FAIL
            }
            else -> {
                if (theOther == SUBJECTIVE) return SUBJECTIVE
                if (theOther == INCONCLUSIVE) return INCONCLUSIVE
                if (theOther == FAIL) return FAIL
                return PASS
            }
        }
    }
}

/**
 * showTestStatus: An extension method that allows any Memoir (the HTML logger) in Koarse Grind to properly display a TestStatus.
 *
 * @param message Any information relevant as to what caused this status. Typically this is the description property of a TestResult.
 */
fun Memoir.showTestStatus(thisStatus: TestStatus, message: String) {
    this.info(message, thisStatus.memoirIcon)
}

/**
 * toTestStatus: converts a string such as "pass" or "fail" into a TestStatus.
 * Defaults to inconclusive if it's not obvious.
 */
fun String.toTestStatus(): TestStatus {
    if (this.toUpperCase().startsWith("P")) { return TestStatus.PASS }
    if (this.toUpperCase().startsWith("F")) { return TestStatus.FAIL }
    return TestStatus.INCONCLUSIVE
}