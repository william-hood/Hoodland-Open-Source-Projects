##### Release 2.2

This release is a minor bugfix release for the Kotlin side.
On the Java side, there is now a JUnit integrated version of the Java wrapper for Memoir, meaning that
Memoir now plays nice with JUnit. An example module will be included in the IntelliJ project. An
example of using Memoir with JUnit in Eclipse will be included as pat of the release.

Testing was done against Linux Mint 21.1, macOS Monterey, and Windows 11.

---

# Hoodland Open Source Projects

These projects are the "spiritual successors" to software I've developed over
the course of my career as an SDET. In June of 2020, having grown frustrated
with the shortcomings of both Java and C#, I undertook a complete rewrite of
my legacy projects in Kotlin. Each of these projects, or their predecessors,
have served me well at work. I hope you will find them useful.

- **Memoir** - A rich logging system that outputs directly to HTML. This started
many years ago using "ascii art" to improve readability of the text-based output
of programs I created for several previous clients. Eventually it made more
sense to output to HTML. Class objects, HTTP Transactions, JVM Exceptions and
embedded log segments are cleanly presented in "manager friendly" click-to-expand
fashion to a single, self-contained HTML file. In addition to being the integrated
logging system for Koarse Grind (see below) a wrapper class is provided to make
Memoir easy to use with JUnit.
- **ChangeScan** - A program that can tell you what has changed on a computer's
file system. It is the spiritual successor of a JavaScript program I created for
a client to test their installer. Scan all or part of the filesystem; perform
your installation (or otherwise cause changes); scan it again and it tells you
what changed.
- **Toolbox** - An assorted miscellany of tools and toys. Including but not limited to:
    - Matrixfile - Easily creates and manages files of CSV data (you may use tabs or
    other delimiters as you wish).
    - QuantumTextFile - Readies a text output file but does not actually create it
    until the first write operation takes place.
    - StatusCodeDescription - Useful for matching HTTP Status Code values to an
    appropriate text description, as well as determining if the code if passing or valid.
    - SubnameFactory - Do you need to programmatically generate several similar names?
    TestA, TestB, TestC and so on? This makes it easy.
    - ZipFileCreator - ...is exactly that. It creates a compressed Zip file containing all files in the directory you name, recursing down any subdirectories it finds.
- **Koarse Grind** - Numerous employers have asked me to create a test automation
framework from scratch. After writing several similar frameworks for various
clients I decided to create a "reference implementation" ready for use as-is,
and ready to port to a client's tech-stack of choice. The original "Coarse Grind"
was written in Java, but later ported to C#. The Kotlin rewrite is primarily
based on the C# version, and makes use of Memoir as its integrated log system.
Here's what sets it apart from other test frameworks:
    - Not for Unit Tests – As the name suggests it’s intended for larger-grained tests one might do against a program or service that’s already installed and configured; though it can certainly do unit tests if that’s what you want.
    - Rich Logging – Automated tests are often run in the middle of the night. If something fails, not only does the logging need to be verbose it has to be easy to read. Originally I used ascii-art in the logs to make failures stand out. Koarse Grind now uses an HTML-based logging system which I’ve broken out into a separate project called “Memoir.” It visualizes common object types, HTTP Transactions, Exceptions, and embedded log segments with a click-to-expand interface.
    - No Annotations – I deliberately chose to NOT implement any form of “*Unit” where tests, setup, and cleanup are annotated functions. Standard aspects of object orientated programming are suitable for that purpose.
    - Test Results are NOT Exceptions – I didn’t want Koarse Grind to be like JUnit/NUnit where every failure is a thrown exception and unless you trap them the test is over; --which leads to the next point…
    - Multiple Points of Failure – Employers always asked to not end the test if one criterion failed, which meant try/catch blocks everywhere since failures were exceptions. Koarse Grind does not end the test early unless the programmer explicitly tells it to.
    - No Separate Runner Program – JUnit and NUnit both compile the tests to libraries requiring a separate runner program. This can be convenient for running in the IDE itself, or with a third party GUI, but it often proved difficult running from the command line. Koarse Grind tests compile as a runnable program, so running them from a script is already solved. (I’ve had GUI’s to run tests in older versions, but have not yet implemented that in the Kotlin version.)
    - Artifacts Folder – Every test has a folder for its own artifacts. This will contain at least the section of the log unique to that test. It can also hold screen shots, serialized data files, or other artifact files the test produces.
    - Rich Test-Object Description – A special “descriptions” module lets you describe how a candidate object might look and provides for ways to test the typical border conditions and edge cases.
    - Unlike legacy versions of Coarse Grind, the top-level test program will identify your tests, instantiate one of each, and run them. Unless you’re generating a set of tests programmatically (a special TestFactory class provides for this) normal tests do not need to be instantiated or put into a container structure. Also, tests now have one-and-only-one category which is directly reflected by the folder hierarchy and the organization of the logs.

## Usage & Known Issues
- Clone the repository and open the root directory in IntelliJ IDEA CE.
- For JUnitMemoir to compile, hover over any import statement for org.junit.jupiter. Do not download JUnit4; instead select "More Actions" and download a version of JUnit 5 ("Jupiter", 5.8.1 as of this writing).
- There is an example project for the Memoir logging system when used as a stand-alone module.
- A second example program exists for Koarse Grind, which uses Memoir for its logging.
- There is also a partial start on a set of Koarse Grind tests for the other Hoodland projects which also serves as an example.
- The HTML-based output logs will appear in a folder titled "Test Results" off of your Documents directory. The root output folder can be changed from $/Documents/TestResults to the directory of your choice by setting defaultParentFolder before calling TestProgram.run().
- Known Koarse Grind Issue: If a module, or its directory, has a space in its name any tests it contains will not be found and will not run. Don't put spaces in module names. Use an underscore instead.
- The Kotlin code and the overall project is intended for use with IntelliJ IDEA CE. Eclipse is supported for using the Java wrappers in their compiled binary form (KoarseGrindJava.jar, MemoirJava.jar, JUnitMemoir.jar and ToolboxJava.jar). Feel free to adapt the code to any other IDE or Editor you're comfortable with, with the understanding that I don't officially support such use.
- All projects are "Kotlin First". Use from Java is supported by way of the Java wrapper Jar files, and only using the Kotlin bits when a Java wrapping is not available.

## Released under the terms of the MIT License
© 2020, 2021, 2022, 2023 William Hood

*Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished
to do so, subject to the following conditions:*

*The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.*

*THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.*


## Contact Information
https://www.linkedin.com/in/william-a-hood-sdet-pdx/

william.arthur.hood@gmail.com
