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
 * showMap: This will render a Map of any kind to the HTML log.
 *
 * @param target The Map to be rendered.
 * @param targetVariableName The name of the Map, if known.
 * @param recurseLevel This function is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
 * @param targetClassName Can be used to be more specific of the type of this Map.
 * @return Returns the HTML rendition of the Map as it was logged.
 */
fun Boolog.showMap(target: Map<*, *>, targetVariableName: String = NAMELESS, recurseLevel: Int = 0, targetClassName: String = "Map"): String {
    if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
        return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
    }

    //val targetClass = target::class as KClass<Any>

    if (target.count() < 1) {
        return "<div class=\"outlined highlighted\">(the map \"$targetVariableName\" is empty)</div>"
    }

    val timestamp = LocalDateTime.now()

    val result = this.beginShow(timestamp, targetClassName, targetVariableName, "neutral", recurseLevel)
    val content = java.lang.StringBuilder("<br><table class=\"gridlines\">\r\n")
    var fieldCount = 0

    target.forEach {
        fieldCount++
        content.append("<tr><td>")
        content.append(it.key.toString())
        content.append("</td><td>")
        content.append(this.show(it.value, recurseLevel = recurseLevel + 1))
        content.append("</td></tr>\r\n")
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
