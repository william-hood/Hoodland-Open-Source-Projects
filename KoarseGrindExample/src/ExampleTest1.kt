import rockabilly.koarsegrind.Test

class ExampleTest1:Test(
        "Sample Test Number One",
        "This is the detailed description for ExampleTest1.  Use this field to describe what the test does and what its pass criteria are. Commas, \tTabs, \rCarriage Returns, and \nLine Feed chars will be filtered out.",
        "ET-001",
        "Simple", "All", "Example"
) {
    override fun performTest() {
        Log.info("Reality check")
        Log.debug("Did it actually work???")
        assert.shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!")
    }
}

class ExampleTest3:Test(
        "Sample Test Number Three",
        "This is the detailed description for ExampleTest3.  Use this field to describe what the test does and what its pass criteria are. Commas, \tTabs, \rCarriage Returns, and \nLine Feed chars will be filtered out.",
        "ET-001",
        "Simple", "All", "Example"
) {
    override fun performTest() {
        Log.info("Reality check")
        Log.debug("Did it actually work???")
        assert.shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!")
        assert.shouldBeEqual(true, false, "Actually, this should make it fail.")
    }
}