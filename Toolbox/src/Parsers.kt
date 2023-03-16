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

/**
 * Parsers should be used with a MatrixFile of the same type. Given a String, it converts it
 * to the appropriate data type.
 *
 * @param T The data type for the MatrixFile and Parser to use.
 */
interface Parser<T> {
    /**
     * parseMethod: Converts the supplied string to the specified data type.
     *
     * @param stringToParse The string to be converted. This is typically a single cell of data in a CSV (or other delimiter) file.
     * @return The data represented by the string as the appropriate type.
     */
    fun parseMethod(stringToParse: String): T
}

object IntParser : Parser<Int> {
    override fun parseMethod(stringToParse: String): Int {
        return stringToParse.toInt()
    }
}

object FloatParser : Parser<Float> {
    override fun parseMethod(stringToParse: String): Float {
        return stringToParse.toFloat()
    }
}

object StringParser : Parser<String> {
    override fun parseMethod(stringToParse: String): String {
        return stringToParse.trim()
    }
}