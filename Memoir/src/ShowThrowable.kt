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

private const val DEFAULT_STACKTRACE = "(no stacktrace)"

fun Memoir.ShowThrowable(target: Throwable, isNested: Boolean = false): String {
    val timeStamp = LocalDateTime.now()
    val result = StringBuilder("<div class=\"object exception\">\r\n")
    val name = target.javaClass.simpleName
    val htmlStackTrace = StringBuilder(DEFAULT_STACKTRACE)
    val plainTextStackTrace = StringBuilder(DEFAULT_STACKTRACE)
    var hasStackTrace = false

    // Build the stacktrace  strings
    if (target.stackTrace != null) {
        if (target.stackTrace.size > 0) {
            hasStackTrace = true
            htmlStackTrace.clear()
            plainTextStackTrace.clear()

            for (thisElement in target.stackTrace) {
                if (plainTextStackTrace.length > 0) {
                    plainTextStackTrace.append("\r\n\r\n")
                }

                if (htmlStackTrace.length > 0) {
                    htmlStackTrace.append("<br>")
                }

                plainTextStackTrace.append("* ")
                htmlStackTrace.append("<b>&bull; ")

                if (thisElement.moduleName != null) {
                    val moduleName = "${thisElement.moduleName}: "
                    plainTextStackTrace.append(moduleName)
                    htmlStackTrace.append(moduleName)
                }

                val fileAndLine = "${thisElement.fileName} line ${thisElement.lineNumber}"
                plainTextStackTrace.append("$fileAndLine ")
                htmlStackTrace.append("$fileAndLine</b> ")

                val methodLocation = "in method ${thisElement.methodName}()"
                plainTextStackTrace.append("$methodLocation ")
                htmlStackTrace.append("$methodLocation ")

                if (thisElement.className != null) {
                    if (thisElement.className != "MainKt") {
                        val className = "of class ${thisElement.className}"
                        plainTextStackTrace.append(className)
                        htmlStackTrace.append(className)
                    }
                }

                plainTextStackTrace.append("\r\n")
            }
        }
    }

    this.EchoPlainText("$name\r\n\r\n${target.message}\r\n$plainTextStackTrace")

    if (hasStackTrace || (target.cause != null)) {
        val indicator = StringBuilder("show ")
        if (hasStackTrace) {
            indicator.append("stacktrace")
        }

        if (target.cause != null) {
            if (indicator.length > 5) {
                indicator.append(" & ")
            }

            indicator.append("cause")
        }

        val identifier = UUID.randomUUID().toString()
        result.append("<label for=\"$identifier\">\r\n<h2>$name</h2>\r\n${target.message}<br><br><input id=\"$identifier\" type=\"checkbox\"><small><i>($indicator)</i></small>\r\n<div class=\"${this.encapsulationTag}\">\r\n<br><small><i>\r\n$htmlStackTrace\r\n</i></small>\r\n")

        if (target.cause != null) {
            result.append("<br>\r\n<table><tr><td>&nbsp;</td><td>Caused by $EMOJI_CAUSED_BY</td><td>&nbsp;</td><td>${this.ShowThrowable(target.cause!!, true)}</td></tr></table>")
        }

        result.append("</div>\r\n</label>")
    } else {
        result.append("<h2>$name</h2>\r\n${target.message}\r\n<br><br><small><i>(no stacktrace and no known cause)</i></small>")
    }

    result.append("</div>")

    if (! isNested) this.WriteToHTML(result.toString(), EMOJI_ERROR, timeStamp)

    return result.toString()
}