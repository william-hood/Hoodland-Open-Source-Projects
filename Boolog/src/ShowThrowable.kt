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

import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import java.time.LocalDateTime
import java.util.*

private const val DEFAULT_STACKTRACE = "(no stacktrace)"

/**
 * showThrowable: This function properly renders an Exception (or any Throwable) in a readable format. The HTML
 * version provides click-to-expand views of the stack trace and causal exceptions.
 *
 * @param target The Exception, or other Throwable, to be rendered.
 * @param timestamp Omit this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
 * @param plainTextIndent This is used for Boolog's plain-text output. Unless you know what you're doing and why this should be omitted.
 * @return Returns the HTML representation of the exception that it logged.
 */
fun Boolog.showThrowable(target: Throwable, timestamp: LocalDateTime? = LocalDateTime.now(), plainTextIndent: String = ""): String {
    val result = StringBuilder("<div class=\"object exception\">\r\n")
    val name = target.javaClass.simpleName
    val htmlStackTrace = StringBuilder(DEFAULT_STACKTRACE)
    var hasStackTrace = false

    var loggedTextEmoji = EMOJI_ERROR
    // This IS nested if there is a plaintext indent.
    if (plainTextIndent.length > 0) {
        loggedTextEmoji = EMOJI_CAUSED_BY
    }

    this.echoPlainText("", timestamp = timestamp)
    this.echoPlainText("$plainTextIndent$name", loggedTextEmoji, timestamp)
    this.echoPlainText("$plainTextIndent${target.message}", timestamp = timestamp)

    // Build the stacktrace  strings
    if (target.stackTrace != null) {
        if (target.stackTrace.size > 0) {
            hasStackTrace = true
            htmlStackTrace.clear()

            for (thisElement in target.stackTrace) {
                if (htmlStackTrace.length > 0) {
                    htmlStackTrace.append("<br>")
                }

                val plainTextLine = StringBuilder()
                plainTextLine.append("* ")
                htmlStackTrace.append("<b>&bull; ")

                if (thisElement.moduleName != null) {
                    val moduleName = "${thisElement.moduleName}: "
                    plainTextLine.append(moduleName)
                    htmlStackTrace.append(moduleName)
                }

                val fileAndLine = "${thisElement.fileName} line ${thisElement.lineNumber}"
                plainTextLine.append("$fileAndLine ")
                htmlStackTrace.append("$fileAndLine</b> ")

                val methodLocation = "in method ${thisElement.methodName}()"
                plainTextLine.append("$methodLocation ")
                htmlStackTrace.append("$methodLocation ")

                if (thisElement.className != "MainKt") {
                    val className = "of class ${thisElement.className}"
                    plainTextLine.append(className)
                    htmlStackTrace.append(className)
                }

                this.echoPlainText("$plainTextIndent$plainTextLine", timestamp = timestamp)
            }
        }
    }

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
        result.append("<label for=\"$identifier\">\r\n<h2>$name</h2>\r\n<small><i>${target.message}</i></small><br><br><input id=\"$identifier\" type=\"checkbox\"><small><i>($indicator)</i></small>\r\n<div class=\"${this.encapsulationTag}\">\r\n<br><small><i>\r\n$htmlStackTrace\r\n</i></small>\r\n")

        if (target.cause != null) {
            result.append("<br>\r\n<table><tr><td>&nbsp;</td><td><small><b>Cause</b></small>&nbsp;$EMOJI_CAUSED_BY</td><td>&nbsp;</td><td>${this.showThrowable(target.cause!!, timestamp, "$plainTextIndent   ")}</td></tr></table>")
        }

        result.append("</div>\r\n</label>")
    } else {
        result.append("<h2>$name</h2>\r\n${target.message}\r\n<br><br><small><i>(no stacktrace and no known cause)</i></small>")
    }

    result.append("</div>")

    // This is not nested if there is no plaintext indent.
    if (plainTextIndent.length < 1) this.writeToHTML(result.toString(), EMOJI_ERROR, timestamp)

    var concludedIndicator = ""
    if (showEmojis) { concludedIndicator = EMOJI_TEXT_BOOLOG_CONCLUDE }
    this.echoPlainText("$plainTextIndent$concludedIndicator", timestamp = timestamp)
    this.echoPlainText("", timestamp = timestamp)
    return result.toString()
}

fun depictFailure(thisFailure: Throwable): String {
    val stream = ByteArrayOutputStream()
    val printWriter = PrintWriter(stream)
    val boolog = Boolog(
            "",
            printWriter,
            null,
            false,
            false)
    boolog.showThrowable(thisFailure, null)
    boolog.conclude()
    return String(stream.toByteArray()).trim()
}