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

enum class VerticalJustification {
    TOP, CENTER, BOTTOM
}

enum class HorizontalJustification {
    LEFT, CENTER, RIGHT
}

fun createStringFromBasisCharacter(basisChar: Char, length: Int): String {
    val tempCharArray = CharArray(length)
    Arrays.fill(tempCharArray, basisChar)
    return String(tempCharArray)
}

fun repeatString(target: String, count: Int): String {
    val result = StringBuilder()

    for (index in 0 until count) {
        result.append(target)
    }

    return result.toString()
}

fun getDimensional(x: Int, y: Int): String {
    return "($x x $y)"
}

// Reverse is not needed. Built into Kotlin.
// IsBlank is not needed. Built into Kotlin.

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

fun String?.matchesCaseInspecific(theOther: String?): Boolean {
    if (this == null) {
        if (theOther == null) {
            return true
        }

        return false
    }

    if (theOther == null) return false

    return this.toUpperCase().compareTo(theOther.toUpperCase()) == 0
}

fun String.makeParenthetic(): String {
    return "($this)"
}

fun String.makeQuoted(): String {
    return "\"$this\""
}

fun String.makeSingleQuoted(): String {
    return "'$this'"
}

fun String.removeCarriageReturns(): String {
    return this.replace("\r", "")
}


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

fun String.prependEveryLineWith(prependString: String): String? {
    return prependString + this.replace(CRLF, CRLF + prependString)
}

fun String.indentEveryLineBy(indentSize: Int = 5): String {
    return createStringFromBasisCharacter(' ', indentSize) + this
}

fun String.padSides(totalSize: Int, paddingChar: Char = ' '): String? {
    if (this.length >= totalSize) {
        return this
    }
    val whiteSpace: String = createStringFromBasisCharacter(paddingChar, (totalSize - this.length) / 2)
    val padCenteredString = whiteSpace + this + whiteSpace

    // It is possible to have this come out to one short of the desired
    // length. In that case, pad the extra char on the right
    return padCenteredString.padRight(totalSize, paddingChar)
}

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

fun String.padLeft(totalWidth: Int, paddingChar: Char = ' '): String? {
    return this.padEither(paddingChar, totalWidth, PaddingSide.LEFT)
}

fun String.padRight(totalWidth: Int, paddingChar: Char = ' '): String? {
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

