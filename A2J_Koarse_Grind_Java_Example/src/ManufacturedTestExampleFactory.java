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

import hoodland.opensource.koarsegrind.TestFactory;
import hoodland.opensource.descriptions.*;
import hoodland.opensource.toolbox.java.*;

public class ManufacturedTestExampleFactory extends TestFactory {

	@Override
	public void produceTests() {
		SubnameFactory subname = new SubnameFactory();
		StringFieldDescription testDataGenerator = new StringFieldDescription("The rain in Spain stays mainly on the plain.");
		
		for(StringFieldTargets it : StringFieldTargets.values()) {
            if (it != StringFieldTargets.DEFAULT) {
                testDataGenerator.setTarget(it);
                getProducedTests().add(new ManufacturedTestExample(
                        "Show messed up string: " + it.toString(),
                        "This is just an example of using the Descriptions module to generate test data. This particular case modifies a basis string to meet the test data criterion. In this case: " + it.toString(),
                        "ETF-01" + subname.getNextSubname(),
                        testDataGenerator.getDescribedValue()));
            }
		}
	}

}
