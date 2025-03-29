// Copyright (c) 2020, 2023, 2025 William Arthur Hood
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

package hoodland.opensource.boolog

import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass

/**
 * showArray: This will render any kind of non-primitive Array to the HTML log. Kotlin beginners should become familiar with the difference between primitive and non-primitive arrays and how they are created.
 *
 * @param target The non-primitive array to be rendered.
 * @param targetVariableName The variable name of the array, if known.
 * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
 * @return Returns the HTML rendition of the array as it was logged.
 */
fun Boolog.showArray(target: Array<*>, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
    if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
        return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
    }

    val targetClass = target::class as KClass<Any>

    if (target.count() < 1) {
        return "<div class=\"outlined\">(${targetClass.simpleName} \"$targetVariableName\" is empty)</div>"
    }

    val timestamp = LocalDateTime.now()

    val typeName = target[0]!!::class.simpleName
    val result = this.beginShow(timestamp, "Array&lt$typeName&gt", targetVariableName, "neutral", recurseLevel)
    val content = java.lang.StringBuilder("<br><table class=\"gridlines\">\r\n")

    for (index in 0 until target.size) {
        content.append("<tr><td>")
        content.append(index.toString())
        content.append("</td><td>")
        content.append(this.show(target[index], "Array slot #$index", recurseLevel + 1))
        content.append("</td></tr>\r\n")
    }

    content.append("\r\n</table><br>")

    if (target.size > MAX_OBJECT_FIELDS_TO_DISPLAY) {
        val identifier2 = UUID.randomUUID().toString()
        result.append("<label for=\"$identifier2\">\r\n<input id=\"$identifier2\" type=\"checkbox\">\r\n(show ${target.size} items)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
        result.append(content.toString());
        result.append("</div></label>");
    } else {
        result.append(content.toString());
    }

    if (recurseLevel > 0) {
        result.append("\r\n</div></label>")
    }

    result.append("\r\n</div>")

    val rendition = result.toString()

    if (recurseLevel < 1) {
        this.writeToHTML(rendition)
    }

    return rendition
}

/**
 * showPrimitiveArray: This will render any kind of primitive array to the HTML log. Note that this works by translating it to a Map and calling showMap(). Kotlin beginners should become familiar with the difference between primitive and non-primitive arrays and how they are created.
 *
 * @param candidate The primitive array to be rendered.
 * @param targetVariableName The variable name of the primitive array, if known.
 * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
 * @return Returns the HTML rendition of the primitive array as it was logged.
 */
fun Boolog.showPrimitiveArray(candidate: Any, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
    val target = HashMap<String, String>()

    if (candidate is IntArray) {
        for (index in 0 until candidate.size) {
            target[index.toString()] = candidate[index].toString()
        }
        return this.showMap(target, targetVariableName, recurseLevel, "IntArray")
    }

    if (candidate is FloatArray) {
        for (index in 0 until candidate.size) {
            target[index.toString()] = candidate[index].toString()
        }
        return this.showMap(target, targetVariableName, recurseLevel, "FloatArray")
    }

    if (candidate is ShortArray) {
        for (index in 0 until candidate.size) {
            target[index.toString()] = candidate[index].toString()
        }
        return this.showMap(target, targetVariableName, recurseLevel, "ShortArray")
    }

    if (candidate is LongArray) {
        for (index in 0 until candidate.size) {
            target[index.toString()] = candidate[index].toString()
        }
        return this.showMap(target, targetVariableName, recurseLevel, "LongArray")
    }

    if (candidate is DoubleArray) {
        for (index in 0 until candidate.size) {
            target[index.toString()] = candidate[index].toString()
        }
        return this.showMap(target, targetVariableName, recurseLevel, "DoubleArray")
    }

    if (candidate is CharArray) {
        for (index in 0 until candidate.size) {
            target[index.toString()] = candidate[index].toString()
        }
        return this.showMap(target, targetVariableName, recurseLevel, "CharArray")
    }

    if (candidate is ByteArray) {
        for (index in 0 until candidate.size) {
            target[index.toString()] = candidate[index].toString()
        }
        return this.showMap(target, targetVariableName, recurseLevel, "ByteArray")
    }

    if (candidate is BooleanArray) {
        for (index in 0 until candidate.size) {
            target[index.toString()] = candidate[index].toString()
        }
        return this.showMap(target, targetVariableName, recurseLevel, "BooleanArray")
    }

    throw IllegalArgumentException("The parameter supplied is not a primitive array.")
}

