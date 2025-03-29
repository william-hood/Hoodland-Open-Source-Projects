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
import kotlin.reflect.full.memberProperties

/**
 * showObject: Use this to render the visible portion of ANY object. Useful for debugging. Fields that are private
 * or internal scope can not be shown, but the total number of fields will be known.
 *
 * @param target The class or object to be rendered.
 * @param targetVariableName The name of the target object, if known.
 * @param recurseLevel This function is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
 * @return Returns the HTML rendition of the class/object as it was logged.
 */
fun Boolog.showObject(target: Any?, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
    if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
        return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
    }

    if (target == null) {
        return highlight("(Object is Null)")
    }

    val timestamp = LocalDateTime.now()
    val targetClass = target::class as KClass<Any>
    val result = this.beginShow(timestamp, targetClass.simpleName.toString(), targetVariableName, "plate", recurseLevel)
    val content = java.lang.StringBuilder("<br><table class=\"gridlines\">\r\n")
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
                content.append(this.show(value, it.name, recurseLevel + 1))
                content.append("</td></tr>\r\n")
            } catch (dontCare: Throwable) { }
        }
    }

    content.append("\r\n</table><br>")

    val fieldCount = targetClass.memberProperties.count()

    var visibilityDescription: String
    if (visibleProperties < 1) {
        content.clear()
        visibilityDescription = "${treatAsCode("toString() returns " + target.toString())}None of the $fieldCount members are"
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

    // Don't show the checkbox if nothing's actually visible
    if (visibleProperties > 0) {
        if (fieldCount > MAX_OBJECT_FIELDS_TO_DISPLAY) {
            val identifier2 = UUID.randomUUID().toString()
            result.append("<label for=\"$identifier2\">\r\n<input id=\"$identifier2\" type=\"checkbox\">\r\n(show $fieldCount fields)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
            result.append(content.toString())
            result.append("</div></label>")
        } else {
            result.append(content.toString())
        }
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