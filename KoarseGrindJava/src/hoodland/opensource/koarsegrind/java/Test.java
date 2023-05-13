package hoodland.opensource.koarsegrind.java;

import hoodland.opensource.memoir.java.Memoir;

public abstract class Test extends hoodland.opensource.koarsegrind.Test {
    public Test(String name) {
        super (name, "", null, "");
    }

    public Test(String name, String detailedDescription) {
        super (name, detailedDescription, null, "");
    }

    public Test(String name, String detailedDescription, String categoryPath) {
        super (name, detailedDescription, categoryPath, "");
    }

    public Test(String name, String detailedDescription, String categoryPath, String identifier) {
        super (name, detailedDescription, categoryPath, identifier);
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
