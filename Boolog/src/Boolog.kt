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

import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val STARTING_CONTENT = "<table class=\"left_justified\">\r\n"
private val PLAINTEXT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd\tHH:mm:ss.SSS")
private val HTML_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val HTML_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

internal fun defaultHeader(title: String): String {
    return "<h1><img src=\"data:image/png;base64,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\" alt=\"Boolog Logo\" />&nbsp;$title</h1>\r\n<hr>\r\n<small><i>Powered by Boolog...</i></small>\r\n\r\n"
}

internal fun highlight(message: String, style: String = "highlighted"): String {
    return "<p class=\"$style outlined\">&nbsp;$message&nbsp;</p>"
}

/**
 * BoologConcludedException is thrown if an attempt is made to write to a boolog that has already
 * had it's file closed (by the explict conclude() function) or has been included as a subsection
 * of another Boolog.
 */
class BoologConcludedException: Exception(ALREADY_CONCLUDED_MESSAGE) { }

/**
 * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
 * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
 * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
 * in click-to-expand fashion.
 *
 * @property title This will be indicated at the top of the file in the header if this is a root-level Boolog. For a subsection it appears in bold above the click-to-expand portion.
 * @property forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
 * @property forHTML This is the main log file. It may be left out when used as a subsection of another Boolog.
 * @property showTimestamps If you don't want time stamps with every line of the log, set this to false.
 * @property showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
 * @constructor
 *
 * @param theme Set this to one of the supported themes. You can use THEME_DEFAULT if you don't know which to choose. If there is no HTML stream, or the HTML is not going tp its own file, THEME_NONE is the best choice.
 * @param headerFunction Use this to override the default header and make your own.
 */
open class Boolog (
        val title: String = UNKNOWN,
        val forPlainText: PrintWriter? = null,
        val forHTML: PrintWriter? = null,
        val showTimestamps: Boolean = true,
        val showEmojis: Boolean = true,
        theme: String = THEME_DEFAULT,
        headerFunction: (String)->String = ::defaultHeader) {
    private val printWriter_HTML: PrintWriter? = forHTML
    private val printWriter_PlainText: PrintWriter? = forPlainText
    private val content = StringBuilder(STARTING_CONTENT)
    private var isConcluded = false
    private var firstEcho = true

    init {
        if (printWriter_HTML != null) {
            printWriter_HTML.print("<html>\r\n<meta charset=\"UTF-8\">\r\n<head>\r\n<title>$title</title>\r\n")
            printWriter_HTML.print(theme)
            printWriter_HTML.print("</head>\r\n<body>\r\n")
            printWriter_HTML.print(headerFunction(title))
        }
    }

    /**
     * Will return false if this boolog never got used. This may be used to decline adding an empty subsection to another Boolog.
     */
    val wasUsed: Boolean
        get() = (content.length - STARTING_CONTENT.length) > 0

    internal val encapsulationTag: String
        get() = "lvl-${UUID.randomUUID()}"

    /**
     * conclude: This explicitly puts the boolog in concluded status. If a printwriter for
     * HTML output had been supplied at construction time, that file will be properly closed.
     * Once a Boolog has been concluded, it is read-only. Calling this function will
     * not produce an exception if this Boolog is already concluded. Adding this boolog as
     * a subsection of another Boolog with either show() or showBoolog() will call this
     * method and force conclusion.
     *
     * @return Returns the HTML content that was (or would be) written to its HTML output file.
     * This content is what's used to add it as a subsection of another Boolog, and does not include the header.
     */
    fun conclude(): String {
        if (!isConcluded) {
            echoPlainText("", EMOJI_TEXT_BOOLOG_CONCLUDE)
            echoPlainText("")

            isConcluded = true

            content.append("\r\n</table>")

            if (printWriter_HTML != null) {
                printWriter_HTML.print(content.toString())
                printWriter_HTML.print("\r\n</body>\r\n</html>")
                printWriter_HTML.flush()
                printWriter_HTML.close()
            }
        }

        return content.toString()
    }

    // Parameter order differs from the C# version
    /**
     * echoPlainText: This sends non-HTML plain-text output to the designated PrintWriter. Typically, that
     * is stdout console output, but it may also be sent to a plain-text log file. Unlike the HTML stream,
     * plain text is typically sent immediately. It does not try to show as a subsection and it may omit
     * details when rendering complicated objects. (For example, an HTTP Response will only show the status
     * code and description on the plain-text stream.) Note that calling this does not result in a counterpart
     * call to writeToHTML().
     *
     * @param message Text that is being logged. Note that if HTML is sent here, the tags will not be hidden.
     * @param emoji Used as an icon to indicate the nature of the message. There are emoji constants available in Constants.kt.
     * @param timestamp Omit this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in. Note that the time stamp will be discarded if this Boolog was created with showTimestamps=false.
     */
    fun echoPlainText(message: String, emoji: String = EMOJI_TEXT_BLANK_LINE, timestamp: LocalDateTime? = LocalDateTime.now()) {
        if (printWriter_PlainText == null) {
            // Silently decline
            return
        }

        if (isConcluded) {
            throw BoologConcludedException()
        }

        if (firstEcho) {
            firstEcho = false
            echoPlainText("", timestamp = timestamp)
            echoPlainText(title, EMOJI_BOOLOG, timestamp)
        }


        if (showTimestamps) {
            var dateTime = "                        "
            timestamp?.let {
                dateTime = it.format(PLAINTEXT_DATETIME_FORMATTER)
            }

            printWriter_PlainText.print("$dateTime\t")
        }

        if (showEmojis) {
            printWriter_PlainText.print("$emoji\t")
        }

        printWriter_PlainText.println(message)
        printWriter_PlainText.flush()
    }

    // Parameter order differs from the C# version
    /**
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Boolog's subsection.
     * Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser
     * that views the file. Note that calling this does not result in a counterpart call to echoPlainText().
     *
     * @param message Text that is being logged.  Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser that views the file.
     * @param emoji Used as an icon to indicate the nature of the message. There are emoji constants available in Constants.kt.
     * @param timestamp Omit this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
     */
    fun writeToHTML(message: String, emoji: String = EMOJI_TEXT_BLANK_LINE, timestamp: LocalDateTime? = LocalDateTime.now()) {
        if (isConcluded) {
            throw BoologConcludedException()
        }

        content.append("<tr>")

        if (showTimestamps) {
            timestamp?.let {
                var date = it.format(HTML_DATE_FORMATTER)
                var time = it.format(HTML_TIME_FORMATTER)

                content.append("<td class=\"min\"><small>$date</small></td><td>&nbsp;</td><td class=\"min\"><small>$time</small></td><td>&nbsp;</td>")
            }
        }

        if (showEmojis) {
            content.append("<td><h2>$emoji</h2></td>")
        }

        content.append("<td>$message</td></tr>\r\n")
    }

    /**
     * info: This is the primary way to log ordinary output with boolog. The line in the log will have a time stamp,
     * but no emoji unless one is provided. This will log to both the HTML and plain-text streams.
     *
     * @param message The information being logged.
     * @param emoji If not omitted, you can use this to designate an emoji to appear next to the line. There are emoji constants available in Constants.kt.
     */
    fun info(message: String, emoji: String = EMOJI_TEXT_BLANK_LINE) {
        val timestamp = LocalDateTime.now()
        writeToHTML(message, emoji, timestamp)
        echoPlainText(message, emoji, timestamp)
    }

    /**
     * debug will show a message highlighted in yellow with a "bug" emoji accompanying it. (The plain-text
     * stream will not be highlighted.)
     *
     * @param message The information being logged.
     */
    fun debug(message: String) {
        val timestamp = LocalDateTime.now()
        writeToHTML(highlight(message), EMOJI_DEBUG, timestamp)
        echoPlainText(message, EMOJI_DEBUG, timestamp)
    }

    /**
     * error will show a message highlighted in yellow with an "error" emoji accompanying it. (The plain-text
     * stream will not be highlighted.)
     *
     * @param message The information being logged.
     */
    fun error(message: String) {
        val timestamp = LocalDateTime.now()
        writeToHTML(highlight(message), EMOJI_ERROR, timestamp)
        echoPlainText(message, EMOJI_ERROR, timestamp)
    }

    /**
     * skipLine will log a blank line to both the HTML and plain-text streams. (It will still have a timestamp.)
     *
     */
    fun skipLine() {
        writeToHTML("")
        echoPlainText("")
    }

    private fun wrapAsSubordinate(boologTitle: String, boologContent: String, style: String = "neutral"): String {
        val identifier = UUID.randomUUID().toString()
        return "\r\n\r\n<div class=\"boolog $style\">\r\n<label for=\"$identifier\">\r\n<input id=\"$identifier\" class=\"gone\" type=\"checkbox\">\r\n<h2>$boologTitle</h2>\r\n<div class=\"$encapsulationTag\">\r\n$boologContent\r\n</div></label></div>"
    }

    /**
     * showBoolog will take another Boolog object, conclude it, and embed it as a subsection. If the subordinate
     * Boolog has an HTML file associated with it, it will be written out and closed.
     *
     * @param subordinate The Boolog to be embedded as a subsection. If it is not yet concluded, it will be after this method returns.
     * @param emoji If not omitted, you can use this to override the "Boolog" emoji that normally appears next to the subsection. There are emoji constants available in Constants.kt.
     * @param style If not omitted, you can use this to override the "neutral" theme the subsection will have. This does not change the appearance of the subordinate Boolog's HTML file.
     * @param recurseLevel Used to determine if this is the root level, and whether or not to send this to the HTML stream. If you don't know what you're doing with this, omit it.
     * @return Returns the HTML to represent the subordinate Boolog as a subsection of this one.
     */
    fun showBoolog(subordinate: Boolog, emoji: String = EMOJI_BOOLOG, style: String = "neutral", recurseLevel: Int = 0) : String {
        val timestamp = LocalDateTime.now()
        val subordinateContent = subordinate.conclude()
        val result = wrapAsSubordinate(subordinate.title, subordinateContent, style)

        if (recurseLevel < 1) {
            writeToHTML(result, emoji, timestamp)
        }

        return result
    }
}
