/*

HOODLAND OPEN SOURCE PROJECTS
KOARSE GRIND TEST SUITE

This is not just an example, but an actual Koarse Grind test suite for some
of the author's legacy projects. While it's not yet exhaustive it shows a
more real-world example of Koarse Grind in action.

 */
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


package hoodland.opensource.testsuite

import hoodland.opensource.koarsegrind.TestProgram

fun main(args: Array<String>) {
    // The line below will force a workaround so at least the HTML
    // output is correct when running on Windows. You may also need
    // to get the new Windows Terminal and Set Windows to use UTF-8
    // by default (like every other OS does). To do this open Windows
    // Settings, then search for and go into Language Settings. Scroll
    // down and click "Administrative language settings", then
    // click "Change system locale" and check the box labeled
    // "Beta: Use Unicode UTF-8 for worldwide language support".
    // As of June 2023 the line below is only necessary on Microsoft
    // Windows and only when not running via IntelliJ or Eclipse.
    System.setProperty("file.encoding", "UTF-8")

    TestProgram.run("Test Suite - Hoodland Open Source Projects", args = args)
}