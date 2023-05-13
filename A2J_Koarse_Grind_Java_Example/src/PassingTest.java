import hoodland.opensource.koarsegrind.java.*;

public class PassingTest extends Test {
	public PassingTest() {
		super(
	    "This Test Should Pass",
	    "Let's see how each of the test statuses look when logged. Here's what passing looks like...",
	    Main.STATUS_CATEGORY,
	    "TSJ-001"
	    );
	}
	
	@Override
	public void performTest() {
        log().info("Starting the test");
        assertion().shouldBeTrue(true, "I'll just brute-force this thing as passing!");
	}

}
