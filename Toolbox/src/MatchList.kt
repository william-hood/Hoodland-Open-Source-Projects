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

package hoodland.opensource.toolbox

import java.util.*


class MatchList(vararg matchStrings: String) : ArrayList<String>() {

    init {
        this.addAll(matchStrings)
    }

    constructor(arrayListOfStrings: ArrayList<String>): this(*(arrayListOfStrings.toTypedArray())) { }

    /**
     * matches: Determines if the supplied candidate string is a match for any string contained in the list.
     *
     * @param candidateString The string to check if in the list.
     * @return Returns true if the supplied candidateString is a partial, case-specific match, for at least one string in the list. If candidateString is a substring of one in the list, it matches. Returns false if no match is found.
     */
    fun matches(candidateString: String): Boolean {
        for (thisListedString in this) {
            if (candidateString.contains(thisListedString)) return true
        }
        return false
    }

    /**
     * matchesCaseInspecific: Determines if the supplied candidate string is a match for any string contained in the list, regardless of case.
     *
     * @param candidateString The string to check if in the list.
     * @return Returns true if the supplied candidateString is a match, regardless of uppercase/lowercase, for at least one string in the list. If candidateString is a substring of one in the list, it matches. Returns false if no match is found.
     */
    fun matchesCaseInspecific(candidateString: String): Boolean {
        for (thisListedString in this) {
            if (candidateString.contains(thisListedString, true)) return true
        }
        return false
    }

    /**
     * containsCaseInspecific: Determines if the supplied candidate string is contained in the list, regardless of case.
     *
     * @param candidateString The string to check if in the list.
     * @return Returns true if the supplied candidateString is a full match, regardless of uppercase/lowercase, for at least one string in the list. If candidateString is a substring of one in the list, it DOES NOT match. Returns false if no match is found.
     */
    fun containsCaseInspecific(candidateString: String): Boolean {
        for (thisListedString in this) {
            if (thisListedString.equals(candidateString, ignoreCase = true)) return true
        }
        return false
    }
}
