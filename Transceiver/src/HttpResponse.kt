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
import rockabilly.toolbox.UnsetString
import rockabilly.toolbox.readEntireInputStream
import rockabilly.toolbox.readLineFromInputStream
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.charset.Charset


class HttpResponse : HttpMessage {
    private var statusCode: Int = Int.MIN_VALUE

    private constructor() {}
    constructor(blankPayload: HttpPayload<*>) {
        payload = blankPayload
    }

    constructor(code: Int, blankPayload: HttpPayload<*>) : this(blankPayload) {
        setStatusCode(code)
    }

    @Throws(IllegalStatusCodeException::class)
    fun setStatusCode(code: Int) {
        if (isInvalidStatusCode(code)) throw IllegalStatusCodeException(code)
        statusCode = code
    }

    fun getStatusCode(): Int {
        return statusCode
    }

    //override var server: String = UnsetString

    @Throws(IOException::class)
    override fun toOutgoingStream(outputStream: DataOutputStream?) {
        var outgoing: StringBuilder? = StringBuilder()
        outgoing!!.append("""$PROTOCOL $statusCode ${statusCodeDescription(statusCode)}
""")
        outgoing.append(HttpHeader.dateHeader.toString())
        outgoing.append("\r\n")
        outgoing.append(HttpHeader(HttpHeader.SERVER_HEADER_KEY, server).toString())
        outgoing.append("\r\n")
        outgoing.append(headersToOutgoingDataString())
        //outgoing.append("\r\n");
        //outgoing.append("\r\n");
        outputStream!!.writeBytes(outgoing.toString())
        payload!!.toOutgoingStream(outputStream)
        outgoing = null
    }

    override fun toString(): String {
        val result = StringBuilder()
        if (statusCode == Int.MIN_VALUE) {
            result.append("MISSING")
        } else {
            result.append(statusCode)
        }
        result.append(" STATUS CODE")
        if (statusCode != Int.MIN_VALUE) {
            result.append(" - ")
            result.append(statusCodeDescription(statusCode))
        }
        result.append(CarriageReturnLineFeed)
        result.append(CarriageReturnLineFeed)
        result.append(super.toString())
        return result.toString()
    }

    companion object {
        /*
	 * from http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html
	 *
      - 1xx: Informational - Request received, continuing process
      - 2xx: Success - The action was successfully received,
        understood, and accepted
      - 3xx: Redirection - Further action must be taken in order to
        complete the request
      - 4xx: Client Error - The request contains bad syntax or cannot
        be fulfilled
      - 5xx: Server Error - The server failed to fulfill an apparently
        valid request
	 */
        fun isInvalidStatusCode(code: Int): Boolean {
            return code < 100 || code > 599
        }

        fun isValidStatusCode(code: Int): Boolean {
            return !isInvalidStatusCode(code)
        }

        @Throws(IOException::class, HttpMessageParseException::class)
        fun fromInputStream(rawInputStream: BufferedInputStream): HttpResponse {
            // NEED TO DETERMINE WHETHER THIS IS STRING, BINARY, OR MULTIPART
            // ASSUMING STRING FOR NOW
            val result = HttpResponse()
            val unparsedMessage: String = readEntireInputStream(rawInputStream)
            try {
                // Determine the status code from first line
                var thisLine = ""
                var firstLineParts = thisLine.trim { it <= ' ' }.split("\\s+".toRegex()).toTypedArray()
                val fromString = BufferedInputStream(ByteArrayInputStream(unparsedMessage.toByteArray(Charset.defaultCharset())))
                if (unparsedMessage.startsWith("<")) {
                    result.httpContent!!.type = HttpContent.text
                    result.httpContent!!.subtype = HttpContent.html
                    result.skipReadingHeaders = true
                } else {
                    // handle first line
                    do {
                        thisLine = readLineFromInputStream(fromString)
                        firstLineParts = thisLine.trim { it <= ' ' }.split("\\s+".toRegex()).toTypedArray()
                    } while (thisLine.length < 1 || firstLineParts.size < 3)
                    result.statusCode = firstLineParts[1].toInt()
                }
                result.populateFromInputStream(fromString)
            } catch (rethrownException: IOException) {
                throw rethrownException
            } catch (causalException: Exception) {
                throw HttpMessageParseException(causalException)
            }
            return result
        }

        fun statusCodeDescription(code: Int): String {
            return when (code) {
                100 -> "Continue"
                200 -> "OK"
                201 -> "Created"
                202 -> "Accepted"
                203 -> "Non-authoritative Information"
                204 -> "No Content"
                205 -> "Reset Content"
                206 -> "Partial Content"
                300 -> "Multiple Choices"
                301 -> "Moved Permanently"
                302 -> "Found"
                303 -> "See Other"
                304 -> "Not Modified"
                305 -> "Use Proxy"
                306 -> "Unused"
                307 -> "Temporary Redirect"
                400 -> "Bad Request"
                401 -> "Unauthorized"
                402 -> "Payment Required"
                403 -> "Forbidden"
                404 -> "Not Found"
                405 -> "Method Not Allowed"
                406 -> "Not Acceptable"
                407 -> "Proxy Authentication Required"
                408 -> "Request Timeout"
                409 -> "Conflict"
                410 -> "Gone"
                411 -> "Length Required"
                412 -> "Precondition Failed"
                413 -> "Request Entity Too Large"
                414 -> "Request-url Too Long"
                415 -> "Unsupported Media Type"
                416 -> "Requested Range Not Satisfiable"
                417 -> "Expectation Failed"
                500 -> "Internal Server Error"
                501 -> "Not Implemented"
                502 -> "Bad Gateway"
                503 -> "Service Unavailable"
                504 -> "Gateway Timeout"
                505 -> "HTTP Version Not Supported"
                else -> ""
            }
        }
    }
}