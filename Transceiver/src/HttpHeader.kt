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

import rockabilly.toolbox.UnsetString
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.AbstractMap.SimpleEntry

// TODO: This has to be changed to Map<String, ArrayList<String?>>
// The value on the header can be many strings, not just one.
// TODO: Intellisense shows Transceiver to look like a Java/Kotlin b@st_rd love child. Make it into proper Kotlin.
class HttpHeader(key: String, value: String) : SimpleEntry<String, String>(key, value) {
    companion object {
        private const val serialVersionUID = 5682603081858980317L
        private val httpDateFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).withZone(ZoneId.of("GMT")) //SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        const val DATE_HEADER_KEY = "Date"
        const val SERVER_HEADER_KEY = "Server"
        val httpDate: String
            get() = httpDateFormat.format(LocalDateTime.now())

        fun fromString(headerLine: String): HttpHeader {
            val split = headerLine.indexOf(':')
            return if (split == -1) HttpHeader(headerLine, UnsetString) else HttpHeader(headerLine.substring(0, split), headerLine.substring(split + 2))
        }

        val dateHeader: HttpHeader
            get() = HttpHeader(DATE_HEADER_KEY, httpDate)
    }

    override fun toString(): String {
        return key.toString() + ": " + value
    }
}