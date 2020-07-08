package rockabilly.transceiver

import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.io.IOException

interface Transceivable {
    @Throws(IOException::class)
    fun sendToOutgoingStream(outputStream: DataOutputStream)

    @Throws(IOException::class, HttpMessageParseException::class)
    fun populateFromIncomingStream(inputStream: BufferedInputStream, multipartBoundary: String? = null)
}