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

import rockabilly.common.Foundation
import rockabilly.common.Symbols
import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.io.IOException


class HttpStringPayload : HttpHeadersList(), HttpPayload<StringBuilder?> {
    override var content: StringBuilder? = StringBuilder()

    @Throws(IOException::class)
    override fun toOutgoingStream(outputStream: DataOutputStream?) {
        /*
		if (headers.size() > 0) {
			outputStream.writeBytes(headersToOutgoingDataString());
			outputStream.writeBytes("\r\n");
		}
		*/
        outputStream!!.writeBytes(content.toString())
    }

    override fun getContent(): StringBuilder? {
        return content
    }

    override fun setContent(newContent: StringBuilder?) {
        content!!.setLength(0)
        content = null
        System.gc()
        content = newContent
    }

    override fun toString(): String {
        val result = StringBuilder()
        if (headers.size() > 0) {
            result.append(super.toString())
            result.append(Symbols.divider())
            result.append(Symbols.CarriageReturnLineFeed)
            result.append(Symbols.CarriageReturnLineFeed)
        }
        result.append("TEXT CONTENT:")
        if (content == null) {
            result.append(" (null)")
        } else if (content!!.length < 1) {
            result.append(" (empty)")
        } else {
            result.append(Symbols.CarriageReturnLineFeed)
            result.append(content.toString())
        }
        return result.toString()
    }

    @Throws(IOException::class, HttpMessageParseException::class)
    override fun populateFromInputStream(rawInputStream: BufferedInputStream?, multipartBoundary: String?) {
        var thisLine = "DO NOT USE DEFAULT STRING"
        while (!Foundation.StringIsEmpty(thisLine.trim { it <= ' ' })) {
            thisLine = Foundation.readLineFromInputStream(rawInputStream)
            if (thisLine.trim { it <= ' ' } == multipartBoundary) return
            content!!.append(thisLine)
        }
    }

    override val isEmpty: Boolean
        get() = content!!.length < 1

    override val contentLength: Int
        get() = getContent()!!.length - 1
}