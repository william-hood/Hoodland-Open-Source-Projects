// Copyright (c) 2020, 2023 William Arthur Hood
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

/*

Important note for version 2.1: To enable proper Java support, much of the functionality that
has been in extension methods must truly become part of the actual Memoir class. Since
Kotlin does not support partial classes, all that functionality must, unfortunately, be
put into one large file.

This file is now ridiculously large not by design, but by necessity.

 */
package hoodland.opensource.memoir

import hoodland.opensource.toolbox.isSuccessfulStatusCode
import hoodland.opensource.toolbox.toStatusCodeDescription
import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import java.net.http.HttpClient
import java.net.http.HttpHeaders
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

private const val STARTING_CONTENT = "<table class=\"left_justified\">\r\n"
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

    //===================================================
    // Formerly ShowArray.kt

    /**
     * showArray: This will render any kind of non-primitive Array to the HTML log. Kotlin beginners should become familiar with the difference between primitive and non-primitive arrays and how they are created.
     *
     * @param target The non-primitive array to be rendered.
     * @param targetVariableName The variable name of the array, if known.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the array as it was logged.
     */
    fun showArray(target: Array<*>, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
        if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
            return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
        }

        val targetClass = target::class as KClass<Any>

        if (target.count() < 1) {
            return "<div class=\"outlined\">(${targetClass.simpleName} \"$targetVariableName\" is empty)</div>"
        }

        val timestamp = LocalDateTime.now()

        val typeName = target[0]!!::class.simpleName
        val result = this.beginShow(timestamp, "Array&lt$typeName&gt", targetVariableName, "neutral", recurseLevel)
        val content = java.lang.StringBuilder("<br><table class=\"gridlines\">\r\n")

        for (index in 0 until target.size) {
            content.append("<tr><td>")
            content.append(index.toString())
            content.append("</td><td>")
            content.append(this.show(target[index], "Array slot #$index", recurseLevel + 1))
            content.append("</td></tr>\r\n")
        }

        content.append("\r\n</table><br>")

        if (target.size > MAX_OBJECT_FIELDS_TO_DISPLAY) {
            val identifier2 = UUID.randomUUID().toString()
            result.append("<label for=\"$identifier2\">\r\n<input id=\"$identifier2\" type=\"checkbox\">\r\n(show ${target.size} items)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
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

    /**
     * showPrimitiveArray: This will render any kind of primitive array to the HTML log. Note that this works by translating it to a Map and calling showMap(). Kotlin beginners should become familiar with the difference between primitive and non-primitive arrays and how they are created.
     *
     * @param candidate The primitive array to be rendered.
     * @param targetVariableName The variable name of the primitive array, if known.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the primitive array as it was logged.
     */
    fun showPrimitiveArray(candidate: Any, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
        val target = HashMap<String, String>()

        if (candidate is IntArray) {
            for (index in 0 until candidate.size) {
                target[index.toString()] = candidate[index].toString()
            }
            return this.showMap(target, targetVariableName, recurseLevel, "IntArray")
        }

        if (candidate is FloatArray) {
            for (index in 0 until candidate.size) {
                target[index.toString()] = candidate[index].toString()
            }
            return this.showMap(target, targetVariableName, recurseLevel, "FloatArray")
        }

        if (candidate is ShortArray) {
            for (index in 0 until candidate.size) {
                target[index.toString()] = candidate[index].toString()
            }
            return this.showMap(target, targetVariableName, recurseLevel, "ShortArray")
        }

        if (candidate is LongArray) {
            for (index in 0 until candidate.size) {
                target[index.toString()] = candidate[index].toString()
            }
            return this.showMap(target, targetVariableName, recurseLevel, "LongArray")
        }

        if (candidate is DoubleArray) {
            for (index in 0 until candidate.size) {
                target[index.toString()] = candidate[index].toString()
            }
            return this.showMap(target, targetVariableName, recurseLevel, "DoubleArray")
        }

        if (candidate is CharArray) {
            for (index in 0 until candidate.size) {
                target[index.toString()] = candidate[index].toString()
            }
            return this.showMap(target, targetVariableName, recurseLevel, "CharArray")
        }

        if (candidate is ByteArray) {
            for (index in 0 until candidate.size) {
                target[index.toString()] = candidate[index].toString()
            }
            return this.showMap(target, targetVariableName, recurseLevel, "ByteArray")
        }

        if (candidate is BooleanArray) {
            for (index in 0 until candidate.size) {
                target[index.toString()] = candidate[index].toString()
            }
            return this.showMap(target, targetVariableName, recurseLevel, "BooleanArray")
        }

        throw IllegalArgumentException("The parameter supplied is not a primitive array.")
    }

    //===================================================
    // Formerly ShowCommon.kt

    internal fun beginShow(timestamp: LocalDateTime, className: String, variableName: String, style: String, recurseLevel: Int): StringBuilder {
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
    fun show(target: Any?, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
        if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
            // Write to HTML and plain text???
            return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
        }

        if (target == null) {
            // Write to HTML and plain text???
            return highlight("($targetVariableName is Null)")
        }

        if (target is Throwable) { return this.showThrowable(target) }
        if (target is Memoir) { return this.showMemoir(target, recurseLevel = recurseLevel) }
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

//===================================================
// Formerly in ShowIterable.kt

    /**
     * showIterable: This will render an iterable of any kind to the HTML log. Maps sent to this function will not necessarily be redirected to showMap().
     *
     * @param target The Iterable to be rendered.
     * @param targetVariableName The variable name of the Iterable, if known.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the Iterable as it was logged.
     */
    fun showIterable(target: Iterable<*>, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
        if (recurseLevel > MAX_SHOW_OBJECT_RECURSION) {
            return "<div class=\"outlined\">$EMOJI_INCONCLUSIVE_TEST Too Many Levels In $EMOJI_INCONCLUSIVE_TEST</div>"
        }

        val targetClass = target::class as KClass<Any>

        if (target.count() < 1) {
            return "<div class=\"outlined\">(${targetClass.simpleName} \"$targetVariableName\" is empty)</div>"
        }

        val timestamp = LocalDateTime.now()

        val result = this.beginShow(timestamp, targetClass.simpleName.toString(), targetVariableName, "neutral", recurseLevel)
        val content = java.lang.StringBuilder("<br><table class=\"gridlines\">\r\n")
        var fieldCount = 0

        if (target is Map<*, *>) {
            val targetMap = target as Map<Any, Any>
            targetMap.forEach {
                fieldCount++
                content.append("<tr><td>")
                content.append(it.key.toString())
                content.append("</td><td>")
                content.append(this.show(it, recurseLevel = recurseLevel + 1))
                content.append("</td></tr>\r\n")
            }
        } else {
            target.forEach {
                fieldCount++
                content.append("<tr><td>")
                content.append(it!!::class.simpleName)
                content.append("</td><td>")
                content.append(this.show(it, recurseLevel = recurseLevel + 1))
                content.append("</td></tr>\r\n")
            }
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

//===================================================
// Formerly in ShowMap.kt

    /**
     * showMap: This will render a Map of any kind to the HTML log.
     *
     * @param target The Map to be rendered.
     * @param targetVariableName The name of the Map, if known.
     * @param recurseLevel This function is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @param targetClassName Can be used to be more specific of the type of this Map.
     * @return Returns the HTML rendition of the Map as it was logged.
     */
    fun showMap(target: Map<*, *>, targetVariableName: String = NAMELESS, recurseLevel: Int = 0, targetClassName: String = "Map"): String {
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

//===================================================
// Formerly in ShowObject.kt


    /**
     * showObject: Use this to render the visible portion of ANY object. Useful for debugging. Fields that are private
     * or internal scope can not be shown, but the total number of fields will be known.
     *
     * @param target The class or object to be rendered.
     * @param targetVariableName The name of the target object, if known.
     * @param recurseLevel This function is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    fun showObject(target: Any?, targetVariableName: String = NAMELESS, recurseLevel: Int = 0): String {
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

        var visibilityDescription = UNKNOWN
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

//===================================================
// Formerly in ShowThrowable.kt

    /**
     * showThrowable: This function properly renders an Exception (or any Throwable) in a readable format. The HTML
     * version provides click-to-expand views of the stack trace and causal exceptions.
     *
     * @param target The Exception, or other Throwable, to be rendered.
     * @param timestamp Omit this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
     * @param plainTextIndent This is used for Memoir's plain-text output. Unless you know what you're doing and why this should be omitted.
     * @return Returns the HTML representation of the exception that it logged.
     */
    fun showThrowable(target: Throwable, timestamp: LocalDateTime? = LocalDateTime.now(), plainTextIndent: String = ""): String {
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
        if (showEmojis) { concludedIndicator = EMOJI_TEXT_MEMOIR_CONCLUDE }
        this.echoPlainText("$plainTextIndent$concludedIndicator", timestamp = timestamp)
        this.echoPlainText("", timestamp = timestamp)
        return result.toString()
    }

//===================================================
// Formerly in ShowHttpMessages.kt

    /**
     * showHttpRequest: This renders a java.net.http.HttpRequest to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
     * supply a callbackFunction if none is needed.
     *
     * @param request The java.net.http.HttpRequest to be rendered.
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Memoir to display the outgoing content.
     * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON.
     */

    fun showHttpRequest(request: HttpRequest, bodyContentAsString: String? = null, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null) {
        val uri = request.uri()
        val queries = ArrayList<String>()
        val result = StringBuilder("<div class=\"outgoing implied_caution\">\r\n")

        if (uri.query != null) {
            queries.addAll(uri.query.split('&')) // Starts with ?
        }

        val textRendition = "${request.method()} ${uri.path}"
        result.append("<center><h2>$textRendition</h2>")
        result.append("<small><b><i>${uri.host}</i></b></small>")

        // Hide the Full URL
        val identifier = UUID.randomUUID().toString()
        result.append("<br><br><label for=\"$identifier\">\r\n<input id=\"$identifier\" type=\"checkbox\"><small><i>(show complete URL)</i></small>\r\n<div class=\"${this.encapsulationTag}\">\r\n")

        result.append("<br>\r\n${request.uri().toString().replace("&", "&amp;")}\r\n")
        result.append("</div>\r\n</label>")

        if (queries.size < 1) {
            result.append("<br><br><small><i>(no query)</i></small>")
        } else {
            result.append("<br><br><b>Queries</b><br><table class=\"gridlines\">\r\n")

            queries.forEach {
                val part = it.split('=')
                result.append("<tr><td>")
                result.append(part[0].replace("?", ""))
                result.append("</td><td>")
                if (part.size > 1)
                {
                    result.append(processString(part[0], part[1], callbackFunction))
                } else
                {
                    result.append("(unset)")
                }
                result.append("</td></tr>")
            }
            result.append("\r\n</table>")
        }

        var payload = ""

        bodyContentAsString?.let {
            payload = it
        }

        if (payload.length < 1) {
            if (request.bodyPublisher().isPresent) {
                payload = "(unknown content)"
            }
        }

        result.append("<br>${renderHeadersAndBody(request.headers(), payload, callbackFunction)}")

        writeToHTML(result.toString(), EMOJI_OUTGOING)
        echoPlainText(textRendition, EMOJI_OUTGOING)
    }

    /**
     * showHttpResponse: Properly renders a java.net.http.HttpResponse to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
     * supply a callbackFunction if none is needed.
     * @param response The java.net.http.HttpResponse to be rendered.
     * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON.
     */
    fun showHttpResponse(response: HttpResponse<*>, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null) {
        val statusCode = response.statusCode()
        var style = "implied_bad"
        if (statusCode.isSuccessfulStatusCode) { style = "implied_good" }

        val result = java.lang.StringBuilder("<div class=\"incoming $style\">\r\n")

        // Status code & description
        val textRendition = "$statusCode ${statusCode.toStatusCodeDescription()}"
        result.append("<center><h2>$textRendition</h2>")
        result.append(renderHeadersAndBody(response.headers(), response.body().toString(), callbackFunction))

        writeToHTML(result.toString(), EMOJI_INCOMING)
        echoPlainText(textRendition, EMOJI_INCOMING)
    }

    private fun renderHeadersAndBody(headers: HttpHeaders, stringPayload: String, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null): String {
        val result = StringBuilder()
        val headerMap = headers.map()

        // Headers
        if (headerMap.size > 0) {
            result.append("<br><b>Headers</b><br>")
            val renderedHeaders = StringBuilder("<table class=\"gridlines\">\r\n")

            headerMap.forEach() {thisHeader->
                renderedHeaders.append("<tr><td>")
                renderedHeaders.append(thisHeader.key)
                renderedHeaders.append("</td><td>")

                if (thisHeader.value.size < 1) {
                    renderedHeaders.append("<small><i>(empty)</i></small>")
                } else if (thisHeader.value.size == 1) {
                    // Attempt Base64 Decode and JSON pretty-print here.
                    renderedHeaders.append(processString(thisHeader.key, thisHeader.value[0].toString(), callbackFunction))
                } else {
                    renderedHeaders.append("<table class=\"gridlines neutral\">\r\n")
                    thisHeader.value.forEach() {
                        renderedHeaders.append("<tr><td>")
                        // Attempt Base64 Decode and JSON pretty-print here.
                        renderedHeaders.append(processString(thisHeader.key, it.toString(), callbackFunction))
                        renderedHeaders.append("</td></tr>")
                    }
                    renderedHeaders.append("\r\n</table>")
                }

                renderedHeaders.append("</td></tr>")
            }
            renderedHeaders.append("\r\n</table><br>")

            if (headerMap.keys.size > MAX_HEADERS_TO_DISPLAY) {
                val identifier1 = UUID.randomUUID().toString()
                result.append("<label for=\"$identifier1\">\r\n<input id=\"$identifier1\" type=\"checkbox\">\r\n(show ${headerMap.keys.size} headers)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
                result.append(renderedHeaders)
                result.append("</div></label>");
            } else {
                result.append(renderedHeaders)
            }
        }
        else {
            result.append("<br><br><small><i>(no headers)</i></small><br>\r\n")
        }

        // Body
        if ((stringPayload == "") || (stringPayload == "Optional.empty")) {
            result.append("<br><br><small><i>(no payload)</i></small></center>")
        } else {
            val size = stringPayload.length

            result.append("<br><b>Payload</b><br></center>\r\n")

            val renderedBody = treatAsCode(processString(HTTP_MESSAGE_BODY, stringPayload, callbackFunction))

            if (size > MAX_BODY_LENGTH_TO_DISPLAY) {
                val identifier2 = UUID.randomUUID().toString()
                result.append("<label for=\"$identifier2\">\r\n<input id=\"$identifier2\" type=\"checkbox\">\r\n(show large payload)\r\n<div class=\"${this.encapsulationTag}\">\r\n")
                result.append(renderedBody);
                result.append("</div></label>")
            } else {
                result.append(renderedBody)
            }
        }

        return result.toString()
    }

    /**
     * showHttpTransaction: Given a java.net.http.HttpRequest this renders it in the HTML log, uses java.net.http.HttpClient to send it and
     * receive a java.net.http.HttpResponse. The response will also be rendered to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
     * supply a callbackFunction if none is needed.
     * @param request The java.net.http.HttpRequest to be logged and sent.
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Memoir to display the outgoing content.
     * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON. This will be applied to BOTH the request and response.
     * @return The java.net.http.HttpResponse that was logged and returned.
     */
    fun showHttpTransaction(request: HttpRequest, bodyContentAsString: String? = null, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null): HttpResponse<*> {
        val client = HttpClient.newHttpClient()
        showHttpRequest(request, bodyContentAsString, callbackFunction)
        val result = client.send(request, HttpResponse.BodyHandlers.ofString())
        showHttpResponse(result, callbackFunction)
        return result
    }

    /* TODO: Provide a version of the function that uses the request embedded in the response.
     * The trick is getting a text version of the body correctly.
    fun Memoir.showHttpTransaction(response: HttpResponse<*>, callbackFunction: ((fieldName: String, fieldValue: String)->String)? = null) {
        val request = response.request()
        showHttpRequest(request, "(tbd)", callbackFunction)
        showHttpResponse(response, callbackFunction)
    }
     */
}


//===================================================
// Formerly in ShowCommon.kt

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

//===================================================
// Formerly in ShowThrowable.kt

fun depictFailure(thisFailure: Throwable): String {
    val stream = ByteArrayOutputStream()
    val printWriter = PrintWriter(stream)
    val memoir = Memoir(
        "",
        printWriter,
        null,
        false,
        false)
    memoir.showThrowable(thisFailure, null)
    memoir.conclude()
    return String(stream.toByteArray()).trim()
}