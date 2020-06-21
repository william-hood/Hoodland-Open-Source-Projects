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

package rockabilly.koarsegrind

import rockabilly.memoir.*

enum class TestStatus {
    Inconclusive {
        override val memoirIcon = EMOJI_INCONCLUSIVE_TEST
        override val memoirStyle = "decaf_orange_light_roast"
    },
    Fail {
        override val memoirIcon = EMOJI_FAILING_TEST
        override val memoirStyle = "decaf_orange"
    },
    Subjective {
        override val memoirIcon = EMOJI_SUBJECTIVE_TEST
        override val memoirStyle = "old_parchment"
    },
    Pass {
        override val memoirIcon = EMOJI_PASSING_TEST
        override val memoirStyle = "decaf_green"
    };

    abstract val memoirIcon: String
    abstract val memoirStyle: String

    fun isPassing(): Boolean {
        return this == Pass
    }

    fun isFailing(): Boolean {
        return this == Fail
    }

    fun isInconclusive(): Boolean {
        return this == Inconclusive
    }

    // Using the + operator in place of CombineWith()
    operator fun plus(theOther: TestStatus): TestStatus {
        when (this) {
            Subjective -> return Subjective
            Inconclusive -> {
                if (theOther == Subjective) return Subjective
                return Inconclusive
            }
            Fail -> {
                if (theOther == Subjective) return Subjective
                if (theOther == Inconclusive) return Inconclusive
                return Fail
            }
            else -> {
                if (theOther == Subjective) return Subjective
                if (theOther == Inconclusive) return Inconclusive
                if (theOther == Fail) return Fail
                return Pass
            }
        }
    }
}

fun Memoir.ShowTestStatus(thisStatus: TestStatus, message: String) {
    this.Info(message, thisStatus.memoirIcon)
}

fun String.toTestStatus(): TestStatus {
    if (this.toUpperCase().startsWith("P")) { return TestStatus.Pass }
    if (this.toUpperCase().startsWith("F")) { return TestStatus.Fail }
    return TestStatus.Inconclusive
}