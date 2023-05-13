// Copyright (c) 2020, 2023 William Arthur Hood
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

// NOTE: See comments at end of file regarding methods from the Java & C# code that are now obsolete.

package hoodland.opensource.toolbox

import java.io.*
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.AbstractMap.SimpleEntry
import java.util.zip.CRC32
import java.util.zip.CheckedInputStream
import kotlin.collections.ArrayList

/**
 * Provides a PrintWriter pointed at stdout
 */
val stdout = PrintWriter(System.out)

/**
 * Provides a PrintWriter pointed at stderr
 */
val stderr = PrintWriter(System.err)

private val quickDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd kk-mm-ss.SSS")

/**
 * Provides a plaintext date and time formatted as yyyy-MM-dd kk-mm-ss.SSS
 */
val quickTimestamp: String
    get() = quickDateFormat.format(LocalDateTime.now())

/**
 * Given a path to an existing file, provides an open BufferedReader for it.
 */
@Throws(FileNotFoundException::class)
fun openForReading(filePath: String): BufferedReader {
    return BufferedReader(FileReader(filePath))
}

private const val REPLACER = "\u25a2"
fun filterOutNonPrintables(candidate: String): String? {
    return candidate.replace("\\p{C}".toRegex(), REPLACER)
}

private const val nullString = "(null)"
fun robustGetString(candidate: Any?): String? {
    return if (candidate == null) nullString else try {
        filterOutNonPrintables(candidate.toString())
    } catch (dontCare: NullPointerException) {
        nullString
    } catch (dontCare: Throwable) {
        "(ERROR)"
    }
}

fun getOperatingSystemName(): String {
    return System.getProperty("os.name")
}

fun getCurrentWorkingDirectory(): String {
    return System.getProperty("user.dir")
}

fun getUserHomeFolder(): String {
    return System.getProperty("user.home")
}

// From http://stackoverflow.com/questions/2546078/java-random-long-number-in-0-x-n-range

/**
 * Provides a random Long integer between 0 and n
 * @param rng A random number generator (Random class) that you have already instantiated.
 * @param n The upper bound of the random Long integer. 0 is the lower bound.
 * @return a random Long integer between 0 and n
 */
fun nextLong(rng: Random, n: Long): Long {
    // error checking and 2^x checking removed for simplicity.
    var bits: Long
    var `val`: Long
    do {
        bits = rng.nextLong() shl 1 ushr 1
        `val` = bits % n
    } while (bits - `val` + (n - 1) < 0L)
    return `val`
}

fun randomInt(min: Int, max: Int): Int {
    return min + Random().nextInt(max - min)
}

fun randomInteger(min: Int, max: Int): Int {
    return randomInt(min, max)
}

// TODO: Many of the following functions are cased wrong! Should not start with upper case!
// TODO: Should this be in String Extensions???
fun StringIsEmpty(candidate: String?): Boolean {
    if (candidate == null) return true
    if (candidate === "") return true
    return if (candidate.length < 1) true else false
}

// TODO: Is this duplicated in String Extensions???
fun StringsMatch(x: String?, y: String?): Boolean {
    if (x == null && y == null) return true
    if (x == null && y != null) return false
    return if (x != null && y == null) false else x!!.compareTo(y!!) == 0
}

// TODO: Is this duplicated in String Extensions???
fun StringsMatchCaseInspecific(x: String?, y: String?): Boolean {
    if (x == null && y == null) return true
    if (x == null && y != null) return false
    return if (x != null && y == null) false else x!!.uppercase().compareTo(y!!.uppercase()) == 0
}

fun stringArrayContains(candidateArray: Array<String?>,
                        candidateString: String?): Boolean {
    for (cursor in candidateArray.indices) {
        if (StringsMatch(candidateArray[cursor], candidateString)) return true
    }
    return false
}

fun stringArrayContainsCaseInspecific(
        candidateArray: Array<String?>, candidateString: String?): Boolean {
    for (cursor in candidateArray.indices) {
        if (StringsMatchCaseInspecific(candidateArray[cursor],
                        candidateString)) return true
    }
    return false
}

fun sortMarkupTags(target: String): ArrayList<String>? {
    val result = ArrayList<String>()
    var thisString = StringBuilder()
    var levelsIn = 0
    for (index in 0 until target.length) {
        val thisChar = target[index]
        if (levelsIn > 0) {
            if (thisChar == '<') {
                levelsIn++
            } else if (thisChar == '>') {
                levelsIn--
            }
            thisString.append(thisChar)
            if (levelsIn == 0) {
                if (thisString.length > 0) {
                    result.add(thisString.toString())
                    thisString = StringBuilder()
                }
            }
        } else {
            if (thisChar == '<') {
                if (thisString.length > 0) {
                    result.add(thisString.toString())
                    thisString = StringBuilder()
                }
                levelsIn = 1
            }
            thisString.append(thisChar)
        }
    }
    if (thisString.length > 0) {
        result.add(thisString.toString())
    }
    return result
}

// Based on http://stackoverflow.com/questions/13592236/parse-a-uri-string-into-name-value-collection
val URL.queryParamsAsNameValuePairs : ArrayList<SimpleEntry<String, String>>
        get() {
            val result = ArrayList<SimpleEntry<String, String>>()
            val params = this.query.split("&".toRegex()).toTypedArray()
            for (thisParam in params) {
                val keyValuePair = thisParam.split("=".toRegex()).toTypedArray()
                result.add(SimpleEntry(keyValuePair[0], keyValuePair[1]))
            }
            return result
        }

val File.crc32ChecksumValue: Long
    get() {
        val midStream = CheckedInputStream(this.inputStream(), CRC32())
        val readStream = BufferedInputStream(midStream)
        try {
            while (readStream.read() != -1) {
                // Read the file in completely
            }
        } finally {
            readStream.close()
        }

        return midStream.checksum.value
    }

// TODO: Verify these comments are not needed. Does the Java wrapper need any of these?
// Legacy method readLineFromInputStream() is OBSOLETE
// val check = BufferedReader(InputStreamReader(rawInputStream))
// check.readLine()
// There is also readLine for stdin and File.forEachLine() for files...

// Legacy method readEntireInputStream() is OBSOLETE
//  val check = BufferedReader(InputStreamReader(rawInputStream))
//  check.readText()
// File.readLines() can get the whole file as an array of lines
// Creating the BufferedInputStream as shown above also provides readLines()
// String(rawInputStream.readAllBytes())

// Legacy function forceParentDirectoryExistence() is probably OBSOLETE.
// Use Files.createDirectories(dest.getParent()) or File(fileName).parentFile.mkdirs()
/*
// Based on http://stackoverflow.com/questions/8668905/directory-does-not-exist-with-filewriter
fun forceParentDirectoryExistence(fileName: String?) {
    var file: File? = File(fileName)
    var parent_directory = file!!.parentFile
    parent_directory?.mkdirs()
}
 */

// Legacy function forceDirectoryExistence() is OBSOLETE. Use File.mkdirs()

// Legacy function getShortFileName() is OBSOLETE: Use File("myFile.txt").nameWithoutExtension
/*
fun getShortFileName(completeFilePath: String): String? {
    val baseExt = completeFilePath.substring(completeFilePath
            .lastIndexOf(File.separatorChar) + 1)
    return if (completeFilePath.contains(".")) {
        baseExt.substring(0, baseExt.lastIndexOf('.'))
    } else baseExt
}
 */

// Obsoleting this in favor of a namesake function in Memoir
/*
fun depictFailure(thisFailure: Throwable): String? {
    val stacktraceWriter = StringWriter()
    thisFailure.printStackTrace(PrintWriter(stacktraceWriter))
    /*
		 * Throwable cause = thisFailure.getCause(); if (cause != null) {
		 * stacktraceWriter.append(Symbols.NewLine);
		 * stacktraceWriter.append("Caused by " + FX.arrow());
		 * stacktraceWriter.append(depictFailure(cause)); }
		 */return stacktraceWriter.toString()
}
 */

// Legacy function hardDelete() is OBSOLETE: Use File(<complete-path>).deleteRecursively()

// Legacy function copyCompletely() is OBSOLETE: Use File(<complete-path>).copyRecursively()
