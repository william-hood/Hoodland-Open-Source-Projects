/*
EXAMPLE - OBJECT & THROWABLE DESCRIPTIONS (some editors will hide all but the first line of this comment)

The "Descriptions" module allows you to generate on-the-fly test data that is
random, but conforming to specifications. Combined with a TestFactory it is
possible to generate as much test data as needed. "ManufacturedTestExample.kt"
also uses "Descriptions" but only for a single string field. This shows an
example of trying a variety of test cases on every possible field.

Also shown in the example is the "ThrowableDescription" class, which lets you
make a "rough" check against an exception or other throwable. This can prevent
unnecessary code changes in situations where the developers may change the
wording of an exceptions message, or slightly change its class name.

❗️ CAUTION ❗️
If you wanted to, you could exhaust every possible combination of test targets
against all of an objects fields. Just because you can, doesn't mean you should.
There may be cases where robustness is needed and you really do need to do this.
When that's not the case, you may generate thousands of test cases with little
or no real value to the test effort.

ℹ️ Note for Java Users: The "Descriptions" module has been left as unwrapped
   Kotlin as it was determined to be usable enough from Java without having
   its own Java wrapper. If you disagree, please let the author know.
 */

import hoodland.opensource.koarsegrind.java.ManufacturedTest;
import hoodland.opensource.descriptions.*;

public class PackageTrackerTest extends ManufacturedTest {
	PackageTracker testData;
	
    // ‼️ Sometimes developers make slight changes to an exception they throw --without telling QA.
    // ❗️ A ThrowableDescription will let you verify that an Exception's type name and message
    // 👇 meet certain criteria, as well as any causal exceptions (or lack thereof).
	ThrowableDescription expectedException = new ThrowableDescription("IllegalStateException", "", null);
	
	public PackageTrackerTest(
			String name,
			String detailedDescription,
			String identifier,
			PackageTracker testDataInput) {
		super(name, detailedDescription, "Descriptions Example", identifier);
		testData = testDataInput;
	}

	@Override
	public void performTest() {
        log().info("Show our test data in the log.");
        log().showObject(testData);

        Throwable thrownException = null;

        try {
            testData.verify();
        } catch (Throwable failure) {
            thrownException = failure;
        }

        assertion().shouldBeNull(thrownException, "The verify() function should not throw an exception.");

        if (thrownException != null) {
        	log().showThrowable(thrownException);
        	assertion().shouldBeTrue(expectedException.isMatch(thrownException), "Thrown exception should have " + expectedException.toString());
        }
	}
}
