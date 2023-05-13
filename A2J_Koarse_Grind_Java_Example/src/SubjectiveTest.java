import hoodland.opensource.koarsegrind.java.Test;

public class SubjectiveTest extends Test {
	public SubjectiveTest() {
		super(
	    "This Test Should Be Subjective",
	    "Let's see how each of the test statuses look when logged. Here's what subjective looks like...",
	    Main.STATUS_CATEGORY,
	    "TSJ-004"
	    );
	}
	
	@Override
	public void performTest() {
        log().info("OK, here's a condition that could be open to interpretation...");
        consider().shouldBeTrue(true, "This really should work. If it doesn't, let some manager decide.");
        consider().shouldBeTrue(false, "This also really should work. If it doesn't, let a different manager decide.");
	}
}
