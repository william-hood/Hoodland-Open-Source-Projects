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

import hoodland.opensource.koarsegrind.Test
import hoodland.opensource.toolbox.*

class TestStatusCodeDescription:Test(
        "Int.toStatusCodeDescription()",
        "This verifies that toStatusCodeDescription() gets the correct string value for a few given numbers. It is NOT exhaustive.",
        "TB-SC-01",
        "Toolbox", "StatusCode", "All"
) {
    override fun performTest() {
        assert.shouldBeEqual(100.toStatusCodeDescription(), "Continue", "Status code 100 should map to 'Continue'")
        assert.shouldBeEqual(200.toStatusCodeDescription(), "OK", "Status code 200 should map to 'OK'")
        assert.shouldBeEqual(404.toStatusCodeDescription(), "Not Found", "Status code 404 should map to 'Not Found'")
        assert.shouldBeEqual(500.toStatusCodeDescription(), "Internal Server Error", "Status code 500 should map to 'Internal Server Error'")
    }
}

class TestErrorStatusCode:Test(
        "Int.isErrorStatusCode",
        "This verifies that Int.isErrorStatusCode gets the correct Boolean value for a few given numbers, reading 'true' for 4xx and 5xx. It is NOT exhaustive.",
        "TB-SC-02",
        "Toolbox", "StatusCode", "All"
) {
    override fun performTest() {
        for (candidate in -5..605) {
            if (candidate > 399 && candidate < 600) {
                assert.shouldBeTrue(candidate.isErrorStatusCode, "Status code $candidate should be a valid error code")
            } else {
                assert.shouldBeFalse(candidate.isErrorStatusCode, "Status code $candidate should NOT be considered a valid error code")
            }
        }
    }
}

class TestSuccessfulStatusCode:Test(
        "Int.isSuccessfulStatusCode",
        "This verifies that Int.isSuccessfulStatusCode gets the correct Boolean value for a few given numbers, reading 'true' for 2xx. It is NOT exhaustive.",
        "TB-SC-03",
        "Toolbox", "StatusCode", "All"
) {
    override fun performTest() {
        for (candidate in -5..605) {
            if (candidate > 199 && candidate < 300) {
                assert.shouldBeTrue(candidate.isSuccessfulStatusCode, "Status code $candidate should be a successful status code")
            } else {
                assert.shouldBeFalse(candidate.isSuccessfulStatusCode, "Status code $candidate should NOT be considered a successful status code")
            }
        }
    }
}

class TestValidStatusCode:Test(
        "Int.isValidStatusCode",
        "This verifies that Int.isValidStatusCode gets the correct Boolean value for a few given numbers, reading 'true' for anything between 100 and 599. It is NOT exhaustive.",
        "TB-SC-04",
        "Toolbox", "StatusCode", "All"
) {
    override fun performTest() {
        for (candidate in -5..605) {
            if (candidate > 99 && candidate < 600) {
                assert.shouldBeTrue(candidate.isValidStatusCode, "Status code $candidate should be a valid status code")
            } else {
                assert.shouldBeFalse(candidate.isValidStatusCode, "Status code $candidate should NOT be considered a valid status code")
            }
        }
    }
}

class TestInvalidStatusCode:Test(
        "Int.isInvalidStatusCode",
        "This verifies that Int.isInvalidStatusCode gets the correct Boolean value for a few given numbers, reading 'true' for anything NOT between 100 and 599. It is NOT exhaustive.",
        "TB-SC-05",
        "Toolbox", "StatusCode", "All"
) {
    override fun performTest() {
        for (candidate in -5..605) {
            if (candidate > 99 && candidate < 600) {
                assert.shouldBeFalse(candidate.isInvalidStatusCode, "Status code $candidate should NOT be considered an invalid status code")
            } else {
                assert.shouldBeTrue(candidate.isInvalidStatusCode, "Status code $candidate should be an invalid status code")
            }
        }
    }
}