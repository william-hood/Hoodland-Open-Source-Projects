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
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

private fun shouldRecurse(candidate: Any?): Boolean {
    if (candidate == null) return false
    if (candidate is String) return false
    if (candidate::class.javaPrimitiveType != null) return false

    return true
}

private fun shouldRender(it: KProperty1<Any, *>): Boolean {
    //if (! it.isAccessible) return false
    if (it.visibility == null) return false
    if (it.visibility != KVisibility.PUBLIC) return false

    return true
}

val nameless = "(name not given)"
fun Memoir.ShowObject(target: Any?, targetVariableName: String = nameless, recurseLevel: Int = 0): String {
    if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
        return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
    }

    if (target == null) {
        return "<div class=\"outlined\">(Object is Null)</div>"
    }

    val timeStamp = LocalDateTime.now()
    val result = StringBuilder("\r\n<div class=\"object plate centered\">\r\n")

    if (recurseLevel > 0) {
        val identifier = UUID.randomUUID().toString()
        result.append("<label for=\"$identifier\">\r\n<input id=\"$identifier\" class=\"gone\" type=\"checkbox\">\r\n")
    }

    val targetClass = target::class as KClass<Any>
    val title = "${targetClass.simpleName} ($targetVariableName)"
    this.EchoPlainText("Showing ${targetClass.simpleName}: $targetVariableName (details in HTML log)", EMOJI_OBJECT, timeStamp)

    result.append("<h2>${targetClass.simpleName}</h2>\r\n<small>")
    if (targetVariableName != nameless) { result.append('"') }
    result.append(targetVariableName)
    if (targetVariableName != nameless) { result.append('"') }
    result.append("</small><br>\r\n")

    if (recurseLevel > 0) {
        result.append("<div class=\"${this.encapsulationTag}\">\r\n")
    }

    val content = java.lang.StringBuilder("<br><table class=\"gridlines\">\r\n")

    // TODO: If targetClass is an array { } else
    var visibleProperties = 0
    targetClass.memberProperties.forEach {
        if (shouldRender(it)) {
            try {
                // This one line is the real reason for the try/catch block. It's possible we just don't have access and shouldRender() returned a false positive.
                var value = it.get(target)

                // At this point we've succeeded and can increment
                visibleProperties++
                content.append("<tr><td>")
                content.append(it.returnType.toString())
                content.append("</td><td>")
                content.append(it.name)
                content.append("</td><td>")

                if (shouldRecurse(value)) {
                    content.append(this.ShowObject(value, it.name, recurseLevel + 1))
                } else {
                    // Attempt Base64 decode if appropriate
                    // else
                    content.append(value.toString())
                }
                content.append("</td></tr>\r\n")
            } catch (dontCare: Throwable) { }
        }
    }

    content.append("\r\n</table><br>")

    val fieldCount = targetClass.memberProperties.count()

    var visibilityDescription = UNSET_STRING
    if (visibleProperties < 1) {
        content.clear()
        visibilityDescription = "None of the $fieldCount members are"
    } else {
        if (fieldCount == 1) {
            visibilityDescription = "The only member is"
        } else {
            if (visibleProperties == fieldCount) {
                visibilityDescription = "All of the $fieldCount members are"
            } else {
                visibilityDescription = "$visibleProperties of the $fieldCount members are"
            }
        }
    }

    result.append("<br><small><i>$visibilityDescription visible</i></small><br>")

    if (fieldCount > MAX_OBJECT_FIELDS_TO_DISPLAY) {
        val identifier2 = UUID.randomUUID().toString()
        result.append("<label for=\"$identifier2\">\r\n<input id=\"$identifier2\" type=\"checkbox\">\r\n(show $fieldCount fields)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
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
        this.WriteToHTML(rendition)
    }

    return rendition
}