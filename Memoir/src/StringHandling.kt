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

package rockabilly.memoir

import java.util.*

// Based on https://stackoverflow.com/questions/8571501/how-to-check-whether-a-string-is-base64-encoded-or-not
private val base64Check = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$".toRegex()
private val base64Decoder = Base64.getDecoder()

fun IsBase64(candidate: String): Boolean {
    return false
    // TODO: Make this correct. It is detecting Base64 where such is not the case.
    //return base64Check.matches(candidate)
}

fun FromBase64(candidate: String): String {
    return String(base64Decoder.decode(candidate))
}


// Java does not have a native JSON parser. Kotlin has the kotlinx stuff, but that is Apache license and
// adds a dependency. Will think on this a bit, but may have to implement a simple JSON parser. Easiest
// way to pretty-print JSON may be to read it into an object, then render that object as pretty json.

fun ProcessString(candidate: String, treatAsCode: Boolean = false): String {
    var result = candidate

    // If it's Base64, decode it.
    if (IsBase64(result)) {
        result = FromBase64(result)
    }

    // If it's JSON, pretty-print it.
    // result = readJSON(result).prettyPrint()
    // This should also force treatAsCode to be true

    if (treatAsCode) {
        result = result.replace("<", "&lt;").replace(">", "&gt;")
    }

    return result
}