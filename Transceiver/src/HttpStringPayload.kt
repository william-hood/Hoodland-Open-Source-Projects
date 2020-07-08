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
import java.io.DataOutputStream
import java.io.IOException


class HttpStringPayload : HttpPayload<StringBuilder?>(), Transceivable {
    override var content: StringBuilder? = StringBuilder()

    @Throws(IOException::class)
    override fun sendToOutgoingStream(outputStream: DataOutputStream) {
        outputStream.writeBytes(content.toString())
    }

    // TODO: Handle any necessary nesting of toString() calls
    override fun toString(): String {
        val result = StringBuilder()
        /*
        if (headers.size > 0) {
            result.append(super.toString())
            result.append(divider())
            result.append(CarriageReturnLineFeed)
            result.append(CarriageReturnLineFeed)
        }

         */
        result.append("TEXT CONTENT:")
        if (content == null) {
            result.append(" (null)")
        } else if (content!!.length < 1) {
            result.append(" (empty)")
        } else {
            result.append(CarriageReturnLineFeed)
            result.append(content.toString())
        }
        return result.toString()
    }

    @Throws(IOException::class, HttpMessageParseException::class)
    override fun populateFromIncomingStream(rawInputStream: BufferedInputStream, multipartBoundary: String?) {
        var thisLine = UnsetString // Java version: "DO NOT USE DEFAULT STRING"
        while (!StringIsEmpty(thisLine.trim { it <= ' ' })) {
            thisLine = readLineFromInputStream(rawInputStream)
            if (thisLine.trim { it <= ' ' } == multipartBoundary) return
            content!!.append(thisLine)
        }
    }

    override val isEmpty: Boolean
        get() = content!!.length < 1

    override val contentLength: Int
        get() = content!!.length - 1
}