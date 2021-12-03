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

/**
 * HTTP_MESSAGE_BODY Indicates to your supplied callback function that the field being processed is the payload of an HTTP Request or Response.
 */
const val HTTP_MESSAGE_BODY = "HTTP Req/Resp Body/Payload"

/**
 * showHttpRequest: This renders a java.net.http.HttpRequest to the HTML log. In certain cases, particularly headers and body
 * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
 * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
 * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
 * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
 * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
 * supply a callbackFunction if none is needed.
 *
 * @param request The java.net.http.HttpRequest to be rendered.
 * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Memoir to display the outgoing content.
 * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON.
 */

fun Memoir.showHttpRequest(request: HttpRequest, bodyContentAsString: String? = null, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null) {
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

    var payload = ""

    bodyContentAsString?.let {
        payload = it
    }

    if (payload.length < 1) {
        if (request.bodyPublisher().isPresent) {
            payload = "(unknown content)"
        }
    }

    result.append("<br>${renderHeadersAndBody(request.headers(), payload, callbackFunction)}")

    writeToHTML(result.toString(), EMOJI_OUTGOING)
    echoPlainText(textRendition, EMOJI_OUTGOING)
}

/**
 * showHttpResponse: Properly renders a java.net.http.HttpResponse to the HTML log. In certain cases, particularly headers and body
 * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
 * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
 * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
 * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
 * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
 * supply a callbackFunction if none is needed.
 * @param response The java.net.http.HttpResponse to be rendered.
 * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON.
 */
fun Memoir.showHttpResponse(response: HttpResponse<*>, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null) {
    val statusCode = response.statusCode()
    var style = "implied_bad"
    if (statusCode.isSuccessfulStatusCode) { style = "implied_good" }

    val result = java.lang.StringBuilder("<div class=\"incoming $style\">\r\n")

    // Status code & description
    val textRendition = "$statusCode ${statusCode.toStatusCodeDescription()}"
    result.append("<center><h2>$textRendition</h2>")
    result.append(renderHeadersAndBody(response.headers(), response.body().toString(), callbackFunction))

    writeToHTML(result.toString(), EMOJI_INCOMING)
    echoPlainText(textRendition, EMOJI_INCOMING)
}

private fun Memoir.renderHeadersAndBody(headers: HttpHeaders, stringPayload: String, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null): String {
    val result = StringBuilder()
    val headerMap = headers.map()

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
        result.append("<br><br><small><i>(no headers)</i></small><br>\r\n")
    }

    // Body
    if ((stringPayload == "") || (stringPayload == "Optional.empty")) {
        result.append("<br><br><small><i>(no payload)</i></small></center>")
    } else {
        val size = stringPayload.length

        result.append("<br><b>Payload</b><br></center>\r\n")

        val renderedBody = treatAsCode(processString(HTTP_MESSAGE_BODY, stringPayload, callbackFunction))

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

/**
 * showHttpTransaction: Given a java.net.http.HttpRequest this renders it in the HTML log, uses java.net.http.HttpClient to send it and
 * receive a java.net.http.HttpResponse. The response will also be rendered to the HTML log. In certain cases, particularly headers and body
 * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
 * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
 * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
 * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
 * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
 * supply a callbackFunction if none is needed.
 * @param request The java.net.http.HttpRequest to be logged and sent.
 * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Memoir to display the outgoing content.
 * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON. This will be applied to BOTH the request and response.
 * @return The java.net.http.HttpResponse that was logged and returned.
 */
fun Memoir.showHttpTransaction(request: HttpRequest, bodyContentAsString: String? = null, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null): HttpResponse<*> {
    val client = HttpClient.newHttpClient()
    showHttpRequest(request, bodyContentAsString, callbackFunction)
    val result = client.send(request, HttpResponse.BodyHandlers.ofString())
    showHttpResponse(result, callbackFunction)
    return result
}

/* TODO: Provide a version of the function that uses the request embedded in the response.
 * The trick is getting a text version of the body correctly.
fun Memoir.showHttpTransaction(response: HttpResponse<*>, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null) {
    val request = response.request()
    showHttpRequest(request, "(tbd)", callbackFunction)
    showHttpResponse(response, callbackFunction)
}
 */