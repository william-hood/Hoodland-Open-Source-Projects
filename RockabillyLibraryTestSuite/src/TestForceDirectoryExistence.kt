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
import rockabilly.koarsegrind.UNSET_DESCRIPTION
import rockabilly.toolbox.*

// Start by making a temp folder based on UUID off user folder WITHOUT USING TOOLBOX FUNCTIONS
// Happy path: New Dir only one level off.
// Happy path: New Dir one level off, file specified.
// New dir several folders in.
// New dir several folders in, existing file specified.
// Existing Dir only one level off.
// Existing Dir one level off, file specified.
// Existing dir several folders in.
// Existing dir several folders in, nonexistent file specified.
// Existing dir several folders in, existing file specified.
// New dir several folders in, part of the path exists.
// New dir several folders in, part of the path exists, existing file specified.

// Probably need to make the default constructor for Test internal or private.
// Evaluate what needs to be marked open in the Test class.
abstract class DirectoryExistenceTest(name: String, detailedDescription: String, testCaseID: String, vararg categories: String): Test(name, detailedDescription, testCaseID, *categories) {
    override fun setup(): Boolean {
        return super.setup()
    }

    override fun cleanup(): Boolean {
        return super.cleanup()
    }
}

class TestReadLineFromInputStream:DirectoryExistenceTest(
        "readLineFromInputStream()",
        "This verfies that toStatusCodeDescription() gets the correct string value for a few given numbers. It is NOT exhaustive.",
        "TB-001",
        "Toolbox", "StatusCode", "All"
) {
    override fun performTest() {
        assert.shouldBeEqual(100.toStatusCodeDescription(), "Continue", "Status code 100 should map to 'Continue'")
        assert.shouldBeEqual(200.toStatusCodeDescription(), "OK", "Status code 200 should map to 'OK'")
        assert.shouldBeEqual(404.toStatusCodeDescription(), "Not Found", "Status code 404 should map to 'Not Found'")
        assert.shouldBeEqual(500.toStatusCodeDescription(), "Internal Server Error", "Status code 500 should map to 'Internal Server Error'")
    }
}