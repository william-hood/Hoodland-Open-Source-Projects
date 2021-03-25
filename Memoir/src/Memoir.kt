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

package hoodland.opensource.memoir

import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val STARTING_CONTENT = "<table>\r\n"
private val PLAINTEXT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd\tHH:mm:ss.SSS")
private val HTML_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val HTML_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

private fun defaultHeader(title: String): String {
    return "<h1>$title</h1>\r\n<hr>\r\n<small><i>Powered by the Memoir Logging System...</i></small>\r\n\r\n"
}

internal fun highlight(message: String, style: String = "highlighted"): String {
    return "<p class=\"$style outlined\">&nbsp;$message&nbsp;</p>"
}

/**
 * MemoirConcludedException is thrown if an attempt is made to write to a memoir that has already
 * had it's file closed (by the explict conclude() function) or has been included as a subsection
 * of another Memoir.
 */
class MemoirConcludedException: Exception(ALREADY_CONCLUDED_MESSAGE) { }

/**
 * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
 * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
 * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
 * in click-to-expand fashion.
 *
 * @property title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
 * @property forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
 * @property forHTML This is the main log file. It may be left out when used as a subsection of another Memoir.
 * @property showTimestamps If you don't want time stamps with every line of the log, set this to false.
 * @property showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
 * @constructor
 *
 * @param headerFunction Use this to override the default header and make your own.
 */
class Memoir (
        val title: String = UNKNOWN,
        val forPlainText: PrintWriter? = null,
        val forHTML: PrintWriter? = null,
        val showTimestamps: Boolean = true,
        val showEmojis: Boolean = true,
        headerFunction: (String)->String = ::defaultHeader) {
    private val printWriter_HTML: PrintWriter? = forHTML
    private val printWriter_PlainText: PrintWriter? = forPlainText
    private val content = StringBuilder(STARTING_CONTENT)
    private var isConcluded = false
    private var firstEcho = true

    init {
        if (printWriter_HTML != null) {
            printWriter_HTML.print("<html>\r\n<meta charset=\"UTF-8\">\r\n<head>\r\n<title>$title</title>\r\n")
            printWriter_HTML.print(MEMOIR_LOG_STYLING)
            printWriter_HTML.print("</head>\r\n<body>\r\n")
            printWriter_HTML.print(headerFunction(title))
        }
    }

    /**
     * Will return false if this memoir never got used. This may be used to decline adding an empty subsection to another Memoir.
     */
    val wasUsed: Boolean
        get() = (content.length - STARTING_CONTENT.length) > 0

    internal val encapsulationTag: String
        get() = "lvl-${UUID.randomUUID()}"

    /**
     * conclude: This explicitly puts the memoir in concluded status. If a printwriter for
     * HTML output had been supplied at construction time, that file will be properly closed.
     * Once a Memoir has been concluded, it is read-only. Calling this function will
     * not produce an exception if this Memoir is already concluded. Adding this memoir as
     * a subsection of another Memoir with either show() or showMemoir() will call this
     * method and force conclusion.
     *
     * @return Returns the HTML content that was (or would be) written to its HTML output file.
     * This content is what's used to add it as a subsection of another Memoir, and does not include the header.
     */
    fun conclude(): String {
        if (!isConcluded) {
            echoPlainText("", EMOJI_TEXT_MEMOIR_CONCLUDE)
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
     * @param timestamp Omit this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in. Note that the time stamp will be discarded if this Memoir was created with showTimestamps=false.
     */
    fun echoPlainText(message: String, emoji: String = EMOJI_TEXT_BLANK_LINE, timestamp: LocalDateTime? = LocalDateTime.now()) {
        if (printWriter_PlainText == null) {
            // Silently decline
            return
        }

        if (isConcluded) {
            throw MemoirConcludedException()
        }

        if (firstEcho) {
            firstEcho = false
            echoPlainText("", timestamp = timestamp)
            echoPlainText(title, EMOJI_MEMOIR, timestamp)
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
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Memoir's subsection.
     * Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser
     * that views the file. Note that calling this does not result in a counterpart call to echoPlainText().
     *
     * @param message Text that is being logged.  Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser that views the file.
     * @param emoji Used as an icon to indicate the nature of the message. There are emoji constants available in Constants.kt.
     * @param timestamp Omit this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
     */
    fun writeToHTML(message: String, emoji: String = EMOJI_TEXT_BLANK_LINE, timestamp: LocalDateTime? = LocalDateTime.now()) {
        if (isConcluded) {
            throw MemoirConcludedException()
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
     * info: This is the primary way to log ordinary output with memoir. The line in the log will have a time stamp,
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

    private fun wrapAsSubordinate(memoirTitle: String, memoirContent: String, style: String = "neutral"): String {
        val identifier = UUID.randomUUID().toString()
        return "\r\n\r\n<div class=\"memoir $style\">\r\n<label for=\"$identifier\">\r\n<input id=\"$identifier\" class=\"gone\" type=\"checkbox\">\r\n<h2>$memoirTitle</h2>\r\n<div class=\"$encapsulationTag\">\r\n$memoirContent\r\n</div></label></div>"
    }

    /**
     * showMemoir will take another Memoir object, conclude it, and embed it as a subsection. If the subordinate
     * Memoir has an HTML file associated with it, it will be written out and closed.
     *
     * @param subordinate The Memoir to be embedded as a subsection. If it is not yet concluded, it will be after this method returns.
     * @param emoji If not omitted, you can use this to override the "Memoir" emoji that normally appears next to the subsection. There are emoji constants available in Constants.kt.
     * @param style If not omitted, you can use this to override the "neutral" theme the subsection will have. This does not change the appearance of the subordinate Memoir's HTML file.
     * @param recurseLevel Used to determine if this is the root level, and whether or not to send this to the HTML stream. If you don't know what you're doing with this, omit it.
     * @return Returns the HTML to represent the subordinate Memoir as a subsection of this one.
     */
    fun showMemoir(subordinate: Memoir, emoji: String = EMOJI_MEMOIR, style: String = "neutral", recurseLevel: Int = 0) : String {
        val timestamp = LocalDateTime.now()
        val subordinateContent = subordinate.conclude()
        val result = wrapAsSubordinate(subordinate.title, subordinateContent, style)

        if (recurseLevel < 1) {
            writeToHTML(result, emoji, timestamp)
        }

        return result
    }
}
