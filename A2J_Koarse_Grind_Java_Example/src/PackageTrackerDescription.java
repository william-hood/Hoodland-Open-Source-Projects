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

import java.time.LocalDate;

import hoodland.opensource.descriptions.*;

public class PackageTrackerDescription extends ObjectDescription<PackageTracker> {
	DateFieldDescription sentDate = new DateFieldDescription(LocalDate.now(), DateLimitsDescriptionKt.getUnlimitedDate());
	DateFieldDescription expectedArrivalDate = new DateFieldDescription(LocalDate.now(), DateLimitsDescriptionKt.getUnlimitedDate());
	IntFieldDescription totalStops = new IntFieldDescription(3, new IntLimitsDescription(0, 100));
	IntFieldDescription expectedMaxStops = new IntFieldDescription(5, new IntLimitsDescription(1, 100));
	StringFieldDescription addStopName = new StringFieldDescription("Oceanside");

	@Override
	public PackageTracker getDescribedObject() throws InappropriateDescriptionException {

		if (sentDate.getDescribedValue() != null) {
			if (expectedMaxStops.getDescribedValue() != null) {
				if (totalStops.getDescribedValue() != null) {
					PackageTracker result = new PackageTracker(sentDate.getDescribedValue());
                    result.expectedArrivalDate = expectedArrivalDate.getDescribedValue();
                    result.expectedMaxStops = expectedMaxStops.getDescribedValue();
                    result.totalStops = totalStops.getDescribedValue();
                    return result;
				}
			}
		}

		throw new InappropriateDescriptionException("Neither sentDate nor expectedMaxStops nor totalStops may be null.");
	}

}
