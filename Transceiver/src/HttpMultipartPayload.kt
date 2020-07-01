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
import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*


class HttpMultipartPayload : HttpPayload<ArrayList<HttpPayload<*>?>?> {
    var MULTIPART_BOUNDARY = DEFAULT_MULTIPART_BOUNDARY
    override var content: ArrayList<HttpPayload<*>?>? = ArrayList()

    @Throws(IOException::class)
    override fun toOutgoingStream(outputStream: DataOutputStream?) {
        for (thisPart in content!!) {
            outputStream!!.writeBytes(TERMINUS)
            outputStream.writeBytes(MULTIPART_BOUNDARY)
            thisPart!!.toOutgoingStream(outputStream)
        }
        outputStream!!.writeBytes(TERMINUS)
        outputStream.writeBytes(MULTIPART_BOUNDARY)
        outputStream.writeBytes(TERMINUS)
    }

    override fun toString(): String {
        val result = StringBuilder("(MULTIPART PAYLOAD)")
        result.append(CarriageReturnLineFeed)
        if (content == null) {
            result.append(divider())
            result.append(CarriageReturnLineFeed)
            result.append(CarriageReturnLineFeed)
            result.append("(null)")
        } else {
            for (thisPart in content!!) {
                if (result.length < 1) {
                    result.append(divider())
                    result.append(CarriageReturnLineFeed)
                    result.append(CarriageReturnLineFeed)
                }
                result.append(thisPart.toString())
            }
            result.append(divider())
            result.append(CarriageReturnLineFeed)
            result.append(CarriageReturnLineFeed)
            result.append("(END MULTIPART)")
        }
        return result.toString()
    }

    @Throws(IOException::class, HttpMessageParseException::class)
    override fun populateFromInputStream(rawInputStream: BufferedInputStream, multipartBoundary: String?) {
        val incomingHeaders = HttpHeadersList()
        incomingHeaders.populateHeadersFromInputStream(rawInputStream)
        if (incomingHeaders.httpContent!!.isText) {
            var newStringPayload: HttpStringPayload? = incomingHeaders as HttpStringPayload
            newStringPayload!!.populateFromInputStream(rawInputStream, multipartBoundary)
            content!!.add(newStringPayload)
            newStringPayload = null
        } else {
            var newBinaryPayload: HttpBinaryPayload? = incomingHeaders as HttpBinaryPayload
            newBinaryPayload!!.populateFromInputStream(rawInputStream, multipartBoundary)
            content!!.add(newBinaryPayload)
            newBinaryPayload = null
        }
    }

    override val isEmpty: Boolean
        get() = content!!.size < 1

    override val contentLength: Int
        get() {
            var result = 0
            for (thisPart in content!!) {
                result += TERMINUS.length
                result += MULTIPART_BOUNDARY.length
                result += thisPart!!.contentLength
            }
            result += TERMINUS.length
            result += MULTIPART_BOUNDARY.length
            result += TERMINUS.length
            return result
        }

    companion object {
        const val DEFAULT_MULTIPART_BOUNDARY = "X_ROCKABILLY_TRANSCEIVER_BOUNDARY_X"
        private const val TERMINUS = "--"
    }
}