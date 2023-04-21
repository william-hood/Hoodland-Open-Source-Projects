/*
EXAMPLE - OBJECT & THROWABLE DESCRIPTIONS (some editors will hide all but the first line of this comment)

The "Descriptions" module allows you to generate on-the-fly test data that is
random, but conforming to specifications. Combined with a TestFactory it is
possible to generate as much test data as needed. "ManufacturedTestExample.kt"
also uses "Descriptions" but only for a single string field. This shows an
example of trying a variety of test cases on every possible field.

Also shown in the example is the "ThrowableDescription" class, which lets you
make a "rough" check against an exception or other throwable. This can prevent
unnecessary code changes in situations where the developers may change the
wording of an exceptions message, or slightly change its class name.

❗️ CAUTION ❗️
If you wanted to, you could exhaust every possible combination of test targets
against all of an objects fields. Just because you can, doesn't mean you should.
There may be cases where robustness is needed and you really do need to do this.
When that's not the case, you may generate thousands of test cases with little
or no real value to the test effort.
 */

package com.mycompany.testing

import hoodland.opensource.descriptions.*
import hoodland.opensource.koarsegrind.ManufacturedTest
import hoodland.opensource.koarsegrind.TestFactory
import hoodland.opensource.toolbox.SubnameFactory
import java.lang.IllegalStateException
import java.time.LocalDate

// This is the "generic" version of the test. Since it extends ManufacturedTest,
// Koarse Grind will NOT attempt to instantiate and run it.
class PackageTrackerTest(
        name: String,
        detailedDescription: String,
        identifier: String,
        val testData: PackageTracker) : ManufacturedTest(name, detailedDescription, "Descriptions Example", identifier) {

    // ‼️ Sometimes developers make slight changes to an exception they throw --without telling QA.
    // ❗️ A ThrowableDescription will let you verify that an Exception's type name and message
    // 👇 meet certain criteria, as well as any causal exceptions (or lack thereof).
    val expectedException = ThrowableDescription("IllegalStateException")

    override fun performTest() {
        log.info("Show our test data in the log.")
        log.showObject(testData)

        var thrownException: Throwable? = null

        try {
            testData.verify()
        } catch (failure: Throwable) {
            thrownException = failure
        }

        assert.shouldBeNull(thrownException, "The verify() function should not throw an exception.")

        thrownException?.let {
            log.showThrowable(it)
            assert.shouldBeTrue(expectedException.isMatch(it), "Thrown exception should have ${expectedException.toString()}")
        }
    }
}

// Use a TestFactory to instantiate your ManufacturedTests, each with the
// parameters you want, and add them to the "products" collection. Tests
// dropped in "products" will run like any other test, though they will be
// in their own click-to-expand subsection in the "All Tests.html" log file.
// Additionally, their artifact folders will be similarly grouped together
// in their own folder (using the collectionName parameter to the
// TestFactory constructor).
//
// ‼️ IMPORTANT ‼️ Your TestFactory should only have the default constructor with no parameters.
//
// Also, be careful about generating thousands of ancillary tests that no one cares about.
// It is possible to exhaust every combination of test data, but that may not be of much value.
//
class DescriptionsExample: TestFactory() {
    override fun produceTests() {
        val testDataGenerator = PackageTrackerDescription()
        val subname = SubnameFactory()

        ValueFieldTargets.values().forEach {
            subname.advance()

            reset(testDataGenerator)
            testDataGenerator.totalStops.target = it

            try {
                val testData = testDataGenerator.describedObject
                producedTests.add(PackageTrackerTest(
                        "PackageTracker: Total Stops ${it.toString()}",
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-01${subname.currentSubname}",
                        testData))
            } catch (dontCare: Throwable) {
                // Silently ignore inappropriate test cases
            }

            reset(testDataGenerator)
            testDataGenerator.expectedMaxStops.target = it

            try {
                val testData = testDataGenerator.describedObject
                producedTests.add(PackageTrackerTest(
                        "PackageTracker: Expected Max Stops ${it.toString()}",
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-02${subname.currentSubname}",
                        testData))
            } catch (dontCare: Throwable) {
                // Silently ignore inappropriate test cases
            }


        }

        DateFieldTargets.values().forEach {
            reset(testDataGenerator)
            testDataGenerator.sentDate.target = it

            try {
                val testData = testDataGenerator.describedObject
                producedTests.add(PackageTrackerTest(
                        "PackageTracker: Sent Date ${it.toString()}",
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-03${subname.currentSubname}",
                        testData))
            } catch (dontCare: Throwable) {
                // Silently ignore inappropriate test cases
            }

            reset(testDataGenerator)
            testDataGenerator.expectedArrivalDate.target = it

            try {
                val testData = testDataGenerator.describedObject
                producedTests.add(PackageTrackerTest(
                        "PackageTracker: Expected Arrival Date ${it.toString()}",
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-04${subname.nextSubname}",
                        testData))
            } catch (dontCare: Throwable) {
                // Silently ignore inappropriate test cases
            }
        }

        reset(testDataGenerator)
        StringFieldTargets.values().forEach {currentTarget ->
            testDataGenerator.addStopName.target = currentTarget

            try {
                testDataGenerator.addStopName.describedValue?.let {
                    val testData = testDataGenerator.describedObject
                    testData.addStop(it)
                    producedTests.add(PackageTrackerTest(
                            "PackageTracker: Last Stop ${currentTarget.toString()}",
                            "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                            "DES-05${subname.nextSubname}",
                            testData))
                }
            } catch (dontCare: Throwable) {
                // Silently ignore inappropriate test cases
            }
        }
    }

    fun reset(testDataGenerator: PackageTrackerDescription) {
        testDataGenerator.sentDate.target = DateFieldTargets.HAPPY_PATH
        testDataGenerator.expectedArrivalDate.target = DateFieldTargets.HAPPY_PATH
        testDataGenerator.totalStops.target = ValueFieldTargets.HAPPY_PATH
        testDataGenerator.expectedMaxStops.target = ValueFieldTargets.HAPPY_PATH
    }
}

class PackageTracker(val sent: LocalDate = LocalDate.now()) {
    var expectedArrivalDate: LocalDate? = null
    var expectedMaxStops = 0
    var totalStops = 0
    private var _lastStop: String? = null

    val lastStop: String
        get() {
            _lastStop?.let {
                return it
            }

            return "(awaiting pickup)"
        }

    fun addStop(stopName: String) {
        totalStops++
        _lastStop = stopName
    }

    fun verify() {
        if (sent.compareTo(LocalDate.now()) > 0) {
            throw IllegalStateException("Sent date is in the future.")
        }

        expectedArrivalDate?.let {
            if (it.compareTo(LocalDate.now()) < 0) {
                throw IllegalStateException("Arrival date is in the past.")
            }
        }

        if (totalStops < 0) {
            throw IllegalStateException("Total stops is negative.")
        }

        if (expectedMaxStops < 1) {
            throw IllegalStateException("Expected Max Stops is less than one.")
        }

        _lastStop?.let {
            for (thisChar in lastStop) {
                if (thisChar !in 'A'..'Z' && thisChar !in 'a'..'z' && thisChar !in '0'..'9') {
                    throw IllegalStateException("Last Stop is not alphanumeric.")
                }
            }
        }
    }
}

class PackageTrackerDescription: ObjectDescription<PackageTracker>() {
    val sentDate = DateFieldDescription(LocalDate.now(), UnlimitedDate)
    val expectedArrivalDate = DateFieldDescription(LocalDate.now(), UnlimitedDate)
    val totalStops = IntFieldDescription(3, IntLimitsDescription(0, 100))
    val expectedMaxStops = IntFieldDescription(5, IntLimitsDescription(1, 100))
    val addStopName = StringFieldDescription("Oceanside")

    override val describedObject: PackageTracker
        get() {
            sentDate.describedValue?.let { sentDate ->
                expectedMaxStops.describedValue?.let {maxStops ->
                    totalStops.describedValue?.let { stops ->
                        val result = PackageTracker(sentDate)
                        result.expectedArrivalDate = expectedArrivalDate.describedValue
                        result.expectedMaxStops = maxStops
                        result.totalStops = stops
                        return result
                    }
                }
            }

            throw InappropriateDescriptionException("Neither sentDate nor expectedMaxStops nor totalStops may be null.")
        }
}