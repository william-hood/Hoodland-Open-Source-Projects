import hoodland.opensource.koarsegrind.java.Test;

public class InconclusiveTest extends Test {
	public InconclusiveTest() {
		super(
	    "This Test Should Be Inconclusive",
	    "Let's see how each of the test statuses look when logged. Here's what inconclusive looks like...",
	    Main.STATUS_CATEGORY,
	    "TSJ-003"
	    );
	}
	
	@Override
	public void performTest() {
        log().info("Making sure we're correctly configured...");
        require().shouldBeTrue(false, "If this fails, it wasn't configured correctly. Bummer!");
	}
}
