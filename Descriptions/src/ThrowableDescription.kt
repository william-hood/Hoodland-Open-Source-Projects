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

package hoodland.opensource.descriptions

class ThrowableDescription(
        val throwableTypePartialName: String,
        val messageSubString: String = "",
        val cause: ThrowableDescription? = null
) {
    private fun messageMatches(candidate: String): Boolean {
        if (messageSubString.length > 0) {
            return candidate.contains(messageSubString)
        }

        return true
    }

    fun isMatch(candidateFailure: Throwable): Boolean {
        if (! candidateFailure.javaClass.canonicalName.contains(throwableTypePartialName)) return false

        candidateFailure.message?.let {
            if (! messageMatches(it)) return false
        }

        cause?.let {
            candidateFailure.cause?.let { causalThrowable ->
                return it.isMatch(causalThrowable)
            }

            return false
        }

        return true
    }

    override fun toString(): String {
        val result = StringBuilder()
        result.append("failure with type name containing \"$throwableTypePartialName\"")
        if (messageSubString.length > 0) result.append(" with message containing \"$messageSubString\"")
        cause?.let {
            result.append("; Caused by $it.toString()")
        }

        return result.toString()
    }
}