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

package hoodland.opensource.koarsegrind

/**
 * A manufactured test has to be produced by a test factory in order to be included in the test run.
 * This provides the option of constructing tests on-the-fly at runtime.
 *
 * If an optional setup() method exists it will be run first.
 * Failed assertions in setup() render the test inconclusive. If an optional cleanup() method exists it will always
 * be run, even if setup failed. The performTest() method will be run, unless setup failed.
 *
 * @property name A human-readable name for the test. This should not be too long. You may use a full sentence if you wish, but that might be better as the detailedDescription parameter.
 * @property detailedDescription This should be used to explain, in plain english, anything the test does that is not obvious to someone reviewing the results or editing the code. This is important if someone who did not write the test is assigned to write code that makes it pass.
 * @property categoryPath Tests are organized in a hierarchy of categories. This will be reflected in both the log file and the hierarchy of test result folders. Specify the test's fully qualified path, with path names separated by pipe ("|") characters. The test will be at the top level if this parameter is left null.
 * @property testCaseID You may omit this if you wish. If you are using a test tracking system that assigns a test case ID, this is the place it goes.
 *
 */
public abstract class ManufacturedTest(name: String, detailedDescription: String = UNSET_DESCRIPTION, categoryPath: String? = null, testCaseID: String = "") : Test(name, detailedDescription, categoryPath, testCaseID) {
}

/**
 * A TestFactory generates an TestCollection of ManufacturedTest objects. As with stand-alone tests, TestFactories
 * will be instantiated at runtime, automatically calling populateProducts(). Any manufactured tests sitting in
 * the products ArrayList will then be run as if they were stand-alone tests. You may also create a subordinate
 * TestCollection for some of your tests if they need to be grouped together. Note that the "products" TestCollection
 * will be it's own subordinate section of the log and its own subdirctory within the results. Any further TestCollections you
 * add into "products" will be another subordinate log/directory within that one.
 */
abstract class TestFactory()
 {
    val producedTests = ArrayList<ManufacturedTest>()

    abstract fun produceTests()

    init {
        produceTests()
    }
}