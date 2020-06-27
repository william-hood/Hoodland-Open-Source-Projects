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

package rockabilly.toolbox

import java.util.*


class MatchList : ArrayList<String> {
    constructor() {}
    constructor(arrayListOfStrings: ArrayList<String>) {
        this.addAll(arrayListOfStrings)
    }

    constructor(arrayOfStrings: Array<String?>) {
        this.addAll(Arrays.asList<String>(*arrayOfStrings))
    }

    fun matches(candidateString: String): Boolean {
        for (thisListedString in this) {
            if (candidateString.contains(thisListedString)) return true
        }
        return false
    }

    /* ArrayList already has contains.
    fun contains(candidateString: String): Boolean {
        for (thisListedString in this) {
            if (thisListedString == candidateString) return true
        }
        return false
    }
    */

    fun matchesCaseInspecific(candidateString: String): Boolean {
        for (thisListedString in this) {
            if (candidateString.toUpperCase().contains(thisListedString.toUpperCase())) return true
        }
        return false
    }

    fun containsCaseInspecific(candidateString: String?): Boolean {
        for (thisListedString in this) {
            if (thisListedString.equals(candidateString, ignoreCase = true)) return true
        }
        return false
    }

    companion object {
        private const val serialVersionUID = -8871132198493596572L
    }
}