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

package hoodland.opensource.memoir

import hoodland.opensource.toolbox.isSuccessfulStatusCode
import hoodland.opensource.toolbox.toStatusCodeDescription
import java.net.http.HttpClient
import java.net.http.HttpHeaders
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

const val HTTP_MESSAGE_BODY = "HTTP Req/Resp Body/Payload"

fun Memoir.showHttpRequest(request: HttpRequest, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null) {
    val uri = request.uri()
    val queries = ArrayList<String>()
    val result = StringBuilder("<div class=\"outgoing implied_caution\">\r\n")

    if (uri.query != null) {
        queries.addAll(uri.query.split('&')) // Starts with ?
    }

    val textRendition = "${request.method()} ${uri.path}"
    result.append("<center><h2>$textRendition</h2>")
    result.append("<small><b><i>${uri.host}</i></b></small>")

    // Hide the Full URL
    val identifier = UUID.randomUUID().toString()
    result.append("<br><br><label for=\"$identifier\">\r\n<input id=\"$identifier\" type=\"checkbox\"><small><i>(show complete URL)</i></small>\r\n<div class=\"${this.encapsulationTag}\">\r\n")

    result.append("<br>\r\n${request.uri().toString().replace("&", "&amp;")}\r\n")
    result.append("</div>\r\n</label>")

    if (queries.size < 1) {
        result.append("<br><br><small><i>(no query)</i></small>")
    } else {
        result.append("<br><br><b>Queries</b><br><table class=\"gridlines\">\r\n")

        queries.forEach {
            val part = it.split('=')
            result.append("<tr><td>")
            result.append(part[0].replace("?", ""))
            result.append("</td><td>")
            if (part.size > 1)
            {
                result.append(processString(part[0], part[1], callbackFunction))
            } else
            {
                result.append("(unset)")
            }
            result.append("</td></tr>")
        }
        result.append("\r\n</table>")
    }

    result.append("<br>${renderHeadersAndBody(request.headers(), request.bodyPublisher().toString())}")

    writeToHTML(result.toString(), EMOJI_OUTGOING)
    echoPlainText(textRendition, EMOJI_OUTGOING)
}

fun Memoir.showHttpResponse(response: HttpResponse<*>, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null) {
    val statusCode = response.statusCode()
    var style = "implied_bad"
    if (statusCode.isSuccessfulStatusCode) { style = "implied_good" }

    val result = java.lang.StringBuilder("<div class=\"incoming $style\">\r\n")

    // Status code & description
    val textRendition = "$statusCode ${statusCode.toStatusCodeDescription()}"
    result.append("<center><h2>$textRendition</h2>")

    result.append(renderHeadersAndBody(response.headers(), response.body().toString()))

    writeToHTML(result.toString(), EMOJI_INCOMING)
    echoPlainText(textRendition, EMOJI_INCOMING)
}

private fun Memoir.renderHeadersAndBody(Headers: HttpHeaders, StringPayload: String, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null): String {
    val result = StringBuilder()
    val headerMap = Headers.map()

    // Headers
    if (headerMap.size > 0) {
        result.append("<br><b>Headers</b><br>")
        val renderedHeaders = StringBuilder("<table class=\"gridlines\">\r\n")

        headerMap.forEach() {thisHeader->
            renderedHeaders.append("<tr><td>")
            renderedHeaders.append(thisHeader.key)
            renderedHeaders.append("</td><td>")

            if (thisHeader.value.size < 1) {
                renderedHeaders.append("<small><i>(empty)</i></small>")
            } else if (thisHeader.value.size == 1) {
                // Attempt Base64 Decode and JSON pretty-print here.
                renderedHeaders.append(processString(thisHeader.key, thisHeader.value[0].toString(), callbackFunction))
            } else {
                renderedHeaders.append("<table class=\"gridlines neutral\">\r\n")
                thisHeader.value.forEach() {
                    renderedHeaders.append("<tr><td>")
                    // Attempt Base64 Decode and JSON pretty-print here.
                    renderedHeaders.append(processString(thisHeader.key, it.toString(), callbackFunction))
                    renderedHeaders.append("</td></tr>")
                }
                renderedHeaders.append("\r\n</table>")
            }

            renderedHeaders.append("</td></tr>")
        }
        renderedHeaders.append("\r\n</table><br>")

        if (headerMap.keys.size > MAX_HEADERS_TO_DISPLAY) {
            val identifier1 = UUID.randomUUID().toString()
            result.append("<label for=\"$identifier1\">\r\n<input id=\"$identifier1\" type=\"checkbox\">\r\n(show ${headerMap.keys.size} headers)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
            result.append(renderedHeaders)
            result.append("</div></label>");
        } else {
            result.append(renderedHeaders)
        }
    }
    else {
        result.append("<br><br><small><i>(no headers)</i></small>\r\n")
    }

    // Body
    if ((StringPayload == "") || (StringPayload == "Optional.empty")) {
        result.append("<br><br><small><i>(no payload)</i></small></center>")
    } else {
        val size = StringPayload.length

        result.append("<br><b>Payload</b><br></center>\r\n")

        val renderedBody = treatAsCode(processString(HTTP_MESSAGE_BODY, StringPayload, callbackFunction))

        if (size > MAX_BODY_LENGTH_TO_DISPLAY) {
            val identifier2 = UUID.randomUUID().toString()
            result.append("<label for=\"$identifier2\">\r\n<input id=\"$identifier2\" type=\"checkbox\">\r\n(show large payload)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
            result.append(renderedBody);
            result.append("</div></label>")
        } else {
            result.append(renderedBody)
        }
    }

    return result.toString()
}

fun Memoir.showHttpTransaction(request: HttpRequest, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null): HttpResponse<*> {
    val client = HttpClient.newHttpClient()
    showHttpRequest(request, callbackFunction)
    val result = client.send(request, HttpResponse.BodyHandlers.ofString())
    showHttpResponse(result, callbackFunction)
    return result
}