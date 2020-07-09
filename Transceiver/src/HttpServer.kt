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
import rockabilly.memoir.ShowHttpRequest
import rockabilly.memoir.ShowHttpResponse
import rockabilly.memoir.ShowThrowable
import rockabilly.toolbox.depictFailure
import rockabilly.toolbox.stderr
import rockabilly.toolbox.stdout
import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.util.concurrent.CountDownLatch

internal const val SERVER_NAME = "Rockabilly Transceiver HTTP Server"
private const val DISCONTINUE_WARNING = "$SERVER_NAME: DISCONTINUING SERVICE NOW"

abstract class HttpServer(log: Memoir? = null) : Thread() {
    protected abstract fun handle(incomingRequest: HttpRequest?): HttpResponse
    private var listeningSocket: ServerSocket? = null
    private var block: CountDownLatch? = null
    private var memoir = log

    init {
        if (memoir == null) {
            memoir = Memoir("$SERVER_NAME", forPlainText = stdout)
        }
    }

    val isServing: Boolean
        get() = if (block == null) false else continueService()

    protected fun continueService(): Boolean {
        return block!!.count > 0
    }

    @Throws(IOException::class)
    fun setupService(port: Int, connectionBacklog: Int, localAddress: InetAddress?) {
        listeningSocket = ServerSocket(port, connectionBacklog, localAddress)
        block = null
    }

    @Throws(IOException::class)
    fun setupService(port: Int) {
        listeningSocket = ServerSocket(port)
        block = null
    }

    fun discontinueService() {
        if (memoir != null) {
            memoir!!.Info(DISCONTINUE_WARNING)
        } else {
            stderr.println(DISCONTINUE_WARNING)
        }

        while (block!!.count > 0) {
            try {
                block!!.countDown()
            } catch (dontCare: Exception) {
                // DELIBERATE NO-OP
            }
        }
        while (!isInterrupted) {
            try {
                interrupt()
            } catch (dontCare: Exception) {
                // DELIBERATE NO-OP
            }
        }
    }

    fun waitWhileServing() {

        // Busy loop below is deliberate;
        // block will only be null in the instant
        // before service begins.
        while (block == null)

        if (!continueService()) return
        try {
            block!!.await()
        } catch (e: InterruptedException) {
            // DELIBERATE NO-OP
        }
    }

    val serviceUrl: String
        get() = "http://" + listeningSocket!!.inetAddress.hostName + ":" + listeningSocket!!.localPort

    fun describeService(): String {
        return "Listening for connections on port " + listeningSocket!!.localPort
    }

    override fun run() {
        block = CountDownLatch(1)
        var transactionLog: Memoir? = null
        while (continueService()) {
            try {
                val connectedSocket = listeningSocket!!.accept()
                val inputStream = BufferedInputStream(connectedSocket.getInputStream())
                val incomingRequest = HttpRequest()
                incomingRequest.populateFromIncomingStream(inputStream)

                if (memoir != null) {
                    transactionLog = Memoir("${connectedSocket.inetAddress.hostName}: ${incomingRequest.verb} ${incomingRequest.uRL}")
                    transactionLog.ShowHttpRequest(incomingRequest.verb.toString(), incomingRequest.uRL.toString(), incomingRequest.headers, incomingRequest.payload.toString(), incomingRequest.toString())
                }

                val outgoingResponse = handle(incomingRequest)

                if (transactionLog != null) {
                    transactionLog.ShowHttpResponse(outgoingResponse.statusCode, outgoingResponse.headers, outgoingResponse.payload.toString(), outgoingResponse.toString())
                }

                val outputStream = DataOutputStream(connectedSocket.getOutputStream())
                outgoingResponse.sendToOutgoingStream(outputStream)
                outputStream.flush()
                connectedSocket.close()
            } catch (caughtFailure: Throwable) {
                if (memoir != null) {
                    if (transactionLog != null) {
                        transactionLog.ShowThrowable(caughtFailure)
                    } else {
                        memoir!!.ShowThrowable(caughtFailure)
                    }
                } else {
                    stderr.println(depictFailure(caughtFailure))
                }
            } finally {
                if (transactionLog != null) {
                    if (memoir != null) {
                        memoir!!.ShowMemoir(transactionLog)
                    }
                }
            }
        }
    }
}