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

import java.util.*

enum class VerticalJustification {
    TOP, CENTER, BOTTOM
}

enum class HorizontalJustification {
    LEFT, CENTER, RIGHT
}

/**
 * createStringFromBasisCharacter: Creates a string composed of the requested number of the
 * basis character in a row.
 * @param basisChar The character to use. Every character in the string will be this.
 * @param length The total number of characters in the string.
 * @return A string consisting of exactly (length) characters in a row, all of which are the basis character.
 */
fun createStringFromBasisCharacter(basisChar: Char, length: Int): String {
    val tempCharArray = CharArray(length)
    Arrays.fill(tempCharArray, basisChar)
    return String(tempCharArray)
}

/**
 * repeatString: Creates a new string with the requested number of the string it is given.
 * @param target The basis string which will be repeated.
 * @param count The number of times to repeat the target string.
 * @return A new string composed of exactly (count) copies of the (target) string.
 */
fun repeatString(target: String, count: Int): String {
    val result = StringBuilder()

    for (index in 0 until count) {
        result.append(target)
    }

    return result.toString()
}

/**
 * getDimensional
 * @param x
 * @param y
 * @return Given x and y returns "(x x y)"
 */
fun getDimensional(x: Int, y: Int): String {
    return "($x x $y)"
}

/**
 * String?.matches: Compares strings, even if one or both are null.
 * @param theOther
 * @return true if both strings are null or if both are exactly the same content. false otherwise.
 */
fun String?.matches(theOther: String?): Boolean {
    if (this == null) {
        if (theOther == null) {
            return true
        }

        return false
    }

    if (theOther == null) return false

    return this.compareTo(theOther) == 0
}

/**
 * String?.matchesCaseInspecific: Compares strings without considering upper or lower case, even if one or both are null.
 * @param theOther
 * @return true if both strings are null or if both are exactly the same content without distinguishing between upper and lower case. false otherwise.
 */
fun String?.matchesCaseInspecific(theOther: String?): Boolean {
    if (this == null) {
        if (theOther == null) {
            return true
        }

        return false
    }

    if (theOther == null) return false

    return this.uppercase().compareTo(theOther.uppercase()) == 0
}

/**
 * Encloses the target string in parentheses. Given "this string" returns "(this string)".
 * @return the target string in parentheses.
 */
fun String.makeParenthetic(): String {
    return "($this)"
}

/**
 * Encloses the target string in double quotes.
 * @return the target string enclosed in double quotes.
 */
fun String.makeQuoted(): String {
    return "\"$this\""
}

/**
 * Encloses the target string in single quotes.
 * @return the target string enclosed in single quotes.
 */
fun String.makeSingleQuoted(): String {
    return "'$this'"
}

/**
 * removeCarriageReturns: Removes all carriage returns (\r) from the target string.
 * DOES NOT ALSO REMOVE LINE FEED (\n) CHARACTERS. This function can be useful for
 * changing Windows-style endlines (\r\n) into Unix/Linux endlines (just \n).
 * @return The target string without any carriage return characters.
 */
fun String.removeCarriageReturns(): String {
    return this.replace("\r", "")
}

/**
 * justify: Takes the target string and adds spaces and "Carriage Return/Line Feed" (Windows style)
 * endlines to produce a rendition justified to the given number of columns.
 * If columns are not specified, the default value of 75 is used.
 * Windows-style endlines are used because macOS and Linux handle them without issue.
 * @param columns The number of columns to justify to.
 * @return The original string rendered as justified to the specified number of columns.
 */
fun String.justify(columns: Int = 75): String {
    var justifiedMessage = ""

    // Before splitting the message into words, illegal characters must be
    // stripped out.
    val strippedMessageArray = this.split("\n\t\r\b".toRegex()).toTypedArray() // \f also in the Java version// \v \a also in the C# version
    var strippedMessage = ""
    for (cursor in strippedMessageArray.indices) {
        if (strippedMessageArray[cursor].length > 0) {
            if (!strippedMessageArray[cursor].endsWith(" ")) {
                strippedMessageArray[cursor] += " "
            }
        }
        strippedMessage += strippedMessageArray[cursor]
    }
    val parsedMessage = strippedMessage.split(" ".toRegex()).toTypedArray()
    var columnIndex = 0
    for (cursor in parsedMessage.indices) {
        if (parsedMessage[cursor].length >= columns) {
            parsedMessage[cursor] = parsedMessage[cursor].substring(0,
                    columns - 1)
        }
        parsedMessage[cursor] += " "
        if (columnIndex + parsedMessage[cursor].length > columns) {
            // This word will exceed the number of columns. Add a line break
            // before the word.
            justifiedMessage += CRLF
            columnIndex = 0
        }
        justifiedMessage += parsedMessage[cursor]
        columnIndex += parsedMessage[cursor].length
    }
    return justifiedMessage
}

/**
 * prependEveryLineWith: Used to add a string to the beginning of every line in the target string.
 * THIS ASSUMES WINDOWS STYLE LINE ENDINGS (\r\n)
 * @param prependString This string to prepend every line with.
 * @return The target string with every line prepended by the requested string (prependString).
 */
fun String.prependEveryLineWith(prependString: String): String {
    return prependString + this.replace(CRLF, CRLF + prependString)
}

/**
 * indentEveryLineBy: Every line of the target string is prepended by (indentSize) space characters.
 * @param indentSize The number of spaces to indent each line by. Default is 5.
 * @return The target string rendered with each line indented.
 */
fun String.indentEveryLineBy(indentSize: Int = 5): String {
    val result: String = this ?: ""

    val indent = createStringFromBasisCharacter(' ', indentSize)
    return this.prependEveryLineWith(result)
}

/**
 * padSides: Takes the target string and pads both sides with enough of the padding
 * character to make (totalSize) columns. This assumes the target string to be one line.
 * @param totalSize The total number of columns wide the string should be.
 * @param paddingChar The character to use for padding. The default is a blank space (' ').
 * @return The target string padded on both sides to make it (totalSize) characters wide.
 */
fun String.padSides(totalSize: Int, paddingChar: Char = ' '): String {
    if (this.length >= totalSize) {
        return this
    }
    val whiteSpace: String = createStringFromBasisCharacter(paddingChar, (totalSize - this.length) / 2)
    val padCenteredString = whiteSpace + this + whiteSpace

    // It is possible to have this come out to one short of the desired
    // length. In that case, pad the extra char on the right
    return padCenteredString.padRight(totalSize, paddingChar)
}

/**
 * padVertical: Pads the target multi-line string with vertical rows at the top, bottom, or both.
 * @param justification One of the three VerticalJustification enum values: TOP, CENTER (default), or BOTTOM
 * @param totalRows The total number of rows that should be in the message.
 * @param addHorizontalSpaces Defaults to false. Set to true to add horizontal spaces to make every line the same width.
 * @return The target string padded vertically as requested.
 */
fun String.padVertical(totalRows: Int, justification: VerticalJustification = VerticalJustification.CENTER, addHorizontalSpaces: Boolean = false): String {
    var message = this
    var paddedMessage: String = message
    message = this.removeCarriageReturns()
    val splitMessage = message.split("\n".toRegex()).toTypedArray()
    if (totalRows <= splitMessage.size) {
        return paddedMessage
    }
    var emptyLine: String = CRLF
    if (addHorizontalSpaces) {
        var maxHorz = 0
        for (cursor in splitMessage.indices) {
            if (splitMessage[cursor].length > maxHorz) {
                maxHorz = splitMessage[cursor].length
            }
        }
        emptyLine = createStringFromBasisCharacter(' ', maxHorz) + CRLF
    }

    paddedMessage = ""
    var topPadding = when (justification) {
        VerticalJustification.BOTTOM -> totalRows - splitMessage.size
        VerticalJustification.TOP -> 0
        else -> (totalRows - splitMessage.size) / 2
    }
    var pads = topPadding
    for (cursor in 0 until pads) {
        paddedMessage += emptyLine
    }
    for (cursor in splitMessage.indices) {
        paddedMessage += splitMessage[cursor] + CRLF
        pads++
    }
    while (pads < totalRows) {
        paddedMessage += emptyLine
        pads++
    }
    // Deliberate
    // for (pads = pads; pads < totalRows; pads++)
    // {
    // paddedMessage += emptyLine;
    // }
    return paddedMessage
}

/**
 * padLeft: Pads the target string on the left with the specified character to the total width of columns requested.
 * This assumes the target string to be one line.
 * @param totalWidth The total width the string should be after padding.
 * @param paddingChar The character to use for padding. Default is a blank space (' ').
 * @return The padded string as specified.
 */
fun String.padLeft(totalWidth: Int, paddingChar: Char = ' '): String {
    return this.padEither(paddingChar, totalWidth, PaddingSide.LEFT)
}

/**
 * padRight: Pads the target string on the right with the specified character to the total width of columns requested.
 * This assumes the target string to be one line.
 * @param totalWidth The total width the string should be after padding.
 * @param paddingChar The character to use for padding. Default is a blank space (' ').
 * @return The padded string as specified.
 */
fun String.padRight(totalWidth: Int, paddingChar: Char = ' '): String {
    return this.padEither(paddingChar, totalWidth, PaddingSide.RIGHT)
}

private enum class PaddingSide {
    LEFT, RIGHT
}

private fun String.padEither(paddingChar: Char, totalWidth: Int, which: PaddingSide): String {
    val result = StringBuilder()
    result.append(this)
    while (result.length < totalWidth) {
        if (which == PaddingSide.LEFT) {
            result.insert(0, paddingChar)
        } else {
            result.append(paddingChar)
        }
    }
    return result.toString()
}

