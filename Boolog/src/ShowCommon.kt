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
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility

internal fun shouldRecurse(candidate: Any?): Boolean {
    if (candidate == null) return false
    if (candidate is String) return false
    if (candidate::class.javaPrimitiveType != null) return false

    return true
}

internal fun shouldRender(it: KProperty1<Any, *>): Boolean {
    //if (! it.isAccessible) return false
    if (it.visibility == null) return false
    if (it.visibility != KVisibility.PUBLIC) return false

    return true
}

internal fun Boolog.beginShow(timestamp: LocalDateTime, className: String, variableName: String, style: String, recurseLevel: Int): StringBuilder {
    val result = StringBuilder("\r\n<div class=\"object $style centered\">\r\n")

    if (recurseLevel > 0) {
        val identifier = UUID.randomUUID().toString()
        result.append("<label for=\"$identifier\">\r\n<input id=\"$identifier\" class=\"gone\" type=\"checkbox\">\r\n")
    }
    result.append("<h2>$className</h2>\r\n<small>")
    if (variableName != NAMELESS) { result.append('"') }
    result.append(variableName)
    if (variableName != NAMELESS) { result.append('"') }
    result.append("</small><br>\r\n")

    if (recurseLevel > 0) {
        result.append("<div class=\"${this.encapsulationTag}\">\r\n")
    }

    this.echoPlainText("Showing $className: $variableName (details in HTML log)", EMOJI_OBJECT, timestamp)

    return result
}

/**
 * show: This will render a class or object of any kind to the HTML log using the most appropriate function. Kotlin beginners: An "object" in Kotlin is essentially a static class in Java/C#.
 *
 * @param target The class or object to be rendered.
 * @param targetVariableName Variable name of said class/object, if known.
 * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
 * @return Returns the HTML rendition of the class/object as it was logged.
 */
fun Boolog.show(target: Any?, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
    if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
        // Write to HTML and plain text???
        return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
    }

    if (target == null) {
        // Write to HTML and plain text???
        return highlight("($targetVariableName is Null)")
    }

    if (target is Throwable) { return this.showThrowable(target) }
    if (target is Boolog) { return this.showBoolog(target, recurseLevel = recurseLevel) }
    if (target is Array<*>) { return this.showArray(target as Array<Any>, targetVariableName, recurseLevel) }
    if (target is Map<*, *>) { return this.showMap(target as Map<Any, Any>, targetVariableName, recurseLevel) }

    try {
        return this.showPrimitiveArray(target, targetVariableName, recurseLevel)
    } catch (dontCare: Throwable) {

    }

    try {
        return this.showIterable(target as Iterable<Any>, targetVariableName, recurseLevel)
    } catch (dontCare: Throwable) {

    }

    if (target is String) {
        return processString("", target, null)
    }

    if (shouldRecurse(target)) { return this.showObject(target, targetVariableName, recurseLevel) }

    return target.toString()
}
