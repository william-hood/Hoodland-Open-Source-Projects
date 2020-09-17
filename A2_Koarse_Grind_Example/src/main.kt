/*

READ ME: KOARSE GRIND EXAMPLE (some editors will hide all but the first line of this comment)

This example is designed for use with IntelliJ IDEA CE. To run it, right-click the green
triangle to the left of the main() function and select the first item at the top
("Run MainKt"). A folder "Test Results" will be created in your documents folder. The
test program output will be created in a new time-stamped subdirectory from there.
Within the output directory will be an "All Tests.html" file, a CSV spreadsheet of
the test results, and PASS/FAIL prefixed artifact directories for each test that ran.

HISTORY: Years ago, after creating several variations of the same test framework for
several different companies, I decided to create a “reference version” of what I
typically do. If a new employer (again) asked that I create a brand new test framework
for them, I’d have something to start with. Initially written in Java and later ported
to C#, I called the result “Coarse Grind,” and while it’s evolved over the years I’ve
been able to put it into action at two jobs. A few of my colleagues also used it, at
least one of whom deployed it at their next job as well. While I didn’t use it in my
most recent role, the fact that I’d written my own test framework carried weight when
I interviewed.

I’ve decided to get Coarse Grind into good enough shape that more than just me and my
friends might want to use it. After starting to improve the C# version, I got frustrated
that both C# and Java would force me to put up with their own set of problems. In June,
2020, I decided to drop C# and port all my personal projects to Kotlin.

Koarse Grind (now with a ‘K’) is at least in pre-beta condition and ready for feedback.
Here’s the core differences that set it apart from most other frameworks:

* Not for Unit Tests – As the name suggests it’s intended for larger-grained tests one
  might do against a program or service that’s already installed and configured; though
  it can certainly do unit tests if that’s what you want.
* Rich Logging – Automated tests are often run in the middle of the night. If something
  fails, not only does the logging need to be verbose it has to be easy to read.
  Originally I used ascii-art in the logs to make failures stand out. Koarse Grind now
  uses an HTML-based logging system which I’ve broken out into a separate project called
  “Memoir.” It visualizes common object types, HTTP Transactions, Exceptions, and embedded
  log segments with a click-to-expand interface.
* No Annotations – I deliberately chose to NOT implement any form of “*Unit” where tests,
  setup, and cleanup are annotated functions. Standard aspects of object orientated
  programming are suitable for that.
* Test Results are NOT Exceptions – I didn’t want Koarse Grind to be like JUnit/NUnit where
  every failure is a thrown exception and unless you trap them the test is over; --which
  leads to the next point…
* Multiple Points of Failure – Employers always asked to not end the test if one criterion
  failed, which meant try/catch blocks everywhere since failures were exceptions. Koarse
  Grind does not end the test early unless the programmer explicitly tells it to.
- No Separate Runner Program – JUnit and NUnit both compile the tests to libraries requiring
  a separate runner program. This can be convenient for running in the IDE itself, or with a
  third party GUI, but it often proved difficult running from the command line. Koarse Grind
  tests compile as a runnable program, so running them from a script is already solved.
  (I’ve had GUI’s to run tests in older versions, but have not yet implemented that in the
  Kotlin version.)
* Artifacts Folder – Every test has a folder for its own artifacts. This will contain at
  least the section of the log unique to that test. It can also hold screen shots, serialized
  data files, or other artifact files the test produces.
* Rich Test-Object Description – A special “descriptions” module lets you describe how a
  candidate object might look and provides for ways to test the typical border conditions
  and edge cases.
* New to this version the top-level test program will identify your tests, instantiate one
  of each, and run them. Unless you’re generating a set of tests programmatically (a special
  TestFactory class provides for this) normal tests do not need to be instantiated or put into
  a container structure.

I’d greatly appreciate for some of my fellow Test Automation Professionals to have a look at
Koarse Grind (and it’s “Memoir” logging system), play around with the examples, and offer me
some honest feedback.

The source code is MIT Licensed and developed with InteliJ IDEA CE.

--Bill Hood

https://www.linkedin.com/in/william-a-hood-sdet-pdx/

 */

// ‼️ IMPORTANT ‼️ If a module, or its directory, contains a space tests in it will not be run.
// So don't do that!

package com.mycompany.testing

import hoodland.opensource.koarsegrind.TestProgram

// First thing's first. Below is all you need for a main() function
// to kick off your tests.
fun main(args: Array<String>) {
    TestProgram.run("Koarse Grind Demo", args)
}

// TODO: More/Better examples for Descriptions
// TODO: FailureDescription Example

// You can put a test in the same file as main() if you want.
// Start with the file "ExampleTestTemplate.kt" for an example of a "normal" test.
// Take a look at "A3 Hoodland Projects Test Suite" for an example of an actual test project.