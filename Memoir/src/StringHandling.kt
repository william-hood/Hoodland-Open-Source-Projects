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

fun isBase64(candidate: String): Boolean {
    // TODO: Make this correct. It is detecting Base64 where such is not the case.
    return base64Check.matches(candidate)
}

fun fromBase64(candidate: String): String {
    return String(base64Decoder.decode(candidate))
}


// TODO
// In the future the plan is that this will automatically detect and decode base 64.
// It will then attempt to pretty-print any obvious JSON.
// Some issues with this...
//
// Both the algorithm here, and just seeing if decode succeeds in the older C# version,
// it turns out there are many plain english strings that are legal base64. (Apparently
// the word "whatever" is among them.) Automatic base64 decode can only happen if the
// string really is Base64, otherwise we present the end-user with garbage. One thought
// is that the Show*() functions might take a parameter to LOOK for Base64 and will
// otherwise ignore it.
//
// To the best I can determine there is not a "standard" JSON parser or pretty-printer
// for Java. That might change in the future. Kotlin does have the Kotlinx stuff,
// but I'm not keen with introducing either a dependency or a non-MIT license.
// I am possibly going to implement my own JSON parser as the easiest way to pretty-
// print JSON seems to involve parsing it first.
//
// The reason to base64 decode and pretty print JSON is that I've often encountered
// lengthy JSON in both Headers and payloads of REST call responses. Base64 decode
// can reveal the fields of a JWT and certain other tokens which can be very
// useful for QA and debugging.
fun processString(candidate: String, treatAsCode: Boolean = false): String {
    var result = candidate

    /*
    // If it's Base64, decode it.
    if (isBase64(result)) {
        result = fromBase64(result)
    }
     */

    // If it's JSON, pretty-print it.
    // result = readJSON(result).prettyPrint()
    // This should also force treatAsCode to be true

    if (treatAsCode) {
        result = result.replace("<", "&lt;").replace(">", "&gt;")
    }

    return result
}