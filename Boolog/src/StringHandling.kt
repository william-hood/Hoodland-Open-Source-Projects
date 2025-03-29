// Copyright (c) 2020, 2023, 2025 William Arthur Hood
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

package hoodland.opensource.boolog

import java.util.*

// Based on https://stackoverflow.com/questions/8571501/how-to-check-whether-a-string-is-base64-encoded-or-not
private val base64Check = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$".toRegex()
private val base64Decoder = Base64.getDecoder()

/**
 * isBase64: This function attempts to determine if a given string can be decoded as Base64. PROBLEM: There appear to
 * be some small english strings that technically can decode as Base64 (producing garbage).
 *
 * @param candidate The string to check if it looks like Base64
 * @return Returns true if the candidate string appears to be Base64.
 */
fun isBase64(candidate: String): Boolean {
    // TODO: Is there a more accurate check possible?. It is detecting Base64 where such is not the case.
    // Apparently there are some simple english words that legally decrypt from Base64 to garbage.
    return base64Check.matches(candidate)
}

/**
 * fromBase64: Decodes a Base64 encoded string
 *
 * @param candidate The string to treat as Base64.
 * @return Returns the resulting decoded string.
 */
fun fromBase64(candidate: String): String {
    return String(base64Decoder.decode(candidate))
}

/**
 * treatAsCode: Wraps the given string with HTML tags that prevent rendering of any HTML by the browser and
 * display the value as a code sample.
 *
 * @param value The string value to be wrapped as a code sample.
 * @return The wrapped string. Specifically "<pre><code><xmp>$value</xmp></code></pre>".
 */
fun treatAsCode(value: String): String {
    return "<pre><code><xmp>$value</xmp></code></pre>"
}

// It is left up to the end user as to when a field should be base64 decoded or pretty-printed.
/**
 * processString: This will be called for nearly every message or string field that Boolog tries to log. Any processing
 * necessary before actually logging the string to HTML occurs here. In certain cases, particularly headers and body
 * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
 * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
 * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
 * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
 * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
 * supply a callbackFunction if none is needed.
 *
 * @param fieldName The name of the field being processed.
 * @param fieldValue The actual string value being processed.
 * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON.
 * @return Returns the string after processing.
 */
fun processString(fieldName: String, fieldValue: String, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null): String {
    var result = String(fieldValue.toCharArray()) // Deep copy

    callbackFunction?.let{
        result = it(fieldName, result)
    }

    //result = result.replace("<", "&lt;").replace(">", "&gt;")
    return result
}