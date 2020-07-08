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

import rockabilly.toolbox.*
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.charset.Charset


class HttpResponse() : HttpMessage(), Transceivable {
    private var managedStatusCode: Int = Int.MIN_VALUE
    var statusCode: Int
        get() = managedStatusCode
        @Throws(IllegalStatusCodeException::class)
        set(it) {
            if (it.isInvalidStatusCode) throw IllegalStatusCodeException(it)
            managedStatusCode = it
        }

    constructor(code: Int) : this() {
        statusCode = code
    }

    //override var server: String = UnsetString

    override fun sendToOutgoingStream(outputStream: DataOutputStream) {
        headers.enforceServerHeaderExists()
        headers.updateDateHeader()
        outputStream.writeBytes("$PROTOCOL_AND_VERSION $statusCode ${statusCode.toStatusCodeDescription()}$CarriageReturnLineFeed")

        super.sendToOutgoingStream(outputStream)
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
            result.append(statusCode.toStatusCodeDescription())
        }
        result.append(CarriageReturnLineFeed)
        result.append(CarriageReturnLineFeed)
        result.append(super.toString())
        return result.toString()
    }

    override fun populateFromIncomingStream(inputStream: BufferedInputStream, multipartBoundary: String?) {
        try {

            // Determine the status code from first line
            var thisLine = ""
            var firstLineParts = Array<String>(1){""}
            do {
                thisLine = readLineFromInputStream(inputStream)
                val firstLineParts = thisLine.trim { it <= ' ' }.split("\\s+".toRegex()).toTypedArray()
            } while (thisLine.length < 1 || firstLineParts.size < 3)
            this.statusCode = firstLineParts[1].toInt()

            this.headers.populateFromIncomingStream(inputStream)

            if (headers.contentIsText) {
                payload = HttpStringPayload()
            } else if (headers.contentIsMultipart) {
                payload = HttpMultipartPayload()
            } else {
                payload = HttpBinaryPayload()
            }

            readPayload(inputStream)
        } catch (rethrownException: IOException) {
            throw rethrownException
        } catch (causalException: Exception) {
            throw HttpMessageParseException(causalException)
        }
    }
}