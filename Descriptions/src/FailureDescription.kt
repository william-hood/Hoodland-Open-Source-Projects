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

package rockabilly.descriptions

import rockabilly.toolbox.UnsetString

class FailureDescription {
    private var FailureType: Class<out Throwable>? = null
    var MessageSubstring = ""
    var Cause: FailureDescription? = null
    private var FailureTypePartialName: String = UnsetString

    constructor(failureType: Class<out Throwable>?,
                messageSubstring: String) {
        FailureType = failureType
        MessageSubstring = messageSubstring
    }

    constructor(failureType: Class<out Throwable>?,
                messageSubstring: String, cause: FailureDescription?) : this(failureType, messageSubstring) {
        Cause = cause
    }

    constructor(failureTypePartial: String, messageSubstring: String) {
        FailureTypePartialName = failureTypePartial
        MessageSubstring = messageSubstring
    }

    private fun messageMatches(candidate: String?): Boolean {
        if (MessageSubstring.length > 0) {
            if (!candidate!!.contains(MessageSubstring)) return false
        }
        return true
    }

    fun isMatch(candidateFailure: Throwable): Boolean {
        if (FailureTypePartialName !== UnsetString) return isMatch(candidateFailure.javaClass.canonicalName,
                candidateFailure.message)
        if (candidateFailure.javaClass != FailureType) return false
        if (!messageMatches(candidateFailure.message)) return false
        return if (Cause != null) Cause!!.isMatch(candidateFailure) else true
    }

    fun isMatch(candidateFailureName: String,
                candidateFailureMessage: String?): Boolean {
        if (!candidateFailureName.contains(FailureTypePartialName)) return false
        return if (!messageMatches(candidateFailureMessage)) false else true
    }

    override fun toString(): String {
        val result = StringBuilder()
        if (FailureTypePartialName === UnsetString) {
            result.append("failure of type " + FailureType.toString())
        } else {
            result.append("failure with type name containing \""
                    + FailureTypePartialName + "\"")
        }
        if (MessageSubstring.length > 0) result.append(" with message containing \"" + MessageSubstring
                + "\"")
        if (Cause != null) result.append("; Caused by " + Cause.toString())
        return result.toString()
    }
}