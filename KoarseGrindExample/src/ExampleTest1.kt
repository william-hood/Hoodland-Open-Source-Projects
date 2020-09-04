import hoodland.opensource.descriptions.StringFieldDescription
import hoodland.opensource.descriptions.StringFieldTargets
import hoodland.opensource.koarsegrind.ManufacturedTest
import hoodland.opensource.koarsegrind.Test
import hoodland.opensource.koarsegrind.TestFactory
import hoodland.opensource.toolbox.SubnameFactory

class ExampleTest1:Test(
        "Sample Test Number One",
        "This is the detailed description for ExampleTest1.  Use this field to describe what the test does and what its pass criteria are. Commas, \tTabs, \rCarriage Returns, and \nLine Feed chars will be filtered out.",
        "ET-001",
        "Simple", "All", "Example"
) {
    override fun performTest() {
        log.info("Reality check")
        log.debug("Did it actually work???")
        assert.shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!")
    }
}

class ExampleTest3:Test(
        "Sample Test Number Three",
        "This is the detailed description for ExampleTest3.  Use this field to describe what the test does and what its pass criteria are. Commas, \tTabs, \rCarriage Returns, and \nLine Feed chars will be filtered out.",
        "ET-001",
        "Simple", "All", "Example"
) {
    override fun performTest() {
        log.info("Reality check")
        log.debug("Did it actually work???")
        assert.shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!")
        assert.shouldBeEqual(true, false, "Actually, this should make it fail.")
    }
}

class ExampleTest3B:Test(
        "Sample Test Number Three (B)",
        "This is the detailed description for ExampleTest3.  Use this field to describe what the test does and what its pass criteria are. Commas, \tTabs, \rCarriage Returns, and \nLine Feed chars will be filtered out.",
        "ET-001",
        "Simple", "All", "Example"
) {
    override fun performTest() {
        log.info("Reality check")
        log.debug("Did it actually work???")
        assert.shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!")
        assert.shouldBeEqual(true, false, "Actually, this should make it fail.")
    }
}
/*
class ManufacturedTestExample(
        name: String,
        detailedDescription: String,
        identifier: String,
        val testData: String?)
    : ManufacturedTest(name, detailedDescription, identifier,
        "Manufactured", "Descriptions", "All", "Example") {
    override fun performTest() {
        log.info("Let's pretend we're sending this string into a database, REST call, or whatever else.")
        assert.shouldBeTrue(true, "This string worked: $testData")
    }
}

class TestFactoryExample: TestFactory() {
    override fun populateProducts() {
        val subname = SubnameFactory()
        val testDataGenerator = StringFieldDescription("The rain in Spain stays mainly on the plain.")

        StringFieldTargets.values().forEach {
            if (it != StringFieldTargets.DEFAULT) {
                testDataGenerator.target = it
                products.add(ManufacturedTestExample(
                        "Show messed up string: ${it.toString()}",
                        "This is just an example of using the Descriptions module to generate test data. This particular case modifies a basis string to meet the test data criterion. In this case: ${it.toString()}",
                        subname.nextSubname,
                        testDataGenerator.describedValue))
            }
        }
    }

}
*/