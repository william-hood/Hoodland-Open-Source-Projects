package hoodland.opensource.koarsegrind.java;

import hoodland.opensource.memoir.java.Memoir;

public abstract class Outfitter extends hoodland.opensource.koarsegrind.Outfitter {
    public Outfitter(String categoryPath) {
        super(categoryPath);
    }

    public Outfitter() {
        super(null);
    }

    public hoodland.opensource.memoir.java.Memoir log() {
        return new Memoir(getLog());
    }

    public hoodland.opensource.koarsegrind.Enforcer assertion() {
        return super.getAssert();
    }

    public hoodland.opensource.koarsegrind.Enforcer require() {
        return super.getRequire();
    }

    public hoodland.opensource.koarsegrind.Enforcer consider() {
        return super.getConsider();
    }
}
