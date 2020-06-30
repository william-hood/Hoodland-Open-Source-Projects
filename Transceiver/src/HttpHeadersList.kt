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
import rockabilly.toolbox.StringIsEmpty
import rockabilly.toolbox.UnsetString
import rockabilly.toolbox.readLineFromInputStream
import java.io.BufferedInputStream
import java.io.IOException
import java.util.*


open class HttpHeadersList {
    val headers: ArrayList<HttpHeader> = ArrayList<HttpHeader>()
    var httpContent: HttpContent? = HttpContent()
    protected fun headersToOutgoingDataString(): String {
        val result = StringBuilder()
        if (httpContent != null) {
            if (httpContent!!.isSet) {
                headers.add(httpContent!!.toHttpHeader())
            }
        }
        if (!headers.isEmpty()) {
            for (thisHeader in headers) {
                result.append(thisHeader.toString())
                result.append("\r\n")
            }
            result.append("\r\n")
        }
        return result.toString()
    }

    override fun toString(): String {
        val result = StringBuilder()
        //if (! httpContent.toString().isEmpty()) result.append(httpContent.toHttpHeader().toString());;
        result.append(CarriageReturnLineFeed)
        for (thisHeader in headers) {
            result.append(thisHeader.toString())
            result.append(CarriageReturnLineFeed)
        }
        return result.toString()
    }

    @Throws(IOException::class, HttpMessageParseException::class)
    fun populateHeadersFromInputStream(inputStream: BufferedInputStream): String {
        // This assumes we've read in far enough to begin reading the headers
        // Returns the server
        var server: String = UnsetString
        try {
            // Read headers, Content Type, and Server.
            // (Date is left as a header and not a special field)
            var headerLine: String = readLineFromInputStream(inputStream)
            while (!StringIsEmpty(headerLine)) {

                // DEBUG
                //System.out.println(headerLine);
                // It appears that Java itself has a race condition (thanks Oracle)
                // Need to print, or otherwise access headerLine here or you
                // might get a mysterious nullref downstream. *sigh*
                if (WORK_AROUND_DELAY > 0) {
                    Thread.sleep(WORK_AROUND_DELAY.toLong())
                }
                val thisHeader: HttpHeader = HttpHeader.fromString(headerLine)
                if (thisHeader.key.equals(HttpContent.HEADER_KEY)) {
                    httpContent = HttpContent.fromHttpHeader(thisHeader)
                } else if (thisHeader.key.equals(HttpHeader.SERVER_HEADER_KEY)) {
                    server = thisHeader.value
                } else {
                    headers.add(thisHeader)
                }
                headerLine = readLineFromInputStream(inputStream)
            }
        } catch (causalException: Exception) {
            throw HttpMessageParseException(causalException)
        }


        // DEBUG
        //System.out.println(FX.divider());
        return server
    }

    companion object {
        var WORK_AROUND_DELAY = 0
    }
}