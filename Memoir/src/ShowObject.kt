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

/*
private fun shouldRecurse(candidate: Object): Boolean {
    return true // TODO: Stub for now
}

private fun shouldRender(thisField: FieldInfo): Boolean {

}
*/

fun Memoir.ShowObject(target: Any, targetVariableName: String = "(name not given)", recurseLevel: Int = 0): String {
    if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
        return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
    }

    val timeStamp = LocalDateTime.now()
    val result = StringBuilder("<div class=\"object neutral\">\r\n")

    val targetClass = target::class
    val title = "${targetClass.simpleName} $targetVariableName"
    this.EchoPlainText(title, EMOJI_OBJECT, timeStamp)
    val identifier = UUID.randomUUID().toString()
    result.append("<label for=\"identifier\">\r\n<input id=\"identifier\" class=\"gone\" type=\"checkbox\">\r\n<center><h2>$title</h2></center>\r\n<div class=\"${this.encapsulationTag}\">\r\n")

    // C#: fieldcount = renderableFields(target)
    val content = java.lang.StringBuilder("<center><table class=\"gridlines\">\r\n")
    // TODO: If targetClass is an array { } else
    targetClass.members.forEach {
        // C#: If shouldRender(thisMember)
        content.append("<tr><td>");
        content.append(it.returnType.toString());
        content.append("</td><td>");
        content.append(it.name);
        content.append("</td><td>");

        val value = it.toString() // Stub

        // If ShouldRecurse() then do so
        // Attempt Base64 decode if appropriate
        // else
        content.append(value)
        content.append("</td></tr>\r\n")
    }
    content.append("\r\n</table></center><br></div>");

    val fieldCount = targetClass.members.count()
    if (fieldCount > MAX_OBJECT_FIELDS_TO_DISPLAY) {
        val identifier2 = UUID.randomUUID().toString()
        result.append("<label for=\"$identifier2\">\r\n<input id=\"$identifier2\" type=\"checkbox\">\r\n(show $fieldCount fields)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
        result.append(content.toString());
        result.append("</div></label>");
    } else {
        result.append(content.toString());
    }

    result.append("\r\n</label></div>")

    val rendition = result.toString()
    this.WriteToHTML(rendition)
    return rendition
}

// https://ktor.io/servers/features/content-negotiation/serialization-converter.html
// JSON Pretty print