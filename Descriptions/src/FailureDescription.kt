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

import rockabilly.toolbox.UNSET_STRING

class FailureDescription {
    private var _failureType: Class<out Throwable>? = null
    private var _messageSubstring = ""
    private var _cause: FailureDescription? = null
    private var _failureTypePartialName: String = UNSET_STRING

    constructor(failureType: Class<out Throwable>?,
                messageSubstring: String) {
        this._failureType = failureType
        this._messageSubstring = messageSubstring
    }

    constructor(failureType: Class<out Throwable>?,
                messageSubstring: String, cause: FailureDescription?) : this(failureType, messageSubstring) {
        this._cause = cause
    }

    constructor(failureTypePartialName: String, messageSubstring: String) {
        _failureTypePartialName = failureTypePartialName
        this._messageSubstring = messageSubstring
    }

    private fun messageMatches(candidate: String?): Boolean {
        if (_messageSubstring.length > 0) {
            if (!candidate!!.contains(_messageSubstring)) return false
        }
        return true
    }

    fun isMatch(candidateFailure: Throwable): Boolean {
        if (_failureTypePartialName !== UNSET_STRING) return isMatch(candidateFailure.javaClass.canonicalName,
                candidateFailure.message)
        if (candidateFailure.javaClass != _failureType) return false
        if (!messageMatches(candidateFailure.message)) return false
        return if (_cause != null) _cause!!.isMatch(candidateFailure) else true
    }

    fun isMatch(candidateFailureName: String, candidateFailureMessage: String?): Boolean {
        if (!candidateFailureName.contains(_failureTypePartialName)) return false
        return if (!messageMatches(candidateFailureMessage)) false else true
    }

    override fun toString(): String {
        val result = StringBuilder()
        if (_failureTypePartialName === UNSET_STRING) {
            result.append("failure of type " + _failureType.toString())
        } else {
            result.append("failure with type name containing \""
                    + _failureTypePartialName + "\"")
        }
        if (_messageSubstring.length > 0) result.append(" with message containing \"" + _messageSubstring
                + "\"")
        if (_cause != null) result.append("; Caused by " + _cause.toString())
        return result.toString()
    }
}