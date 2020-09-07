// Copyright (c) 2020 William Arthur Hood
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights to
// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
// of the Software, and to permit persons to whom the Software is furnished
// to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.

package hoodland.opensource.koarsegrind

import hoodland.opensource.koarsegrind.*
import hoodland.opensource.koarsegrind.SUMMARY_FILE_NAME
import hoodland.opensource.koarsegrind.SUMMARY_TEXTFILE_NAME
import hoodland.opensource.memoir.Memoir
import hoodland.opensource.memoir.UNKNOWN
import hoodland.opensource.memoir.showThrowable
import hoodland.opensource.toolbox.MatrixFile
import hoodland.opensource.toolbox.QuantumTextFile
import hoodland.opensource.toolbox.StringParser
import hoodland.opensource.toolbox.stdout
import java.io.File
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.full.isSubclassOf


// Based on https://dzone.com/articles/get-all-classes-within-package
private val testLoader = Thread.currentThread().getContextClassLoader()
private val preclusions = ArrayList<Throwable>()

private val debugFile = File("${System.getProperty("user.home")}${File.separator}Documents${File.separator}Test Results${File.separator}KGDEBUG.html")
private val debuggingMemoir = Memoir("\uD83D\uDC1E DEBUG", null, debugFile.printWriter() )

object TestProgram {
    // This may have to be changed to take the same arguments as TestCollection
    fun run(name: String = UNKNOWN) {
        val rootCollection = TestCollection(name)
        val packages = testLoader.definedPackages
        packages.forEach {
            val resources = testLoader.getResources(it.name.replace('.', File.separatorChar)).asIterator()
            resources.forEach {
                recursiveIdentify(rootCollection, File(it.file))
            }
        }

        val rootLog = rootCollection.run(preclusions)
        createSummaryReport(rootCollection, rootLog)
        rootLog.conclude()
        debuggingMemoir.conclude()
    }

    fun createSummaryReport(rootCollection: TestCollection, memoir: Memoir = Memoir(forPlainText = stdout)) {
        val fullyQualifiedSummaryFileName = rootCollection.rootDirectory + File.separatorChar + SUMMARY_FILE_NAME
        memoir.info("Creating Test Suite Summary Report ($fullyQualifiedSummaryFileName)")
        var summaryReport = MatrixFile<String>("Categorization", "Test ID", "Name", "Description", "Status", "Reasons")

        try {
            // Try to append to an existing one
            summaryReport = MatrixFile.read(fullyQualifiedSummaryFileName, StringParser)
        } catch(dontCare: Throwable) {
            // Deliberate NO-OP
            // Leave the summaryReport as created above
        }

        rootCollection.gatherForReport(summaryReport)

        summaryReport.write(fullyQualifiedSummaryFileName)


        // Section below creates a single line file stating the overall status
        val fullyQualifiedSummaryTextFileName = rootCollection.rootDirectory + File.separatorChar + SUMMARY_TEXTFILE_NAME

        try {
            val textFile = QuantumTextFile(fullyQualifiedSummaryTextFileName)
            textFile.println(rootCollection.overallStatus.toString())
            textFile.flush()
            textFile.close() // Shouldn't need a getter because this is a val
        } catch (thisFailure: Throwable) {
            memoir.error("Did not successfully create the overall status text file $fullyQualifiedSummaryTextFileName")
            memoir.showThrowable(thisFailure)
        }
    }


    // TODO: Properly handle Jar files in the classpath
    // TODO: Make KG properly usable as a JAR package
    private fun recursiveIdentify(rootCollection: TestCollection, candidate: File) {
        debuggingMemoir.info("PATH ${candidate.absolutePath}")

        if (candidate.exists()) {
            val check = candidate.listFiles()
            check.forEach {
                if (it.isDirectory) {
                    if (!it.name.contains(".")) {
                        recursiveIdentify(rootCollection, it)
                    }
                } else if (it.name.toLowerCase().endsWith(".class")) {
                    var attemptName = it.invariantSeparatorsPath.replace('/', '.')
                    do {
                        attemptName = attemptName.substringAfter('.')
                        try {
                            debuggingMemoir.info("Attempting to load $attemptName")
                            val foundClass = testLoader.loadClass(attemptName.substring(0, attemptName.length - 6))
                            attemptName = "" // Prevent another loop iteration
                            debuggingMemoir.info("foundClass.kotlin.isSubclassOf(Test::class) == ${foundClass.kotlin.isSubclassOf(Test::class)}")
                            if (foundClass.kotlin.isSubclassOf(Test::class)) {
                                if (!(foundClass.kotlin.isSubclassOf(ManufacturedTest::class))) {
                                    val foundTestInstance: Test = foundClass.getDeclaredConstructor().newInstance() as Test
                                    rootCollection.add(foundTestInstance)
                                }
                            } else if (foundClass.kotlin.isSubclassOf(TestFactory::class)) {
                                val factory: TestFactory = foundClass.getDeclaredConstructor().newInstance() as TestFactory
                                rootCollection.add(factory.products)
                            }
                        } catch (materialFailure: InvocationTargetException) {
                            debuggingMemoir.showThrowable(materialFailure)
                            debuggingMemoir.debug("MATERIAL FAILURE")
                            preclusions.add(materialFailure)
                        } catch (dontCare: Throwable) {
                            debuggingMemoir.showThrowable(dontCare)
                            // DELIBERATE NO-OP
                            // Kotlin will hemorrhage exceptions during the process of identifying legitimate tests.
                            // Use the line below if there is need to identify failures that matter.
                            // addResult(getResultForFailureInCleanup(dontCare))
                        }
                    } while (attemptName.contains('.'))
                }
            }
        }
    }
}