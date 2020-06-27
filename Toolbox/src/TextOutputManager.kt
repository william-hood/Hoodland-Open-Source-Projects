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

package rockabilly.toolbox

import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter

val stdout = PrintWriter(System.out)

class TextOutputManager {
    // Creates a file with header only if the first call to write is made.
    // Need method to properly flush and close. ???
    private var internalPrintWriter: PrintWriter? = null
    private var expectedFileName: String = UnsetString
    private var shouldAppend = false

    constructor() {}
    constructor(outputStream: PrintWriter?) {
        internalPrintWriter = outputStream
    }

    @JvmOverloads
    constructor(filename: String, append: Boolean = false) {
        expectedFileName = filename
        shouldAppend = append
    }

    private fun createPrintWriterIfNeeded() {
        if (internalPrintWriter == null) {
            if (expectedFileName === UnsetString) {
                internalPrintWriter = PrintWriter(System.out)
            } else {
                try {
                    forceParentDirectoryExistence(expectedFileName)
                    internalPrintWriter = PrintWriter(FileWriter(
                            expectedFileName, shouldAppend))
                } catch (dontCare: IOException) {
                    internalPrintWriter = PrintWriter(System.out)
                    println("WARNING: Could not create output file ($expectedFileName).  Reverting to standard console output.")
                }
            }
        }
    }

    fun println(output: String?) {
        createPrintWriterIfNeeded()
        internalPrintWriter!!.print(output)
        internalPrintWriter!!.print(CarriageReturnLineFeed)
    }

    fun flush() {
        try {
            internalPrintWriter!!.flush()
        } catch (dontCare: NullPointerException) {
        }
    }

    fun close() {
        try {
            flush()
            internalPrintWriter!!.close()
        } catch (dontCare: NullPointerException) {
        } finally {
            internalPrintWriter = null
            System.gc()
        }
    }
}