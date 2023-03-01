/*
EXAMPLE - TEST FACTORY & MANUFACTURED TEST

Sometimes it makes more sense to programmatically instantiate several similar tests
rather than create several individual Koarse Grind tests. Any class that derives
from the TestFactory class will also be instantiated when your test program is run.

A TestFactory has a TestCollection of it's own that you populate with tests you want
to have run. These tests must extend the ManufacturedTest class so Koarse Grind
knows to not attempt to instantiate them automatically.

This example uses the "Descriptions" module to attempt several variations on the
same base string value. Unlike a normal Koarse Grind test, Manufactured Tests
may have as many parameters as you like in their constructor.
 */
package com.mycompany.testing

import hoodland.opensource.descriptions.StringFieldDescription
import hoodland.opensource.descriptions.StringFieldTargets
import hoodland.opensource.koarsegrind.ManufacturedTest
import hoodland.opensource.koarsegrind.Outfitter
import hoodland.opensource.koarsegrind.TestFactory
import hoodland.opensource.memoir.treatAsCode
import hoodland.opensource.toolbox.SubnameFactory

private const val CATEGORY = "Manufactured Test Example"

// This is the "generic" version of the test. Since it extends ManufacturedTest,
// Koarse Grind will NOT attempt to instantiate and run it.
class ManufacturedTestExample(
        name: String,
        detailedDescription: String,
        identifier: String,
        val testData: String?)
    : ManufacturedTest(name, detailedDescription, CATEGORY, identifier) {
    override fun performTest() {
        log.info("Let's pretend we're sending this string into a database, REST call, or whatever else.")
        assert.shouldBeTrue(true, "This string worked:${treatAsCode(testData.toString())}")
    }
}

// Use a TestFactory to instantiate your ManufacturedTests, each with the
// parameters you want, and add them to the "products" collection. Tests
// dropped in "products" will run like any other test, though they will be
// in their own click-to-expand subsection in the "All Tests.html" log file.
// Additionally, their artifact folders will be similarly grouped together
// in their own folder (using the collectionName parameter to the
// TestFactory constructor).
//
// ‼️ IMPORTANT ‼️ Your TestFactory should only have the default constructor with no parameters.
//

//TestFactory("Test Factory and Manufactured Test Example", ExampleOutfitter)
class TestFactoryExample: TestFactory() {
    override fun produceTests() {
        val subname = SubnameFactory()
        val testDataGenerator = StringFieldDescription("The rain in Spain stays mainly on the plain.")

        StringFieldTargets.values().forEach {
            if (it != StringFieldTargets.DEFAULT) {
                testDataGenerator.target = it
                producedTests.add(ManufacturedTestExample(
                        "Show messed up string: ${it.toString()}",
                        "This is just an example of using the Descriptions module to generate test data. This particular case modifies a basis string to meet the test data criterion. In this case: ${it.toString()}",
                        "ETF-01${subname.nextSubname}",
                        testDataGenerator.describedValue))
            }
        }
    }
}

// If the tests created by your factory require a setup and/or cleanup, pass an Outfitter
// object to the TestFactory constructor.
class ExampleOutfitter : Outfitter(CATEGORY) {
    override fun setup() {
        assert.shouldBeTrue(true, "Collection-level setup ran!")
    }

    override fun cleanup() {
        assert.shouldBeTrue(true, "Collection-level cleanup ran!")
    }
}