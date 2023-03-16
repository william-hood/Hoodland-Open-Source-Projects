// Copyright (c) 2020, 2022, 2023 William Arthur Hood
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

import hoodland.opensource.memoir.Memoir
import hoodland.opensource.memoir.UNKNOWN
import hoodland.opensource.memoir.showThrowable
import hoodland.opensource.toolbox.MatrixFile
import hoodland.opensource.toolbox.QuantumTextFile
import hoodland.opensource.toolbox.StringParser
import hoodland.opensource.toolbox.stdout
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.net.URLDecoder
import java.nio.charset.Charset
import kotlin.reflect.full.isSubclassOf


// Based on https://dzone.com/articles/get-all-classes-within-package
private val testLoader = Thread.currentThread().getContextClassLoader()
private val preclusions = ArrayList<Throwable>()

 //private val debugFile = File("${System.getProperty("user.home")}${File.separator}Documents${File.separator}Test Results${File.separator}KGDEBUG.html")
 //private val debuggingMemoir = Memoir("\uD83D\uDC1E DEBUG", null, debugFile.printWriter() )

/**
 * TestProgram
 *
 * This is the root class to run a Koarse Grind test suite.
 *
 */
object TestProgram {

    /**
     * run
     *
     * Calling this will cause Koarse Grind to search for any Test or TestFactory derivatives in the
     * classpath. Upon finding a Test it instantiates it and queues it to run. (Derivatives of the
     * ManufacturedTest class are not automatically instantiated or queued. That must be handled by
     * a TestFactory.) If a TestFactory derivative is found, it is instantiated and the populateProducts()
     * method is called. The contents of the "products" TestCollection are then queued to run as their
     * own subsection of the reports and output directories.
     *
     * @param name A full descriptive name for the root collection of tests. This will be at the top of the log. Example: "Test Suite - Hoodland Open Source Projects"
     * @param args Pass in the args from main(). These will be analyzed to filter which tests will be run.
     */
    fun run(name: String = UNKNOWN, args: Array<String> = Array<String>(0) { "" }) {
        val filterSet = parseArguments(args)
        val topLevel = Collector(TestCategory(name), testLoader, preclusions).assembledCollection

        val rootLog = topLevel.run(filterSet, preclusions)
        createSummaryReport(topLevel, rootLog)
        rootLog.conclude()
        //debuggingMemoir.conclude()
    }

    private fun createSummaryReport(topLevel: TestCategory, memoir: Memoir = Memoir(forPlainText = stdout)) {
        val fullyQualifiedSummaryFileName = topLevel.rootDirectory + File.separatorChar + SUMMARY_FILE_NAME
        memoir.info("Creating Test Suite Summary Report ($fullyQualifiedSummaryFileName)")
        var summaryReport = MatrixFile<String>("Category", "Test ID", "Name", "Description", "Status", "Reasons")

        try {
            // Try to append to an existing one
            summaryReport = MatrixFile.read(fullyQualifiedSummaryFileName, StringParser)
        } catch(dontCare: Throwable) {
            // Deliberate NO-OP
            // Leave the summaryReport as created above
        }

        topLevel.gatherForReport(summaryReport)

        summaryReport.write(fullyQualifiedSummaryFileName)


        // Section below creates a single line file stating the overall status
        val fullyQualifiedSummaryTextFileName = topLevel.rootDirectory + File.separatorChar + SUMMARY_TEXTFILE_NAME

        try {
            val textFile = QuantumTextFile(fullyQualifiedSummaryTextFileName)
            textFile.println(topLevel.overallStatus.toString())
            textFile.flush()
            textFile.close() // Shouldn't need a getter because this is a val
        } catch (thisFailure: Throwable) {
            memoir.error("Did not successfully create the overall status text file $fullyQualifiedSummaryTextFileName")
            memoir.showThrowable(thisFailure)
        }
    }

    private fun parseArguments(args: Array<String>): FilterSet? {
        val result = ArrayList<Filter>()

        var index = 0
        while (index < args.size) {
            val filterType = when(args[index].toUpperCase()) {
                "INCLUDE" -> FilterType.INCLUDE
                "EXCLUDE" -> FilterType.EXCLUDE
                else -> null
            }

            if (filterType != null) {
                index++
                val parts = args[index].split('=')

                val filterTarget = when(parts[0].toUpperCase()) {
                    "CATEGORIES", "CATEGORY" -> FilterTarget.CATEGORIES
                    "IDENTIFIERS", "IDENTIFIER" -> FilterTarget.IDENTIFIERS
                    "NAMES", "NAME" -> FilterTarget.NAMES
                    else -> null
                }

                if (filterTarget != null) {
                    if (parts.size > 1) {
                        val matchers = ArrayList<String>()

                        matchers.addAll(parts[1].toUpperCase().split(';', ',', '|'))

                        if (matchers.size > 0) {
                            result.add(Filter(filterType, filterTarget, matchers))
                        }
                    }
                }
            }

            index++
        }

        if (result.size < 1) return null
        return FilterSet(result)
    }
}