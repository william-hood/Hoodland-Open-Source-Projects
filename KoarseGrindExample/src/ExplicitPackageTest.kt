package com.mycompany.testing

import hoodland.opensource.koarsegrind.Test

class ExplicitPackageTest: Test(
        "Explicit Package Test",
        "This @#$%! thing hasn't been running tests that explicitly declare their package. Let's fix that.",
        "EP-001",
        "Simple", "All", "Example", "Package"
) {
    override fun performTest() {
        log.info("Reality check")
        log.debug("Did it actually work???")
        assert.shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!")
    }
}