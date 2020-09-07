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

package hoodland.opensource.memoir

import java.util.*

// Based on https://stackoverflow.com/questions/8571501/how-to-check-whether-a-string-is-base64-encoded-or-not
private val base64Check = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$".toRegex()
private val base64Decoder = Base64.getDecoder()

fun isBase64(candidate: String): Boolean {
    // TODO: Is there a more accurate check possible?. It is detecting Base64 where such is not the case.
    return base64Check.matches(candidate)
}

fun fromBase64(candidate: String): String {
    return String(base64Decoder.decode(candidate))
}

fun treatAsCode(value: String): String {
    return "<pre><code><xmp>$value</xmp></code></pre>"
}

// It is left up to the end user as to when a field should be base64 decoded or pretty-printed.
fun processString(fieldName: String, fieldValue: String, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null): String {
    var result = String(fieldValue.toCharArray()) // Deep copy

    callbackFunction?.let{
        result = it(fieldName, result)
    }

    //result = result.replace("<", "&lt;").replace(">", "&gt;")
    return result
}