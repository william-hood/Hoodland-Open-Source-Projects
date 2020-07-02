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

import rockabilly.memoir.Memoir
import rockabilly.memoir.ShowHttpResponse
import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.ArrayList
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

// In the Java version, which predated Memoir, there was a "Verbose" version
// which implemented a common interface with this class.
// TODO: Allow passing of a memoir to log messages to.
// TODO: If not passed a memoir, one will be created to log errors to files. (Replace to prints to stderr)

class HttpClient {
    @Throws(IOException::class, HttpMessageParseException::class)
    fun sendAndReceive(httpRequest: HttpRequest, memoir: Memoir? = null): HttpResponse {
        var outgoing: DataOutputStream? = null
        var incoming: BufferedInputStream? = null
        var socket: Socket? = null
        var secure = false
        if (httpRequest.uRL.toString().toLowerCase().startsWith("https")) secure = true
        var host = httpRequest.uRL!!.host
        var port = httpRequest.uRL!!.port
        if (port < 1 || port > 65535) {
            port = if (secure) {
                443
            } else {
                80
            }
        }
        return try {
            if (secure) {
                socket = SSLSocketFactory.getDefault().createSocket(host, port) as SSLSocket
            } else {
                socket = Socket()
                socket.connect(InetSocketAddress(host, port))
            }
            outgoing = DataOutputStream(socket!!.getOutputStream())
            incoming = BufferedInputStream(socket.getInputStream())
            httpRequest.toOutgoingStream(outgoing)
            //outgoing.close();
            outgoing.flush()
            val result = HttpResponse.fromInputStream(incoming)

            // TODO: Change the HTTP Header class to reflect reality.
            memoir.ShowHttpResponse(result.getStatusCode(), result.headers)
            result
        } finally {
            try {
                //incoming.close();
                socket!!.close()
            } finally {
                outgoing = null
                incoming = null
                host = null
                socket = null
            }
        }
    }
}
