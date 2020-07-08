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


class HttpBinaryPayload : HttpPayload<ByteArray?>(), Transceivable {
    override var content: ByteArray? = null

    @Throws(IOException::class)
    override fun sendToOutgoingStream(outputStream: DataOutputStream) {
        /*
		if (headers.size() > 0) {
			outputStream.writeBytes(headersToOutgoingDataString());
			outputStream.writeBytes("\r\n");
		}
		*/
        outputStream!!.write(content)
    }

    override fun toString(): String {
        val result = StringBuilder()
        result.append("BINARY CONTENT: ")
        if (content == null) {
            result.append("(null)")
        } else {
            result.append(content!!.size)
            result.append(" bytes")
        }
        return result.toString()
    }

    private fun takeContent(buffer: ArrayList<Byte>, length: Int) {
        var length = length
        if (length == Int.MIN_VALUE) length = buffer.size
        content = ByteArray(length)
        for (index in 0 until length) {
            content!![index] = buffer[index]
        }
    }

    @Throws(IOException::class, HttpMessageParseException::class)
    override fun populateFromIncomingStream(rawInputStream: BufferedInputStream, multipartBoundary: String?) {
        var delimiterIndex = 0
        val delimiter = multipartBoundary!!.toByteArray()
        val buffer = ArrayList<Byte>()
        var current: Int = Int.MIN_VALUE
        while (current != -1) {
            current = if (rawInputStream.available() < 1) {
                -1
            } else {
                rawInputStream.read()
            }
            if (current != -1) {
                val currentByte = current.toByte()
                buffer.add(currentByte)
                if (currentByte == delimiter[delimiterIndex]) {
                    delimiterIndex++
                    if (delimiterIndex > delimiter.size) {
                        // we have a match This is a multipart boundary
                        takeContent(buffer, buffer.size - delimiter.size)
                    }
                } else {
                    delimiterIndex = 0
                }
            }
        }
        takeContent(buffer, Int.MIN_VALUE)
    }

    override val isEmpty: Boolean
        get() {
            if (content == null) return true
            return if (content!!.size < 1) true else false
        }

    //Need to subtract 1???
    override val contentLength: Int
        get() = content!!.size //Need to subtract 1???
}