// Copyright (c) 2020, 2023 William Arthur Hood
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

// Based on http://en.wikipedia.org/wiki/Hexavigesimal

/**
 * SubnameFactory is used in cases where a lot of records (or fields, test cases, etc.) need to have the same name,
 * with a unique suffix. MyTestA, MyTestB, etc. Use this to generate the suffix when programmatically generating
 * several similar items. (In the Koarse Grind test framework, this is recommended when creating manufactured
 * tests with a test factory.
 *
 * @constructor
 *
 * @param startingIndex Each subname is a Base26 number with a corresponding long integer. Zero is A. Unless you know why you'd want to change it, omit this parameter so it uses the default value of zero.
 * @param totalPlaces When rendering the subname as a string, use this to force the subname to take a minimum of <totalPlaces> characters. The default placeholder is '.', but you can change it after construction.
 */
class SubnameFactory(startingIndex: Long = 0, totalPlaces: Int = 0) {
    var currentIndex: Long = startingIndex
        private set
    var places = totalPlaces
    var placeholder = DEFAULT_PLACE_HOLDER

    /**
     * nextIndexAsString: This advances the index and produces the next Long-Integer index value as a string. (This is the numeric value, not the Base26 subname.)
     */
    val nextIndexAsString: String
        get() {
            advance()
            return currentIndexAsString
        }

    /**
     * currentIndexAsString: This produces the current Long-Integer index value as a string without advancing the index. (This is the numeric value, not the Base26 subname.)
     */
    val currentIndexAsString: String
        get() = prepend(currentIndex.toString())

    /**
     * nextIndex: This advances the index and produces the next Long-Integer index value. (This is the numeric value, not the Base26 subname.)
     */
    val nextIndex: Long
        get() {
            advance()
            return currentIndex
        }

    /**
     * advance: Advances the index but returns nothing. Use this to skip a subname if necessary.
     */
    fun advance() {
        currentIndex++
    }

    /**
     * nextSubname: This advances the index and produces the next Base26 subname.
     */
    val nextSubname: String
        get() {
            advance()
            return currentSubname
        }

    private fun prepend(result: String): String {
        var prefix = ""
        if (places > 0) {
            if (result.length < places) prefix = createStringFromBasisCharacter(placeholder, places - result.length)
        }
        return prefix + result
    }

    /**
     * currentSubname: This produces the current Base26 subname without advancing the index.
     */
    val currentSubname: String
        get() {
            val result = StringBuffer()
            var cursor = currentIndex
            while (cursor > 0) {
                --cursor
                result.append(('A'.toLong() + cursor % 26).toChar())
                cursor /= 26
            }
            return prepend(result.reverse().toString())
        }

    companion object {
        private const val DEFAULT_PLACE_HOLDER = '.'
    }
}
