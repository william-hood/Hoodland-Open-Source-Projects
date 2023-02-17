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

package hoodland.opensource.koarsegrind

/**
 * A manufactured test has to be produced by a test factory in order to be included in the test run.
 * This provides the option of constructing tests on-the-fly at runtime.
 */
public abstract class ManufacturedTest(name: String, detailedDescription: String = UNSET_DESCRIPTION, ownerName: String? = null, testCaseID: String = "", vararg categories: String) : Test(name, detailedDescription, ownerName, testCaseID, *categories) {
}

/**
 * An Outfitter is used to provide a setup and teardown, without an actual test to perform.
 */
public open class Outfitter: ManufacturedTest("Collection") {
    final override fun performTest() {
        // DELIBERATE NO-OP
    }

    override val overallStatus: TestStatus
        get() {
            if (setupContext.overallStatus == TestStatus.SUBJECTIVE) return TestStatus.SUBJECTIVE
            if (! setupContext.overallStatus.isPassing()) return TestStatus.INCONCLUSIVE
            return TestStatus.PASS
        }

}

/**
 * A TestFactory generates an TestCollection of ManufacturedTest objects. As with stand-alone tests, TestFactories
 * will be instantiated at runtime, automatically calling populateProducts(). Any manufactured tests sitting in
 * the products ArrayList will then be run as if they were stand-alone tests. You may also create a subordinate
 * TestCollection for some of your tests if they need to be grouped together. Note that the "products" TestCollection
 * will be it's own subordinate section of the log and its own subdirctory within the results. Any further TestCollections you
 * add into "products" will be another subordinate log/directory within that one.
 */
abstract class TestFactory(
    val collectionName: String,
    val ownerName: String? = null,

    /**
     * Use this to provide a setup and cleanup for the manufactured tests produced by this factory.
     */
    val collectionOutfitter: Outfitter? = null) {
    val products = TestCollection(collectionName, ownerName)

    /**
     * Use this to instantiate manufactured tests and put them into the Products Collection
     */
    // TODO: Might not be able to deprecate this. Still need to have manufactured tests directly inserted.
    //  or might have to get rid of test factory and have the user declare collections?  Maybe collections
    //  are made based on the owner name in the tests???
    @Deprecated("Tests and collections now declare their owners. This function will be removed.", level = DeprecationLevel.ERROR)
    abstract fun populateProducts()

    init {
        products.outfitter = collectionOutfitter
        // TODO: Delete this line: populateProducts()
    }
}