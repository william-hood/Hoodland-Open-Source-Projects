import hoodland.opensource.koarsegrind.java.*;

//To create a typical Koarse Grind test, extend the Test class. Your extension class
//should only have the default constructor with no parameters, but pass the following
//into the constructor for the parent class...
//
//name:                A brief, human-readable name for the test.
//
//detailedDescription: This will appear as the first line of each test's log output.
//                   Make it detailed enough that someone unfamiliar with the test
//                   can figure out what it does.
//
//categoryPath:        Tests are organized in a hierarchy of categories. This will be
//                   reflected in both the log file and the hierarchy of test result
//                   folders. Specify the test's fully qualified path, with path
//                   names separated by pipe ("|") characters. The test will be at
//                   the top level if this parameter is left null.
//
//identifier:          If you use a test tracking system it typically provides each
//                   test with a test case ID. That's what this is for.
//
//‼️ IMPORTANT ‼️      Your test should only have the default constructor with no parameters.
//                   Otherwise it can not be automatically instantiated.
//
public class ExampleTest extends Test {

	public ExampleTest() {
		super(
				"Koarse Grind Example Template",
				"Use this field to describe what the test does and what its pass criteria are. If someone other than you must evaluate the results or work with the source code, this description should help them understand what they are looking at. Commas, \tTabs, \rCarriage Returns, and \nLine Feed chars will be filtered out.",
				null,
				"ETJ-001"
				);
	}

	@Override
	public void setup() {
		log().info("This is where we set everything up!");
        log().info("You can also verify proper configuration here.");
        log().info("If an assertion fails in setup, the test will be inconclusive and performTest() will not run.");
	}

	@Override
	public void performTest() {
		// The Kotlin versions of KG & Boolog may need to at least be aware of their java counterparts.
        log().info("There are three kinds of conditional checks your test can perform.");
        log().info("Usually you will use a normal assertion, which makes the test fail if it doesn't pass.");
        log().info("There are also requirements, which make the test inconclusive upon failure.");
        log().info("Use requirements at the start of the test to verify that everything is set up and configured the way it should be.");

        // This is a "requirement" it asserts a condition that must pass for the test to be valid.
        // If a requirement fails, the test becomes INCONCLUSIVE. That means that it is neither
        // passing nor failing because a valid result either way can't be gotten.
        require().shouldBeEqual(true, true, "Everything should be set up & configured correctly.");

        // This is a normal "assertion." Assertions are the normal kind of conditional check that
        // your test should perform. If an assertion fails, the test fails.
        assertion().shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!");

        // This is a "consideration". If a consideration fails, the test becomes subjective. That
        // means that evaluation by a human is needed to determine if the test should pass or fail.
        // You can also force a test to become subjective with the makeSubjective() function.
        consider().shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!");

        // 👇 READ THIS... 'Tis important.
        log().debug("Unlike test frameworks such as JUnit or NUnit, failing an assertion in Koarse Grind DOES NOT throw an exception and will NOT stop the test. If you want to exit the test early, you must explicitly do so with a return statement.");
        
		//assertion().shouldBeEqual("Apple", 0, "Comparing an apple to an 0range.");
	}
	
	@Override
	public void cleanup() {
        log().info("Failing cleanup will NOT affect the test.");
        assertion().shouldBeTrue(false, "Let's fail cleanup() and see what happens...");
	}
}
