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

import hoodland.opensource.descriptions.DateFieldTargets;
import hoodland.opensource.descriptions.ValueFieldTargets;
import hoodland.opensource.descriptions.StringFieldTargets;
import hoodland.opensource.koarsegrind.TestFactory;

import hoodland.opensource.toolbox.java.*;

public class DescriptionsExample extends TestFactory {

	@Override
	public void produceTests() {
		PackageTrackerDescription testDataGenerator = new PackageTrackerDescription();
		SubnameFactory subname = new SubnameFactory();
		
		for (ValueFieldTargets it: ValueFieldTargets.values()) {
            subname.advance();

            reset(testDataGenerator);
            testDataGenerator.totalStops.setTarget(it);

            try {
                PackageTracker testData = testDataGenerator.getDescribedObject();
                getProducedTests().add(new PackageTrackerTest(
                        "PackageTracker: Total Stops " + it.toString(),
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-01" + subname.getCurrentSubname(),
                        testData));
            } catch (Throwable dontCare) {
                // Silently ignore inappropriate test cases
            }

            reset(testDataGenerator);
            testDataGenerator.expectedMaxStops.setTarget(it);

            try {
            	PackageTracker testData = testDataGenerator.getDescribedObject();
                getProducedTests().add(new PackageTrackerTest(
                        "PackageTracker: Expected Max Stops " + it.toString(),
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-02" + subname.getCurrentSubname(),
                        testData));
            } catch (Throwable dontCare) {
                // Silently ignore inappropriate test cases
            }


        }

		for (DateFieldTargets it: DateFieldTargets.values()) {
            reset(testDataGenerator);
            testDataGenerator.sentDate.setTarget(it);

            try {
            	PackageTracker testData = testDataGenerator.getDescribedObject();
                getProducedTests().add(new PackageTrackerTest(
                        "PackageTracker: Sent Date " + it.toString(),
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-03" + subname.getCurrentSubname(),
                        testData));
            } catch (Throwable dontCare) {
                // Silently ignore inappropriate test cases
            }

            reset(testDataGenerator);
            testDataGenerator.expectedArrivalDate.setTarget(it);

            try {
            	PackageTracker testData = testDataGenerator.getDescribedObject();
                getProducedTests().add(new PackageTrackerTest(
                        "PackageTracker: Expected Arrival Date " + it.toString(),
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-04" + subname.getNextSubname(),
                        testData));
            } catch (Throwable dontCare) {
                // Silently ignore inappropriate test cases
            }
        }

        reset(testDataGenerator);
		for (StringFieldTargets currentTarget: StringFieldTargets.values()) {
            testDataGenerator.addStopName.setTarget(currentTarget);

            try {
            	String it = testDataGenerator.addStopName.getDescribedValue();
                if (it != null) {
                	PackageTracker testData = testDataGenerator.getDescribedObject();
                    testData.addStop(it);
                    getProducedTests().add(new PackageTrackerTest(
                            "PackageTracker: Last Stop " + currentTarget.toString(),
                            "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                            "DES-05" + subname.getNextSubname(),
                            testData));
                }
            } catch (Throwable dontCare) {
                // Silently ignore inappropriate test cases
            }
        }
	}

    private void reset(PackageTrackerDescription testDataGenerator) {
        testDataGenerator.sentDate.setTarget(DateFieldTargets.HAPPY_PATH);
        testDataGenerator.expectedArrivalDate.setTarget(DateFieldTargets.HAPPY_PATH);
        testDataGenerator.totalStops.setTarget(ValueFieldTargets.HAPPY_PATH);
        testDataGenerator.expectedMaxStops.setTarget(ValueFieldTargets.HAPPY_PATH);
    }

}
