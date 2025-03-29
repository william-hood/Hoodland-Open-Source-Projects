// Copyright (c) 2023, 2025 William Arthur Hood
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

package hoodland.opensource.koarsegrind.java;

import hoodland.opensource.boolog.java.Boolog;

/**
 * A manufactured test has to be produced by a test factory in order to be included in the test run.
 * This provides the option of constructing tests on-the-fly at runtime.
 *
 * If an optional setup() method exists it will be run first.
 * Failed assertions in setup() render the test inconclusive. If an optional cleanup() method exists it will always
 * be run, even if setup failed. The performTest() method will be run, unless setup failed.
 */
public abstract class ManufacturedTest extends hoodland.opensource.koarsegrind.ManufacturedTest {
    public ManufacturedTest(String name) {
        super (name, "", null, "");
    }

    public ManufacturedTest(String name, String detailedDescription) {
        super (name, detailedDescription, null, "");
    }

    public ManufacturedTest(String name, String detailedDescription, String categoryPath) {
        super (name, detailedDescription, categoryPath, "");
    }

    public ManufacturedTest(String name, String detailedDescription, String categoryPath, String identifier) {
        super (name, detailedDescription, categoryPath, identifier);
    }

    public hoodland.opensource.boolog.java.Boolog log() {
        return new Boolog(getLog());
    }

    public hoodland.opensource.koarsegrind.Enforcer assertion() {
        return super.getAssert();
    }

    public hoodland.opensource.koarsegrind.Enforcer require() {
        return super.getRequire();
    }

    public hoodland.opensource.koarsegrind.Enforcer consider() {
        return super.getConsider();
    }
}
