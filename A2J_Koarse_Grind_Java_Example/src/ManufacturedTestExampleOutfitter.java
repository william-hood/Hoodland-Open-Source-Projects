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

import hoodland.opensource.koarsegrind.java.Outfitter;

public class ManufacturedTestExampleOutfitter extends Outfitter {
	public ManufacturedTestExampleOutfitter() {
		super(ManufacturedTestExample.CATEGORY);
	}
	
	@Override
    public void setup() {
        assertion().shouldBeTrue(true, "Category-level setup ran!");
    }

	@Override
    public void cleanup() {
        assertion().shouldBeTrue(true, "Category-level cleanup ran!");
    }
}
