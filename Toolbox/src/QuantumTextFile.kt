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

package hoodland.opensource.toolbox

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter

/**
 * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
 *
 * @constructor
 *
 * @param filename The name of the file to write. No file will be created unless at least one call to print() or println() is made.
 * @param append Set this to true to append to an existing file.
 * @param outputStream Normally omit this. If you want to use QuantumTextFile to operate on a file (or any PrintWriter) you already have open, supply it here.
 */
class QuantumTextFile(val filename: String = UNSET_STRING, val append: Boolean = false, var outputStream: PrintWriter? = null) {
    private fun createPrintWriterIfNeeded() {
        if (outputStream == null) {
            if (filename === UNSET_STRING) {
                outputStream = PrintWriter(System.out)
            } else {
                try {
                    // Force the parent directory to exist...
                    File(filename).parentFile.mkdirs()

                    outputStream = PrintWriter(FileWriter(filename, append))
                } catch (dontCare: IOException) {
                    outputStream = PrintWriter(System.out)
                    println("WARNING: Could not create output file ($filename).  Reverting to standard console output.")
                }
            }
        }
    }

    /**
     * println: Sends a string to the output stream followed by a carriage-return-line-feed sequence. (Assuming Windows-style CR/LF is deliberate because all platforms handle it well.)
     * Calling this function will create the output file.
     *
     * @param output The string to send to the output stream.
     */
    fun println(output: String) {
        print("$output$CRLF")
    }

    /**
     * print: Sends a string to the output stream. Calling this function will create the output file.
     *
     * @param output The string to send to the output stream.
     */
    fun print(output: String) {
        createPrintWriterIfNeeded()
        outputStream!!.print(output)
    }

    /**
     * flush: This will flush the output stream if one exists (AKA if the file has been created) but will do nothing otherwise.
     *
     */
    fun flush() {
        outputStream?.let {
            it.flush()
        }
    }

    /**
     * close: This will flush and then close the output stream if one exists (AKA if the file has been created).
     * It will also request garbage collection from the JVM, regardless of whether or not the file was created.
     *
     */
    fun close() {
        flush()
        outputStream?.let {
            it.close()
        }
        System.gc()
    }
}
