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

package rockabilly.transceiver

import rockabilly.toolbox.CarriageReturnLineFeed
import rockabilly.toolbox.StringIsEmpty
import rockabilly.toolbox.readLineFromInputStream
import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val WORK_AROUND_DELAY: Long = 0
const val DATE_HEADER_KEY = "Date"
const val SERVER_HEADER_KEY = "Server"

open class HttpHeaders: HashMap<String, ArrayList<String>>(), Transceivable {
    // Yes I'm aware of HashMap.merge(). It was difficult to find a proper example of how to use it in Kotlin.
    // The examples I could find were in Java and were not always readable. Decided to just do it my own way.
    fun add(key: String, values: ArrayList<String>) {
        if (this.containsKey(key)) {
            this[key]!!.addAll(values.toList())
        } else {
            put(key, values)
        }
    }

    private fun initialHeaderValue(value: String): ArrayList<String> {
        val result = ArrayList<String>()
        result.add(value)
        return result
    }

    fun add(key: String, value: String) {
        add(key, initialHeaderValue(value))
    }

    fun clobber(key: String, values: ArrayList<String>) {
        put(key, values)
    }

    fun clobber(key: String, value: String) {
        clobber(key, initialHeaderValue(value))
    }

    fun addAll(otherHeaders: HashMap<String, ArrayList<String>>) {
        otherHeaders.keys.forEach {
            if (otherHeaders[it] != null) {
                this.add(it, otherHeaders[it]!!)
            }
        }
    }

    fun enforcePresent(key: String, valueIfNotPresent: String) {
        if (! this.containsKey(SERVER_HEADER_KEY)) {
            this.add(key, valueIfNotPresent)
        }
    }

    //SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
    private val httpDateFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).withZone(ZoneId.of("GMT"))
    val httpDate: String
        get() = httpDateFormat.format(LocalDateTime.now())
    internal fun updateDateHeader() {
        clobber(DATE_HEADER_KEY, httpDate)
    }

    // Determined that in the old Java version there's trivial or no difference
    // between headersToOutgoingDataString() and toString(). So they're the same thing now.
    override fun toString(): String {
        enforceServerHeaderExists()
        val result = StringBuilder()
        if (isNotEmpty()) {
            this.keys.forEach {key ->
                val values = this[key]
                if (values != null) {
                    values.forEach {value ->
                        result.append("$key: $value$CarriageReturnLineFeed")
                    }
                }
            }
        }

        return result.toString()
    }

    override fun sendToOutgoingStream(outputStream: DataOutputStream) {
        outputStream.writeBytes(this.toString())
    }

    val declaredServer: String
        get() {
            enforceServerHeaderExists()
            return this[SERVER_HEADER_KEY]!!.first()
        }

    // May need a LocalDateTime property or a way to
    // get a LocalDateTime from the various date fields.

    internal fun enforceServerHeaderExists() {
        enforcePresent(SERVER_HEADER_KEY, SERVER_NAME)
    }

    // Determine Content Type & Subtype
    // The Content-Type header is "special" and the extensions in ContentType.kt
    // allow it to be treated almost like a separate object.
    // These fields need to be here because they may not be extended in ContentType.kt
    internal var determinedContentType: String? = null
    internal var determinedContentSubtype: String = ""

    override fun populateFromIncomingStream(inputStream: BufferedInputStream, multipartBoundary: String?) {
        // This assumes a newly constructed instance. It does not call clear().
        // It also assumes we've read in far enough to begin reading the headers
        try {
            // Read content type and headers
            var headerLine: String = ""
            while (StringIsEmpty(headerLine)) {
                headerLine = readLineFromInputStream(inputStream).trim()
            }

            // TODO: May have to skip headers if HTML is detected at this point.
            while (!StringIsEmpty(headerLine)) {
                // Comment in the old Java version, accurate as of 2016
                //
                // It appears that Java itself has a race condition (thanks Oracle)
                // Need to print, or otherwise access headerLine here or you
                // might get a mysterious nullref downstream. *sigh*
                if (WORK_AROUND_DELAY > 0) { Thread.sleep(WORK_AROUND_DELAY) }

                var headerKey = headerLine
                val headerValues = ArrayList<String>()
                val split = headerLine.indexOf(':')
                if (split != -1) {
                    headerKey = headerLine.substring(0, split)
                    val allHeaderValues = headerLine.substring(split + 2)

                    when(headerKey) {
                        "Date",
                        "Last-Modifed",
                        "Expires",
                        "Accept-Datetime",
                        "If-Modified-Since",
                        "If-Unmodified-Since"
                        -> headerValues.add(allHeaderValues)
                        else -> headerValues.addAll(allHeaderValues.split(','))
                    }
                }

                this.add(headerKey, headerValues)
                headerLine = readLineFromInputStream(inputStream).trim()
            }

        } catch (causalException: Exception) {
            throw HttpMessageParseException(causalException)
        }
    }
}

/* DELTHIS
fun BufferedInputStream.toHttpHeaders(): HttpHeaders {
    // This assumes we've read in far enough to begin reading the headers
    // Unlike the Java version this does NOT return the server.
    // It returns the constructed object (Java version had this as a
    // static class member). Use the declaredServer property instead.

    val result = HttpHeaders()

    try {
        // Read content type and headers
        var headerLine: String = readLineFromInputStream(this)
        while (!StringIsEmpty(headerLine)) {
            // Comment in the old Java version, accurate as of 2016
            //
            // It appears that Java itself has a race condition (thanks Oracle)
            // Need to print, or otherwise access headerLine here or you
            // might get a mysterious nullref downstream. *sigh*
            if (WORK_AROUND_DELAY > 0) { Thread.sleep(WORK_AROUND_DELAY) }

            var headerKey = headerLine
            val headerValues = ArrayList<String>()
            val split = headerLine.indexOf(':')
            if (split != -1) {
                headerKey = headerLine.substring(0, split)
                val allHeaderValues = headerLine.substring(split + 2)

                when(headerKey) {
                    "Date",
                    "Last-Modifed",
                    "Expires",
                    "Accept-Datetime",
                    "If-Modified-Since",
                    "If-Unmodified-Since"
                        -> headerValues.add(allHeaderValues)
                    else -> headerValues.addAll(allHeaderValues.split(','))
                }
            }

            result.add(headerKey, headerValues)
        }

    } catch (causalException: Exception) {
        throw HttpMessageParseException(causalException)
    }

    return result
}

 */