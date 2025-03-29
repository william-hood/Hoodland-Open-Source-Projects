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

import hoodland.opensource.koarsegrind.java.ManufacturedTest;
import hoodland.opensource.boolog.java.StringHandler;
import hoodland.opensource.toolbox.java.Tools;

public class ManufacturedTestExample extends ManufacturedTest {
	static final String CATEGORY = "Manufactured Test Example";
	private String testDatum;
	
	public ManufacturedTestExample(
			String name,
			String detailedDescription,
			String identifier,
			String testData
			) {
		super(name, detailedDescription, CATEGORY, identifier);
		testDatum = testData;
	}
	
	@Override
	public void performTest() {
        log().info("Let's pretend we're sending this string into a database, REST call, or whatever else.");
        assertion().shouldBeTrue(true, "This string worked:" + StringHandler.treatAsCode(Tools.robustGetString(testDatum).toString()));
	}
}
