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
import rockabilly.toolbox.depictFailure
import rockabilly.toolbox.readLineFromInputStream
import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class HttpRequest() : HttpMessage(), Transceivable {
    var verb = HttpVerb.GET
    var uRL: URL? = null

    val isSecure: Boolean
    get() = uRL.toString().toLowerCase().startsWith("https")

    constructor(httpVerb: HttpVerb, url: URL, body: HttpPayload<*>? = null): this() {
        verb = httpVerb
        uRL = url
        payload = body
    }

    constructor(httpVerb: HttpVerb, url: String, body: HttpPayload<*>? = null) : this(httpVerb, URL(url), body) {}

    @Throws(MalformedURLException::class)
    fun setURL(url: String?) {
        uRL = URL(url)
    }

    override fun toString(): String {
        val result = StringBuilder()
        result.append(uRL.toString())
        result.append(CarriageReturnLineFeed)
        result.append(verb.toString() + " " + uRL!!.path + " " + PROTOCOL_AND_VERSION)
        result.append(CarriageReturnLineFeed)
        result.append(super.toString())
        return result.toString()
    }

    override fun sendToOutgoingStream(outputStream: DataOutputStream) {
        headers.clobber("HOST", uRL!!.host)
        if (verb === HttpVerb.POST) headers.clobber("Content-Length", "" + payload!!.contentLength)
        outputStream.writeBytes("$verb ${uRL!!.path} $PROTOCOL_AND_VERSION$CarriageReturnLineFeed")
        super.sendToOutgoingStream(outputStream)
    }

    override fun populateFromIncomingStream(inputStream: BufferedInputStream, multipartBoundary: String?) {
        var thisLine: String? = ""
        var firstLineParts: Array<String>? = thisLine!!.trim { it <= ' ' }.split("\\s+".toRegex()).toTypedArray()

        // handle first line
        do {
            thisLine = readLineFromInputStream(inputStream)
            firstLineParts = thisLine.trim { it <= ' ' }.split("\\s+".toRegex()).toTypedArray()
        } while (thisLine!!.length == 0 && firstLineParts!!.size < 3)

        // Create the basic HTTP Request object
        var verbString: String = UnsetString
        var protocol: String = UnsetString
        var path: String = UnsetString

        verbString = try {
            firstLineParts!![0]
        } catch (thisException: Exception) {
            // DELIBERATE NO-OP
            System.err.println("This line ==$thisLine")
            System.err.println("This line size ==" + thisLine.length)
            System.err.println(depictFailure(thisException))
            throw thisException
        }

        verb = verbString.toHttpVerb()

        protocol = try {
            firstLineParts[2].split("/".toRegex()).toTypedArray()[0]
        } catch (thisException: Exception) {
            // DELIBERATE NO-OP
            System.err.println("This line ==$thisLine")
            System.err.println("This line size ==" + thisLine.length)
            System.err.println(depictFailure(thisException))
            throw thisException
        }

        path = try {
            firstLineParts[1]
        } catch (thisException: Exception) {
            // DELIBERATE NO-OP
            System.err.println("This line ==$thisLine")
            System.err.println("This line size ==" + thisLine.length)
            System.err.println(depictFailure(thisException))
            throw thisException
        }

        thisLine = null
        firstLineParts = null

        // Add headers
        headers.populateFromIncomingStream(inputStream)

        for ((key, value) in headers) {
            if (key.toLowerCase().startsWith("host")) {
                // Java version had this in a try catch and would print the error.
                // Design for the Kotlin version is that errors should be handled/logged by the server, client, or other client code.
                //result.setURL(protocol + "://" + thisHeader.getValue().substring(thisHeader.getValue().indexOf("://") + 3) + path);
                setURL("$protocol://$value$path")
                break
            }
        }
    }
}