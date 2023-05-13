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

public class PackageTracker {
	public LocalDate sent = null;
	public LocalDate expectedArrivalDate = null;
	public int expectedMaxStops = 0;
	public int totalStops = 0;
	public String _lastStop = null;
	
	public PackageTracker(LocalDate sent) {
		if (sent == null) {
			sent = LocalDate.now();
		}
		
		this.sent = sent;
	}
	
	public String getLastStop() {
		if (_lastStop != null) {
			return _lastStop;
		}
		
		return "(awaiting pickup)";
	}
	
	public void addStop(String stopName) {
		totalStops++;
		_lastStop = stopName;
	}
	
	private static final String ALPHANUMERICS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	public void verify() throws IllegalStateException {
        if (sent.compareTo(LocalDate.now()) > 0) {
            throw new IllegalStateException("Sent date is in the future.");
        }

        if (expectedArrivalDate != null) {
            if (expectedArrivalDate.compareTo(LocalDate.now()) < 0) {
                throw new IllegalStateException("Arrival date is in the past.");
            }
        }

        if (totalStops < 0) {
            throw new IllegalStateException("Total stops is negative.");
        }

        if (expectedMaxStops < 1) {
            throw new IllegalStateException("Expected Max Stops is less than one.");
        }

        if (_lastStop != null) {
            for (char thisChar: _lastStop.toCharArray()) {
                if (! ALPHANUMERICS.contains("" + thisChar)) {
                    throw new IllegalStateException("Last Stop is not alphanumeric.");
                }
            }
        }
	}
}
