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

// C#/Java Log() is replaced by extension function Memoir.ShowTestStatus()

package hoodland.opensource.koarsegrind

import hoodland.opensource.memoir.*

enum class TestStatus {
    INCONCLUSIVE {
        override val memoirIcon = EMOJI_INCONCLUSIVE_TEST
        override val memoirStyle = "decaf_orange_light_roast"
    },
    FAIL {
        override val memoirIcon = EMOJI_FAILING_TEST
        override val memoirStyle = "decaf_orange"
    },
    SUBJECTIVE {
        override val memoirIcon = EMOJI_SUBJECTIVE_TEST
        override val memoirStyle = "old_parchment"
    },
    PASS {
        override val memoirIcon = EMOJI_PASSING_TEST
        override val memoirStyle = "decaf_green"
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

fun Memoir.showTestStatus(thisStatus: TestStatus, message: String) {
    this.info(message, thisStatus.memoirIcon)
}

fun String.toTestStatus(): TestStatus {
    if (this.toUpperCase().startsWith("P")) { return TestStatus.PASS }
    if (this.toUpperCase().startsWith("F")) { return TestStatus.FAIL }
    return TestStatus.INCONCLUSIVE
}