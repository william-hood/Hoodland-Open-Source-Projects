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

package rockabilly.memoir

import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility

internal val nameless = "(name not given)"

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

internal fun Memoir.beginShow(timeStamp: LocalDateTime, className: String, variableName: String, style: String, recurseLevel: Int): StringBuilder {
    val result = StringBuilder("\r\n<div class=\"object $style centered\">\r\n")

    if (recurseLevel > 0) {
        val identifier = UUID.randomUUID().toString()
        result.append("<label for=\"$identifier\">\r\n<input id=\"$identifier\" class=\"gone\" type=\"checkbox\">\r\n")
    }
    result.append("<h2>$className</h2>\r\n<small>")
    if (variableName != nameless) { result.append('"') }
    result.append(variableName)
    if (variableName != nameless) { result.append('"') }
    result.append("</small><br>\r\n")

    if (recurseLevel > 0) {
        result.append("<div class=\"${this.encapsulationTag}\">\r\n")
    }

    this.EchoPlainText("Showing $className: $variableName (details in HTML log)", EMOJI_OBJECT, timeStamp)

    return result
}

fun Memoir.Show(target: Any?, targetVariableName: String = nameless, recurseLevel: Int = 0): String {
    if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
        // Write to HTML and plain text???
        return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
    }

    if (target == null) {
        // Write to HTML and plain text???
        return "<div class=\"outlined\">($targetVariableName is Null)</div>"
    }

    if (target is Throwable) { return this.ShowThrowable(target) }
    if (target is Memoir) { return this.ShowMemoir(target, recurseLevel = recurseLevel) }
    if (target is Array<*>) { return this.ShowArray(target as Array<Any>, targetVariableName, recurseLevel) }
    if (target is Map<*, *>) { return this.ShowMap(target as Map<Any, Any>, targetVariableName, recurseLevel) }

    try {
        return this.ShowPrimitiveArray(target, targetVariableName, recurseLevel)
    } catch (dontCare: Throwable) {

    }

    try {
        return this.ShowIterable(target as Iterable<Any>, targetVariableName, recurseLevel)
    } catch (dontCare: Throwable) {

    }

    if (target is String) {
        // Attempt Base64 decode and JSON pretty-print
        return ProcessString(target)
        //May have to <pre> the result
    }

    if (shouldRecurse(target)) { return this.ShowObject(target, targetVariableName, recurseLevel) }

    return target.toString()
}
