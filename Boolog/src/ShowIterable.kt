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
 * showIterable: This will render an iterable of any kind to the HTML log. Maps sent to this function will not necessarily be redirected to showMap().
 *
 * @param target The Iterable to be rendered.
 * @param targetVariableName The variable name of the Iterable, if known.
 * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
 * @return Returns the HTML rendition of the Iterable as it was logged.
 */
fun Boolog.showIterable(target: Iterable<*>, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
    if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
        return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
    }

    val targetClass = target::class as KClass<Any>

    if (target.count() < 1) {
        return "<div class=\"outlined\">(${targetClass.simpleName} \"$targetVariableName\" is empty)</div>"
    }

    val timestamp = LocalDateTime.now()

    val result = this.beginShow(timestamp, targetClass.simpleName.toString(), targetVariableName, "neutral", recurseLevel)
    val content = java.lang.StringBuilder("<br><table class=\"gridlines\">\r\n")
    var fieldCount = 0

    if (target is Map<*, *>) {
        val targetMap = target as Map<Any, Any>
        targetMap.forEach {
            fieldCount++
            content.append("<tr><td>")
            content.append(it.key.toString())
            content.append("</td><td>")
            content.append(this.show(it, recurseLevel = recurseLevel + 1))
            content.append("</td></tr>\r\n")
        }
    } else {
        target.forEach {
            fieldCount++
            content.append("<tr><td>")
            content.append(it!!::class.simpleName)
            content.append("</td><td>")
            content.append(this.show(it, recurseLevel = recurseLevel + 1))
            content.append("</td></tr>\r\n")
        }
    }

    content.append("\r\n</table><br>")

    if (fieldCount > MAX_OBJECT_FIELDS_TO_DISPLAY) {
        val identifier2 = UUID.randomUUID().toString()
        result.append("<label for=\"$identifier2\">\r\n<input id=\"$identifier2\" type=\"checkbox\">\r\n(show $fieldCount items)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
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
