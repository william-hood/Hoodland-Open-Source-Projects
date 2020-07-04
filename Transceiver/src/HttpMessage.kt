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
import rockabilly.toolbox.divider
import rockabilly.toolbox.readLineFromInputStream
import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.http.HttpRequest


abstract class HttpMessage {
    // Java version kept the property 'server' here.
    // The Kotlin version keeps the property in the HttpHeaders class.
    protected var skipReadingHeaders = false

    val headers = HttpHeaders()
    var payload: HttpPayload<*>? = null

    @Throws(IOException::class)
    abstract fun toOutgoingStream(outputStream: DataOutputStream?)

    @Throws(IOException::class, HttpMessageParseException::class)
    fun populateFromIncomingStream(inputStream: BufferedInputStream?) {
        // This assumes we've read in far enough to begin reading the headers
        if (skipReadingHeaders) {
            // Deliberate NO-OP
        } else {
            inputStream!!.toHttpHeaders()

            // Skip the line after the headers
            readLineFromInputStream(inputStream)
        }
        /*
        if (this is HttpRequest) {
            headers.add(HttpHeader(HttpHeader.SERVER_HEADER_KEY, server))
        }
        */

        try {
            if (headers.contentIsMultipart) {
                payload = HttpMultipartPayload()
                payload!!.populateFromIncomingStream(inputStream!!, headers.multipartBoundary)
            } else if (headers.contentIsText) {
                payload = HttpStringPayload()
                payload!!.populateFromIncomingStream(inputStream!!)
            } else { //Assuming binary
                payload = HttpBinaryPayload()
                payload!!.populateFromIncomingStream(inputStream!!)
            }
        } catch (rethrownException: IOException) {
            throw rethrownException
        } catch (causalException: Exception) {
            throw HttpMessageParseException(causalException)
        }
    }

    override fun toString(): String {
        val result = StringBuilder(super.toString())
        result.append(divider())
        result.append(CarriageReturnLineFeed)
        result.append(CarriageReturnLineFeed)
        result.append(payload.toString())
        return result.toString()
    }

    companion object {
        const val PROTOCOL = "HTTP/1.1"
    }
}