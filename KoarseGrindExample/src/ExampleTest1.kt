import rockabilly.koarsegrind.Test
import rockabilly.koarsegrind.TestResult
import rockabilly.koarsegrind.TestStatus

class ExampleTest1:Test(
        "Sample Test Number One",
        "This is the detailed description for ExampleTest1.  Use this field to describe what the test does and what its pass criteria are. Commas, \tTabs, \rCarriage Returns, and \nLine Feed chars will be filtered out.",
        "ET-001",
        "Simple", "All", "Example"
) {
    override fun PerformTest() {
        Log.Info("Reality check")
        Log.Debug("Did it actually work???")
        Results.add(TestResult(TestStatus.Pass, "Whelp, 'Guess I'll just brute-force the dang thing as passing!"))
    }
}