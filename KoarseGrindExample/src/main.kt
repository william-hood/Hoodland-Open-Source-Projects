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

import rockabilly.koarsegrind.Test
import rockabilly.koarsegrind.TestResult
import rockabilly.koarsegrind.TestStatus

class ExampleTest2: Test(
        "Sample Test Number Two",
        "This is the detailed description for ExampleTest2.  Use this field to describe what the test does and what its pass criteria are. Commas, \tTabs, \rCarriage Returns, and \nLine Feed chars will be filtered out.",
        "ET-002",
        "Simple", "All", "Example"
) {
    override fun PerformTest() {
        Log.Info("Reality check2")
        Log.Debug("Did it actually work2???")
        Results.add(TestResult(TestStatus.Pass, "Whelp2, 'Guess I'll just brute-force the dang thing as passing!"))
    }
}

fun main(args: Array<String>) {
    TestProgram.Run()
}