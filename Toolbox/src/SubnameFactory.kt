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

// Based on http://en.wikipedia.org/wiki/Hexavigesimal
class SubnameFactory(startingIndex: Long = 0, totalPlaces: Int = 0) {
    var currentIndex: Long = startingIndex
        private set
    var places = totalPlaces
    var placeholder = DEFAULT_PLACE_HOLDER

    val nextIndexAsString: String
        get() {
            advance()
            return currentIndexAsString
        }

    val currentIndexAsString: String
        get() = prepend(currentIndex.toString())

    val nextIndex: Long
        get() {
            advance()
            return currentIndex
        }

    fun advance() {
        currentIndex++
    }

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
