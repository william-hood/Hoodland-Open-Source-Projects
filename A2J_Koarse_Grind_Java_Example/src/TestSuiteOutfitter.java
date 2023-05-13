import hoodland.opensource.koarsegrind.java.Outfitter;

public class TestSuiteOutfitter extends Outfitter {
	@Override
    public void setup() {
        assertion().shouldBeTrue(true, "Suite-level setup ran!");
    }

	@Override
    public void cleanup() {
        assertion().shouldBeTrue(true, "Suite-level cleanup ran!");
    }
}
