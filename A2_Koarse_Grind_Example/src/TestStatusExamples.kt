package com.mycompany.testing

import hoodland.opensource.koarsegrind.Test

class Passing : Test(
    "This Test Should Pass",
    "Let's see how each of the test statuses look when logged. Here's what passing looks like...",
    identifier = "TS-001"
) {
    override fun performTest() {
        log.info("Starting the test")
        assert.shouldBeTrue(true, "I'll just brute-force this thing as passing!")
    }
}

class Failing : Test(
    "This Test Should Fail",
    "Let's see how each of the test statuses look when logged. Here's what failing looks like...",
    identifier = "TS-002"
) {
    override fun performTest() {
        log.info("Starting the test")
        assert.shouldBeTrue(false, "This time I'll make it fail!")
    }
}

class Inconclusive : Test(
    "This Test Should Be Inconclusive",
    "Let's see how each of the test statuses look when logged. Here's what inconclusive looks like...",
    identifier = "TS-003"
) {
    override fun performTest() {
        log.info("Making sure we're correctly configured...")
        require.shouldBeTrue(false, "If this fails, it wasn't configured correctly. Bummer!")
    }
}

class Subjective : Test(
    "This Test Should Be Subjective",
    "Let's see how each of the test statuses look when logged. Here's what subjective looks like...",
    identifier = "TS-004"
) {
    override fun performTest() {
        log.info("OK, here's a condition that could be open to interpretation...")
        consider.shouldBeTrue(true, "This really should work. If it doesn't, let some manager decide.")
        consider.shouldBeTrue(false, "This also really should work. If it doesn't, let a different manager decide.")
    }
}