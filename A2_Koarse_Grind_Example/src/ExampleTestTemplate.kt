package com.mycompany.testing

import hoodland.opensource.koarsegrind.Test

// To create a typical Koarse Grind test, extend the Test class. Your extension class
// should only have the deafault constructor with no parameters, but pass the following
// into the constructor for the parent class...
//
// name:                A brief, human-readable name for the test.
//
// detailedDescription: This will appear as the first line of each test's log output.
//                      Make it detailed enough that someone unfamiliar with the test
//                      can figure out what it does.
//
// identifier:          If you use a test tracking system it typically provides each
//                      test with a test case ID. That's what this is for.
//
// categories:          Put some strings that each describe a category this test might
//                      fall into. "HappyPath", "SmokeTests", "HighPriority" are some
//                      suggested examples. It is possible to tell the test program
//                      to only run tests that match a certain category.
//
// ‼️ IMPORTANT ‼️      Your test should only have the default constructor with no parameters.
//                      Otherwise it can not be automatically instantiated.
//
class ExampleTest: Test(
        "Koarse Grind Example Template",
        "Use this field to describe what the test does and what its pass criteria are. If someone other than you must evaluate the results or work with the source code, this description should help them understand what they are looking at. Commas, \tTabs, \rCarriage Returns, and \nLine Feed chars will be filtered out.",
        "ET-001",
        "Simple", "All", "Example"
) {
    // You don't have to have a setup() function if you don't need one.
    // If an assertion fails in setup, your test becomes inconclusive
    // and the performTest() function will not be run.
    override fun setup() {
        log.info("This is where we set everything up!")
        log.info("You can also verify proper configuration here.")
        //assert.shouldBeTrue(true, "Let's fail setup() and see what happens...")
        log.info("If an assertion fails in setup, the test will be inconclusive and performTest() will not run.")
    }

    // You also don't need to have a cleanup() function in your test.
    // If cleanup() exists it will ALWAYS be run, even if setup() failed.
    override fun cleanup() {
        log.info("Failing cleanup will NOT affect the test.")
        assert.shouldBeTrue(false, "Let's fail cleanup() and see what happens...")
    }

    // Every test must implement the performTest() function.
    // If any assertions, requirements, or considerations fail, the test can not pass.
    // If there are no assertions, requirements, or considerations the test is
    // automatically inconclusive.
    override fun performTest() {

        log.info("There are three kinds of conditional checks your test can perform.")
        log.info("Usually you will use a normal assertion, which makes the test fail if it doesn't pass.")
        log.info("There are also requirements, which make the test inconclusive upon failure.")
        log.info("Use requirements at the start of the test to verify that everything is set up and configured the way it should be.")

        // This is a "requirement" it asserts a condition that must pass for the test to be valid.
        // If a requirement fails, the test becomes INCONCLUSIVE. That means that it is neither
        // passing nor failing because a valid result either way can't be gotten.
        require.shouldBeEqual(true, true, "Everything should be set up & configured correctly.")

        // This is a normal "assertion." Assertions are the normal kind of conditional check that
        // your test should perform. If an assertion fails, the test fails.
        assert.shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!")

        // This is a "consideration". If a consideration fails, the test becomes subjective. That
        // means that evaluation by a human is needed to determine if the test should pass or fail.
        // You can also force a test to become subjective with the makeSubjective() function.
        consider.shouldBeTrue(true, "Whelp, 'Guess I'll just brute-force the dang thing as passing!")

        // 👇 READ THIS... 'Tis important.
        log.debug("Unlike test frameworks such as JUnit or NUnit, failing an assertion in Koarse Grind DOES NOT throw an exception and will NOT stop the test. If you want to exit the test early, you must explicitly do so with a return statement.")

    }
}