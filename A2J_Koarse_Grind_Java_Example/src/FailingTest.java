import hoodland.opensource.koarsegrind.java.Test;

public class FailingTest extends Test {
	public FailingTest() {
		super(
	    "This Test Should Fail",
	    "Let's see how each of the test statuses look when logged. Here's what failing looks like...",
	    Main.STATUS_CATEGORY,
	    "TSJ-002"
	    );
	}
	
	@Override
	public void performTest() {
        log().info("Starting the test");
        assertion().shouldBeTrue(false, "This time I'll make it fail!");
	}
}
