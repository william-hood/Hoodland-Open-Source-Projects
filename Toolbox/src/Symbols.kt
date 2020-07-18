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

enum class DividerTypes {
    SINGLE, DOUBLE
}

val PLATFORM_NEWLINE = System.getProperty("line.separator")
const val CRLF = "\r\n"
const val COPYRIGHT = '\u00a9'
const val REGISTERED_TM = '\u00ae'
const val YEN = '\u00a5'
const val POUND = '\u00a3'
const val CENT = '\u00a2'
const val DOLLAR = '$'
const val PARAGRAPH = '\u00b6'
const val OPEN_ANGLE_QUOTE = '\u00ab'
const val CLOSE_ANGLE_QUOTE = '\u00bb'
const val DEGREE = '\u00b0'
const val SPACE = ' '

const val DEFAULT_DIVIDER_LENGTH = 79
const val SINGLE_DIVIDER = '_'
const val DOUBLE_DIVIDER = '='

fun divider(typeToUse: DividerTypes = DividerTypes.SINGLE, length: Int = DEFAULT_DIVIDER_LENGTH): String? {
    var dividerChar = DOUBLE_DIVIDER
    var dividerString = ""
    if (typeToUse === DividerTypes.SINGLE) {
        dividerChar = SINGLE_DIVIDER
    }
    for (cursor in 0 until length) {
        dividerString += dividerChar
    }
    return dividerString
}

const val ERASER = '\u0008'

const val UNSET_STRING = "---UNSET---"