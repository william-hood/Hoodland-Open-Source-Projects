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

package hoodland.opensource.testsuite

import hoodland.opensource.koarsegrind.Test
import hoodland.opensource.memoir.*

class TestDepictFailure : Test(
    "Depict Failure",
    "Verify that the Memoir-based depictFailure() function produces the expected string result. It should be properly indented, contain no superfluous text, and not contain the end-of-log emojis.",
    "Memoir",
    "MR-DF-01"
) {
    lateinit var target: Throwable

    fun makeAndPrint() {
        printSomeItem(intArrayOf(1, 5, 7, 9))
    }

    fun printSomeItem(doa: IntArray) {
        System.out.println(doa[46378])
    }

    val EXPECTED = """ArrayIndexOutOfBoundsException
Index 46378 out of bounds for length 4
* TestDepictFailure.kt line 40 in method printSomeItem() of class hoodland.opensource.testsuite.TestDepictFailure
* TestDepictFailure.kt line 36 in method makeAndPrint() of class hoodland.opensource.testsuite.TestDepictFailure
* TestDepictFailure.kt line 66 in method setup() of class hoodland.opensource.testsuite.TestDepictFailure
* Test.kt line 364 in method runSetup${'$'}KoarseGrind() of class hoodland.opensource.koarsegrind.Test
* Test.kt line 415 in method runTest${'$'}KoarseGrind() of class hoodland.opensource.koarsegrind.Test
* TestCategory.kt line 174 in method invoke() of class hoodland.opensource.koarsegrind.TestCategory${'$'}run${'$'}4
* TestCategory.kt line 174 in method invoke() of class hoodland.opensource.koarsegrind.TestCategory${'$'}run${'$'}4
* Thread.kt line 30 in method run() of class kotlin.concurrent.ThreadsKt${'$'}thread${'$'}thread${'$'}1

   Exception
   Just a fake exception to test this thing!
   * TestDepictFailure.kt line 68 in method setup() of class hoodland.opensource.testsuite.TestDepictFailure
   * Test.kt line 364 in method runSetup${'$'}KoarseGrind() of class hoodland.opensource.koarsegrind.Test
   * Test.kt line 415 in method runTest${'$'}KoarseGrind() of class hoodland.opensource.koarsegrind.Test
   * TestCategory.kt line 174 in method invoke() of class hoodland.opensource.koarsegrind.TestCategory${'$'}run${'$'}4
   * TestCategory.kt line 174 in method invoke() of class hoodland.opensource.koarsegrind.TestCategory${'$'}run${'$'}4
   * Thread.kt line 30 in method run() of class kotlin.concurrent.ThreadsKt${'$'}thread${'$'}thread${'$'}1"""

    override fun setup() {
        log.info("Constructing an Exception to test against")
        try {
            makeAndPrint()
        } catch (thisProblem: Throwable) {
            val fakeExceptionForDemo = Exception("Just a fake exception to test this thing!")
            thisProblem.initCause(fakeExceptionForDemo)
            target = thisProblem
        }

        assert.shouldNotBeNull(target, "The target should have been successfully initialized")

        log.info("This is the target Exception rendered as HTML...")
        log.show(target)
    }

    override fun performTest() {
        log.info("Calling function depictFailure() against the target Throwable")
        val actual = depictFailure(target)

        class Comparison {
            val Expected = treatAsCode(EXPECTED)
            val Actual = treatAsCode(actual)
        }

        log.showObject(Comparison(), "Actual vs. Expected")

        assert.shouldBeEqual(
            actual.replace("\r", ""),
            EXPECTED.replace("\r", ""),
            "Calling depictFailure() against the supplied target should produce the expected result"
        )
    }
}