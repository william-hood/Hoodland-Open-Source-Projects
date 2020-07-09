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

package rockabilly.memoir

import rockabilly.toolbox.isSuccessfulStatusCode
import rockabilly.toolbox.toStatusCodeDescription
import java.net.URL
import java.time.LocalDateTime
import java.util.*

// In Java/Kotlin/JVM-in-general there are many HTTP Clients/Servers available. Rather than adopt
// one over the others, Memoir provides a generic means to render a Request and Response, with only
// plaintext bodies supported. Rockabilly Transceiver provides a usage example.
fun Memoir.ShowHttpRequest(Verb: String, CompleteUrl: String, Headers: Map<String, ArrayList<String>>, StringPayload: String = "", PlaintextRendition: String? = null) {
    val timeStamp = LocalDateTime.now()

    val url = URL(CompleteUrl)
    val queries = ArrayList<String>()

    if (url.query != null) {
        queries.addAll(url.query.split('&')) // Starts with ?
    }

    val result = StringBuilder("<div class=\"outgoing implied_caution\">\r\n")

    result.append("<center><h2>$Verb ${url.path}</h2>")
    result.append("<small><b><i>${url.host}</i></b></small>")

    // Hide the Full URL
    val identifier = UUID.randomUUID().toString()
    result.append("<br><br><label for=\"$identifier\">\r\n<input id=\"$identifier\" type=\"checkbox\"><small><i>(show complete URL)</i></small>\r\n<div class=\"${this.encapsulationTag}\">\r\n")

    result.append("<br>\r\n${CompleteUrl.replace("&", "&amp;")}\r\n")
    result.append("</div>\r\n</label>")

    if (queries.size < 1) {
        result.append("<br><br><small><i>(no query)</i></small>")
    } else {
        //this.ShowArray(queries as Array<Any>, "Queries", 1)
        result.append("<br><br><b>Queries</b><br><table class=\"gridlines\">\r\n")

        queries.forEach {
            val part = it.split('=')
            result.append("<tr><td>")
            result.append(part[0].replace("?", ""))
            result.append("</td><td>")
            if (part.size > 1)
            {
                // Attempt Base64 Decode and JSON pretty-print here.
                result.append(ProcessString(part[1]))
            } else
            {
                result.append("(unset)")
            }
            result.append("</td></tr>")
        }
        result.append("\r\n</table><br>")
    }

    result.append(renderHeadersAndBody(Headers, StringPayload))

    WriteToHTML(result.toString())
    if (PlaintextRendition != null) { EchoPlainText(PlaintextRendition) }
}

fun Memoir.ShowHttpResponse(StatusCode: Int, Headers: Map<String, ArrayList<String>>, StringPayload: String = "", PlaintextRendition: String? = null) {
    val timeStamp = LocalDateTime.now()
    var style = "implied_bad"
    if (StatusCode.isSuccessfulStatusCode) { style = "implied_good" }

    val result = java.lang.StringBuilder("<div class=\"incoming $style\">\r\n")

    // Status code & description
    result.append("<center><h2>$StatusCode ${StatusCode.toStatusCodeDescription()}</h2>")

    result.append(renderHeadersAndBody(Headers, StringPayload))

    WriteToHTML(result.toString())
    if (PlaintextRendition != null) { EchoPlainText(PlaintextRendition) }
}

private fun renderHeadersAndBody(Headers: Map<String, ArrayList<String>>, StringPayload: String): String {
    val result = StringBuilder()

    // Headers
    if (Headers.size > 0) {
        result.append("<br><b>Headers</b><br><table class=\"gridlines\">\r\n")

        Headers.forEach() {
            result.append("<tr><td>")
            result.append(it.key)
            result.append("</td><td>")

            if (it.value.size < 1) {
                result.append("<small><i>(empty)</i></small>")
            } else if (it.value.size == 1) {
                // Attempt Base64 Decode and JSON pretty-print here.
                result.append(ProcessString(it.value[0].toString()))
            } else {
                result.append("<center><table class=\"gridlines\">\r\n")
                it.value.forEach() {
                    result.append("<tr><td>")
                    // Attempt Base64 Decode and JSON pretty-print here.
                    result.append(ProcessString(it.toString()))
                    result.append("</td></tr>")
                }
            }

            result.append("</td></tr>")
        }
        result.append("\r\n</table><br>")
    }
    else {
        result.append("<br><br><small><i>(no headers)</i></small>\r\n")
    }

    // Body
    if (StringPayload == "") {
        result.append("<br><br><small><i>(no payload)</i></small></center>")
    } else {
        result.append("<br><b>Payload</b><br></center>\r\n")
        // TODO: Remove <pre/> tags if ProcessString handles that
        result.append("<pre>\r\n")
        // Attempt Base64 Decode and JSON pretty-print here.
        result.append(ProcessString(StringPayload))
        result.append("\r\n</pre>\r\n")
    }

    result.append("</div>")
    return result.toString()
}