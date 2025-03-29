// Copyright (c) 2023, 2025 William Arthur Hood
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

package hoodland.opensource.boolog.java;

import hoodland.opensource.boolog.*;

import java.io.PrintWriter;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
 * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
 * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
 * in click-to-expand fashion.
 */
public class Boolog {
    private hoodland.opensource.boolog.Boolog KBoolog;

    //=== Primary Constructor

    /**
     * Primary constructor for the Boolog Java Wrapper. This contains a Kotlin Boolog but does not extend it nor expose it in any way.
     * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Boolog. For a subsection it appears in bold above the click-to-expand portion.
     * @param forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
     * @param forHTML This is the main log file. It may be left out when used as a subsection of another Boolog.
     * @param showTimestamps If you don't want time stamps with every line of the log, set this to false.
     * @param showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Boolog(String title,
                  PrintWriter forPlainText,
                  PrintWriter forHTML,
                  Boolean showTimestamps,
                  Boolean showEmojis,
                  HeaderFunction headerFunction) {
        if (headerFunction == null) {
            KBoolog = new hoodland.opensource.boolog.Boolog(
                    title,
                    forPlainText,
                    forHTML,
                    showTimestamps,
                    showEmojis,
                    //"<h1><img src=\"data:image/png;base64,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\" alt=\"Boolog Logo\" />&nbsp;$title</h1>\r\n<hr>\r\n<small><i>Powered by Boolog...</i></small>\r\n\r\n"
                    (p1) -> { return "<h1><img src=\"data:image/png;base64,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\" alt=\"Boolog Logo\" />&nbsp;" + title + "</h1>\r\n<hr>\r\n<small><i>Powered by Boolog...</i></small>\r\n\r\n"; }
            );
        } else {
            KBoolog = new hoodland.opensource.boolog.Boolog(
                    title,
                    forPlainText,
                    forHTML,
                    showTimestamps,
                    showEmojis,
                    headerFunction::displayHeader
            );
        }
    }

    //=== Alternate Constructors

    /**
     * Alternate constructor for the Boolog Java Wrapper that assumes showing emojis & timestamps, no headerFunction and no output for plain text or HTML.
     * This contains a Kotlin Boolog but does not extend it nor expose it in any way.
     * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
     * in click-to-expand fashion.
     *
     */
    public Boolog() {
        this(Constants.UNKNOWN, null, null, true, true, null);
    }

    /**
     * Alternate constructor for the Boolog Java Wrapper that uses the title "(unknown)".
     * It assumes showing timestamps and emojis but no header function and no HTML or plain text output.
     * This contains a Kotlin Boolog but does not extend it nor expose it in any way.
     * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
     * in click-to-expand fashion.
     *
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Boolog(HeaderFunction headerFunction) {
        this(Constants.UNKNOWN, null, null, true, true, headerFunction);
    }

    /**
     * Alternate constructor for the Boolog Java Wrapper that assumes showing timestamps and emojis but no header function and no HTML or plain text output.
     * This contains a Kotlin Boolog but does not extend it nor expose it in any way.
     * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Boolog. For a subsection it appears in bold above the click-to-expand portion.
     */
    public Boolog(String title) {
        this(title, null, null, true, true, null);
    }

    /**
     * Alternate constructor for the Boolog Java Wrapper that assumes showing timestamps and emojis but no HTML or plain text output.
     * This contains a Kotlin Boolog but does not extend it nor expose it in any way.
     * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Boolog. For a subsection it appears in bold above the click-to-expand portion.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Boolog(String title, HeaderFunction headerFunction) {
        this(title, null, null, true, true, headerFunction);
    }

    /**
     * Alternate constructor for the Boolog Java Wrapper that assumes showing timestamps and emojis but no header function.
     * This contains a Kotlin Boolog but does not extend it nor expose it in any way.
     * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Boolog. For a subsection it appears in bold above the click-to-expand portion.
     * @param forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
     * @param forHTML This is the main log file. It may be left out when used as a subsection of another Boolog.
     */
    public Boolog(String title, PrintWriter forPlainText, PrintWriter forHTML) {
        this(title, forPlainText, forHTML, true, true, null);
    }

    /**
     * Alternate constructor for the Boolog Java Wrapper that assumes showing timestamps and emojis.
     * This contains a Kotlin Boolog but does not extend it nor expose it in any way.
     * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Boolog. For a subsection it appears in bold above the click-to-expand portion.
     * @param forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
     * @param forHTML This is the main log file. It may be left out when used as a subsection of another Boolog.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Boolog(String title, PrintWriter forPlainText, PrintWriter forHTML, HeaderFunction headerFunction) {
        this(title, forPlainText, forHTML, true, true, headerFunction);
    }

    /**
     * Alternate constructor for the Boolog Java Wrapper that assumes no plaintext or HTML output and no header function.
     * This contains a Kotlin Boolog but does not extend it nor expose it in any way.
     * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Boolog. For a subsection it appears in bold above the click-to-expand portion.
     * @param showTimestamps If you don't want time stamps with every line of the log, set this to false.
     * @param showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
     */
    public Boolog(String title, Boolean showTimestamps, Boolean showEmojis) {
        this(title, null, null, showTimestamps, showEmojis, null);
    }

    /**
     * Alternate constructor for the Boolog Java Wrapper that assumes no plaintext or HTML output.
     * This contains a Kotlin Boolog but does not extend it nor expose it in any way.
     * Boolog is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Boolog may be a root-level log file, a subsection of another Boolog, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Boologs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Boolog. For a subsection it appears in bold above the click-to-expand portion.
     * @param showTimestamps If you don't want time stamps with every line of the log, set this to false.
     * @param showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Boolog(String title, Boolean showTimestamps, Boolean showEmojis, HeaderFunction headerFunction) {
        this(title, null, null, showTimestamps, showEmojis, headerFunction);
    }

    public Boolog(hoodland.opensource.boolog.Boolog kotlinBoolog) {
        KBoolog = kotlinBoolog;
    }

    //=== Property Field Getters

    public String getTitle() { return KBoolog.getTitle(); }
    public PrintWriter getPlainTextPrintWriter() { return KBoolog.getForPlainText(); }
    public PrintWriter getHTMLPrintWriter() { return KBoolog.getForHTML(); }
    public Boolean isShowingTimestamps() { return KBoolog.getShowTimestamps(); }
    public Boolean isShowingEmojis() { return KBoolog.getShowEmojis(); }

    /**
     * Will return false if this boolog never got used. This may be used to decline adding an empty subsection to another Boolog.
     */
    public Boolean wasUsed() { return KBoolog.getWasUsed(); }

    //=== Functions


    /**
     * conclude: This explicitly puts the boolog in concluded status. If a printwriter for
     * HTML output had been supplied at construction time, that file will be properly closed.
     * Once a Boolog has been concluded, it is read-only. Calling this function will
     * not produce an exception if this Boolog is already concluded. Adding this boolog as
     * a subsection of another Boolog with showBoolog() will call this
     * method and force conclusion.
     *
     * @return Returns the HTML content that was (or would be) written to its HTML output file.
     * This content is what's used to add it as a subsection of another Boolog, and does not include the header.
     */
    public String conclude() { return KBoolog.conclude(); }

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
     * @param timestamp Use the function signature without this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in. Note that the time stamp will be discarded if this Boolog was created with showTimestamps=false.
     */
    public void echoPlainText(String message, String emoji, LocalDateTime timestamp) {
        KBoolog.echoPlainText(message, emoji, timestamp);
    }

    /**
     * echoPlainText: This sends non-HTML plain-text output to the designated PrintWriter. Typically, that
     * is stdout console output, but it may also be sent to a plain-text log file. Unlike the HTML stream,
     * plain text is typically sent immediately. It does not try to show as a subsection and it may omit
     * details when rendering complicated objects. (For example, an HTTP Response will only show the status
     * code and description on the plain-text stream.) Note that calling this does not result in a counterpart
     * call to writeToHTML(). The timestamp will be assumed the current date/time.
     *
     * @param message Text that is being logged. Note that if HTML is sent here, the tags will not be hidden.
     * @param emoji Used as an icon to indicate the nature of the message. There are emoji constants available in Constants.kt.
     */
    public void echoPlainText(String message, String emoji) { echoPlainText(message, emoji, LocalDateTime.now()); }

    /**
     * echoPlainText: This sends non-HTML plain-text output to the designated PrintWriter. Typically, that
     * is stdout console output, but it may also be sent to a plain-text log file. Unlike the HTML stream,
     * plain text is typically sent immediately. It does not try to show as a subsection and it may omit
     * details when rendering complicated objects. (For example, an HTTP Response will only show the status
     * code and description on the plain-text stream.) Note that calling this does not result in a counterpart
     * call to writeToHTML(). No emoji will be provided.
     *
     * @param message Text that is being logged. Note that if HTML is sent here, the tags will not be hidden.
     * @param timestamp Use the function signature without this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in. Note that the time stamp will be discarded if this Boolog was created with showTimestamps=false.
     */
    public void echoPlainText(String message, LocalDateTime timestamp) {
        echoPlainText(message, Constants.EMOJI_TEXT_BLANK_LINE, timestamp);
    }

    /**
     * echoPlainText: This sends non-HTML plain-text output to the designated PrintWriter. Typically, that
     * is stdout console output, but it may also be sent to a plain-text log file. Unlike the HTML stream,
     * plain text is typically sent immediately. It does not try to show as a subsection and it may omit
     * details when rendering complicated objects. (For example, an HTTP Response will only show the status
     * code and description on the plain-text stream.) Note that calling this does not result in a counterpart
     * call to writeToHTML(). No emoji will be provided and the timestamp will be assumed the current date/time.
     *
     * @param message Text that is being logged. Note that if HTML is sent here, the tags will not be hidden.
     */
    public void echoPlainText(String message) {
        echoPlainText(message, Constants.EMOJI_TEXT_BLANK_LINE, LocalDateTime.now());
    }


    /**
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Boolog's subsection.
     * Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser
     * that views the file. Note that calling this does not result in a counterpart call to echoPlainText().
     *
     * @param message Text that is being logged.  Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser that views the file.
     * @param emoji Used as an icon to indicate the nature of the message. There are emoji constants available in Constants.kt.
     * @param timestamp Use the function signature without this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
     */
    public void writeToHTML(String message, String emoji, LocalDateTime timestamp) {
        KBoolog.writeToHTML(message, emoji, timestamp);
    }

    /**
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Boolog's subsection.
     * Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser
     * that views the file. Note that calling this does not result in a counterpart call to echoPlainText().
     * The timestamp will be assumed the current date/time.
     *
     * @param message Text that is being logged.  Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser that views the file.
     * @param emoji Used as an icon to indicate the nature of the message. There are emoji constants available in Constants.kt.
     */
    public void writeToHTML(String message, String emoji) { writeToHTML(message, emoji, LocalDateTime.now()); }

    /**
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Boolog's subsection.
     * Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser
     * that views the file. Note that calling this does not result in a counterpart call to echoPlainText().
     * No emoji will be provided.
     *
     * @param message Text that is being logged.  Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser that views the file.
     * @param timestamp Use the function signature without this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
     */
    public void writeToHTML(String message, LocalDateTime timestamp) {
        writeToHTML(message, Constants.EMOJI_TEXT_BLANK_LINE, timestamp);
    }

    /**
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Boolog's subsection.
     * Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser
     * that views the file. Note that calling this does not result in a counterpart call to echoPlainText().
     * No emoji will be provided and the timestamp will be assumed the current date/time.
     *
     * @param message Text that is being logged.  Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser that views the file.
     */
    public void writeToHTML(String message) {
        writeToHTML(message, Constants.EMOJI_TEXT_BLANK_LINE, LocalDateTime.now());
    }

    /**
     * info: This is the primary way to log ordinary output with boolog. The line in the log will have a time stamp,
     * but no emoji unless one is provided. This will log to both the HTML and plain-text streams.
     *
     * @param message The information being logged.
     * @param emoji If not omitted, you can use this to designate an emoji to appear next to the line. There are emoji constants available in Constants.kt.
     */
    public void info(String message, String emoji) { KBoolog.info(message, emoji); }
    public void info(String message) { info(message, Constants.EMOJI_TEXT_BLANK_LINE); }

    /**
     * debug will show a message highlighted in yellow with a "bug" emoji accompanying it. (The plain-text
     * stream will not be highlighted.)
     *
     * @param message The information being logged.
     */
    public void debug(String message) { KBoolog.debug(message); }

    /**
     * error will show a message highlighted in yellow with an "error" emoji accompanying it. (The plain-text
     * stream will not be highlighted.)
     *
     * @param message The information being logged.
     */
    public void error(String message) { KBoolog.error(message); }

    /**
     * skipLine will log a blank line to both the HTML and plain-text streams. (It will still have a timestamp.)
     *
     */
    public void skipLine() { KBoolog.skipLine(); }


    /**
     * showBoolog will take another Boolog object, conclude it, and embed it as a subsection. If the subordinate
     * Boolog has an HTML file associated with it, it will be written out and closed.
     *
     * @param subordinate The Boolog to be embedded as a subsection. If it is not yet concluded, it will be after this method returns.
     * @param emoji If not omitted, you can use this to override the "Boolog" emoji that normally appears next to the subsection. There are emoji constants available in Constants.kt.
     * @param style If not omitted, you can use this to override the "neutral" theme the subsection will have. This does not change the appearance of the subordinate Boolog's HTML file.
     * @param recurseLevel Used to determine if this is the root level, and whether or not to send this to the HTML stream. If you don't know what you're doing with this, Use the function signatures without it.
     * @return Returns the HTML to represent the subordinate Boolog as a subsection of this one.
     */
    public String showBoolog(Boolog subordinate, String emoji, String style, int recurseLevel) {
        return KBoolog.showBoolog(subordinate.KBoolog, emoji, style, recurseLevel);
    }

    /**
     * showBoolog will take another Boolog object, conclude it, and embed it as a subsection. If the subordinate
     * Boolog has an HTML file associated with it, it will be written out and closed. A recurse level of 0 is assumed.
     *
     * @param subordinate The Boolog to be embedded as a subsection. If it is not yet concluded, it will be after this method returns.
     * @param emoji If not omitted, you can use this to override the "Boolog" emoji that normally appears next to the subsection. There are emoji constants available in Constants.kt.
     * @param style If not omitted, you can use this to override the "neutral" theme the subsection will have. This does not change the appearance of the subordinate Boolog's HTML file.
     * @return Returns the HTML to represent the subordinate Boolog as a subsection of this one.
     */
    public String showBoolog(Boolog subordinate, String emoji, String style) {
        return showBoolog(subordinate, emoji, style, 0);
    }

    /**
     * showBoolog will take another Boolog object, conclude it, and embed it as a subsection. If the subordinate
     * Boolog has an HTML file associated with it, it will be written out and closed. Assumes a "Boolog" emoji and a "neutral" theme.
     *
     * @param subordinate The Boolog to be embedded as a subsection. If it is not yet concluded, it will be after this method returns.
     * @return Returns the HTML to represent the subordinate Boolog as a subsection of this one.
     */
    public String showBoolog(Boolog subordinate) {
        return showBoolog(subordinate, Constants.EMOJI_BOOLOG, "neutral", 0);
    }

    //=== ShowHttpMessages

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
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Boolog to display the outgoing content.
     * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON.
     */
    public void showHttpRequest(HttpRequest request, String bodyContentAsString, HttpFieldProcessingFunction callbackFunction) {
        if (callbackFunction == null) {
            ShowHttpMessagesKt.showHttpRequest(KBoolog, request, bodyContentAsString, null);
        } else {
            ShowHttpMessagesKt.showHttpRequest(KBoolog, request, bodyContentAsString, callbackFunction::processField);
        }
    }

    /**
     * showHttpRequest: This renders a java.net.http.HttpRequest to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. This version assumes no callback function for HTTP field processing.
     *
     * @param request The java.net.http.HttpRequest to be rendered.
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Boolog to display the outgoing content.
     */
    public void showHttpRequest(HttpRequest request, String bodyContentAsString) {
        showHttpRequest(request, bodyContentAsString, null);
    }

    /**
     * showHttpRequest: This renders a java.net.http.HttpRequest to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. This version assumes no body content.
     *
     * @param request The java.net.http.HttpRequest to be rendered.
     * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON.
     */
    public void showHttpRequest(HttpRequest request, HttpFieldProcessingFunction callbackFunction) {
        showHttpRequest(request, null, callbackFunction);
    }

    /**
     * showHttpRequest: This renders a java.net.http.HttpRequest to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
     * supply a callbackFunction if none is needed. This version assumes no body content and no callback function for HTTP field processing.
     *
     * @param request The java.net.http.HttpRequest to be rendered.
     */
    public void showHttpRequest(HttpRequest request) {
        showHttpRequest(request, null, null);
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
    public void showHttpResponse(HttpResponse response, HttpFieldProcessingFunction callbackFunction) {
        if (callbackFunction == null) {
            ShowHttpMessagesKt.showHttpResponse(KBoolog, response, null);
        } else {
            ShowHttpMessagesKt.showHttpResponse(KBoolog, response, callbackFunction::processField);
        }
    }

    /**
     * showHttpResponse: Properly renders a java.net.http.HttpResponse to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed.  This version assumes no callback function for HTTP field processing.
     *
     * @param response The java.net.http.HttpResponse to be rendered.
     */
    public void showHttpResponse(HttpResponse response) {
        showHttpResponse(response, null);
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
     *
     * @param request The java.net.http.HttpRequest to be logged and sent.
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Boolog to display the outgoing content.
     * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON. This will be applied to BOTH the request and response.
     * @return The java.net.http.HttpResponse that was logged and returned.
     */
    public HttpResponse<? extends Object> showHttpTransaction(HttpRequest request, String bodyContentAsString, HttpFieldProcessingFunction callbackFunction) {
        if (callbackFunction == null) {
            return ShowHttpMessagesKt.showHttpTransaction(KBoolog, request, bodyContentAsString, null);
        } else {
            return ShowHttpMessagesKt.showHttpTransaction(KBoolog, request, bodyContentAsString, callbackFunction::processField);
        }
    }

    /**
     * showHttpTransaction: Given a java.net.http.HttpRequest this renders it in the HTML log, uses java.net.http.HttpClient to send it and
     * receive a java.net.http.HttpResponse. The response will also be rendered to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed.  This version assumes no callback function for HTTP field processing.
     *
     * @param request The java.net.http.HttpRequest to be logged and sent.
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Boolog to display the outgoing content.
     * @return The java.net.http.HttpResponse that was logged and returned.
     */
    public HttpResponse<? extends Object> showHttpTransaction(HttpRequest request, String bodyContentAsString) {
        return showHttpTransaction(request, bodyContentAsString, null);
    }

    /**
     * showHttpTransaction: Given a java.net.http.HttpRequest this renders it in the HTML log, uses java.net.http.HttpClient to send it and
     * receive a java.net.http.HttpResponse. The response will also be rendered to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
     * supply a callbackFunction if none is needed. This version assumes no body content.
     *
     * @param request The java.net.http.HttpRequest to be logged and sent.
     * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON. This will be applied to BOTH the request and response.
     * @return The java.net.http.HttpResponse that was logged and returned.
     */
    public HttpResponse<? extends Object> showHttpTransaction(HttpRequest request, HttpFieldProcessingFunction callbackFunction) {
        return showHttpTransaction(request, null, callbackFunction);
    }

    /**
     * showHttpTransaction: Given a java.net.http.HttpRequest this renders it in the HTML log, uses java.net.http.HttpClient to send it and
     * receive a java.net.http.HttpResponse. The response will also be rendered to the HTML log. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed.  This version assumes no body content and no callback function for HTTP field processing.
     *
     * @param request The java.net.http.HttpRequest to be logged and sent.
     * @return The java.net.http.HttpResponse that was logged and returned.
     */
    public HttpResponse<? extends Object> showHttpTransaction(HttpRequest request) {
        return showHttpTransaction(request, null, null);
    }

    //=== ShowThrowable

    /**
     * showThrowable: This function properly renders an Exception (or any Throwable) in a readable format. The HTML
     * version provides click-to-expand views of the stack trace and causal exceptions.
     *
     * @param target The Exception, or other Throwable, to be rendered.
     * @param timestamp Omit this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
     * @param plainTextIndent This is used for Boolog's plain-text output. Unless you know what you're doing and why this should be omitted.
     * @return Returns the HTML representation of the exception that it logged.
     */
    public String showThrowable(Throwable target, LocalDateTime timestamp, String plainTextIndent) {
        return ShowThrowableKt.showThrowable(KBoolog, target, timestamp, plainTextIndent);
    }

    /**
     * showThrowable: This function properly renders an Exception (or any Throwable) in a readable format. The HTML
     * version provides click-to-expand views of the stack trace and causal exceptions.
     *
     * @param target The Exception, or other Throwable, to be rendered.
     * @return Returns the HTML representation of the exception that it logged.
     */
    public String showThrowable(Throwable target) {
        return showThrowable(target, LocalDateTime.now(), "");
    }

    /**
     * showThrowable: This function properly renders an Exception (or any Throwable) in a readable format. The HTML
     * version provides click-to-expand views of the stack trace and causal exceptions.
     *
     * @param target The Exception, or other Throwable, to be rendered.
     * @param timestamp Omit this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
     * @return Returns the HTML representation of the exception that it logged.
     */
    public String showThrowable(Throwable target, LocalDateTime timestamp) {
        return showThrowable(target, timestamp, "");
    }

    /**
     * showThrowable: This function properly renders an Exception (or any Throwable) in a readable format. The HTML
     * version provides click-to-expand views of the stack trace and causal exceptions.
     *
     * @param target The Exception, or other Throwable, to be rendered.
     * @param plainTextIndent This is used for Boolog's plain-text output. Unless you know what you're doing and why this should be omitted.
     * @return Returns the HTML representation of the exception that it logged.
     */
    public String showThrowable(Throwable target, String plainTextIndent) {
        return showThrowable(target, LocalDateTime.now(), plainTextIndent);
    }

    //=== Showing Objects

    /**
     * showObject: Use this to render the visible portion of ANY object. Useful for debugging. Fields that are private
     * or internal scope can not be shown, but the total number of fields will be known.
     *
     * @param target The class or object to be rendered.
     * @param targetVariableName The name of the target object, if known.
     * @param recurseLevel This function is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String showObject(Object target, String targetVariableName, int recurseLevel) {
        return ShowObjectKt.showObject(KBoolog, target, targetVariableName, recurseLevel);
    }

    /**
     * showObject: Use this to render the visible portion of ANY object. Useful for debugging. Fields that are private
     * or internal scope can not be shown, but the total number of fields will be known.
     *
     * @param target The class or object to be rendered.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String showObject(Object target) {
        return showObject(target, Constants.NAMELESS, 0);
    }

    /**
     * showObject: Use this to render the visible portion of ANY object. Useful for debugging. Fields that are private
     * or internal scope can not be shown, but the total number of fields will be known.
     *
     * @param target The class or object to be rendered.
     * @param targetVariableName The name of the target object, if known.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String showObject(Object target, String targetVariableName) {
        return showObject(target, targetVariableName, 0);
    }

    /**
     * showObject: Use this to render the visible portion of ANY object. Useful for debugging. Fields that are private
     * or internal scope can not be shown, but the total number of fields will be known.
     *
     * @param target The class or object to be rendered.
     * @param recurseLevel This function is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String showObject(Object target, int recurseLevel) {
        return showObject(target, Constants.NAMELESS, recurseLevel);
    }


    /**
     * show: This will render a class of any kind to the HTML log using the most appropriate function.
     * IMPORTANT: Unlike in Kotlin you should not pass a Boolog to this function.
     *
     * @param target The class or object to be rendered.
     * @param targetVariableName Variable name of said class/object, if known.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String show(Object target, String targetVariableName, int recurseLevel) {
        return ShowCommonKt.show(KBoolog, target, targetVariableName, recurseLevel);
    }

    /**
     * show: This will render a class of any kind to the HTML log using the most appropriate function.
     * IMPORTANT: Unlike in Kotlin you should not pass a Boolog to this function.
     *
     * @param target The class or object to be rendered.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String show(Object target) {
        return show(target, Constants.NAMELESS, 0);
    }

    /**
     * show: This will render a class of any kind to the HTML log using the most appropriate function.
     * IMPORTANT: Unlike in Kotlin you should not pass a Boolog to this function.
     *
     * @param target The class or object to be rendered.
     * @param targetVariableName Variable name of said class/object, if known.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String show(Object target, String targetVariableName) {
        return show(target, targetVariableName, 0);
    }

    /**
     * show: This will render a class of any kind to the HTML log using the most appropriate function.
     * IMPORTANT: Unlike in Kotlin you should not pass a Boolog to this function.
     *
     * @param target The class or object to be rendered.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String show(Object target, int recurseLevel) {
        return show(target, Constants.NAMELESS, recurseLevel);
    }

    /**
     * showArray: This will render any kind of non-primitive Array to the HTML log.
     *
     * @param target The non-primitive array to be rendered.
     * @param targetVariableName The variable name of the array, if known.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the array as it was logged.
     */
    public String showArray(Object[] target, String targetVariableName, int recurseLevel) {
        return ShowArrayKt.showArray(KBoolog, target, targetVariableName, recurseLevel);
    }

    /**
     * showArray: This will render any kind of non-primitive Array to the HTML log.
     *
     * @param target The non-primitive array to be rendered.
     * @return Returns the HTML rendition of the array as it was logged.
     */
    public String showArray(Object[] target) {
        return showArray(target, Constants.NAMELESS, 0);
    }

    /**
     * showArray: This will render any kind of non-primitive Array to the HTML log.
     *
     * @param target The non-primitive array to be rendered.
     * @param targetVariableName The variable name of the array, if known.
     * @return Returns the HTML rendition of the array as it was logged.
     */
    public String showArray(Object[] target, String targetVariableName) {
        return showArray(target, targetVariableName, 0);
    }

    /**
     * showArray: This will render any kind of non-primitive Array to the HTML log.
     *
     * @param target The non-primitive array to be rendered.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the array as it was logged.
     */
    public String showArray(Object[] target, int recurseLevel) {
        return showArray(target, Constants.NAMELESS, recurseLevel);
    }

    /**
     * showPrimitiveArray: This will render any kind of primitive array to the HTML log. Note that this works by translating it to a Map and calling showMap().
     *
     * @param candidate The primitive array to be rendered.
     * @param targetVariableName The variable name of the primitive array, if known.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the primitive array as it was logged.
     */
    public String showPrimitiveArray(Object candidate, String targetVariableName, int recurseLevel) {
        return ShowArrayKt.showPrimitiveArray(KBoolog, candidate, targetVariableName, recurseLevel);
    }

    /**
     * showPrimitiveArray: This will render any kind of primitive array to the HTML log. Note that this works by translating it to a Map and calling showMap().
     *
     * @param candidate The primitive array to be rendered.
     * @return Returns the HTML rendition of the primitive array as it was logged.
     */
    public String showPrimitiveArray(Object candidate) {
        return showPrimitiveArray(candidate, Constants.NAMELESS, 0);
    }

    /**
     * showPrimitiveArray: This will render any kind of primitive array to the HTML log. Note that this works by translating it to a Map and calling showMap().
     *
     * @param candidate The primitive array to be rendered.
     * @param targetVariableName The variable name of the primitive array, if known.
     * @return Returns the HTML rendition of the primitive array as it was logged.
     */
    public String showPrimitiveArray(Object candidate, String targetVariableName) {
        return showPrimitiveArray(candidate, targetVariableName, 0);
    }

    /**
     * showPrimitiveArray: This will render any kind of primitive array to the HTML log. Note that this works by translating it to a Map and calling showMap().
     *
     * @param candidate The primitive array to be rendered.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the primitive array as it was logged.
     */
    public String showPrimitiveArray(Object candidate, int recurseLevel) {
        return showPrimitiveArray(candidate, Constants.NAMELESS, recurseLevel);
    }

    /**
     * showIterable: This will render an iterable of any kind to the HTML log. Maps sent to this function will not necessarily be redirected to showMap().
     *
     * @param target The Iterable to be rendered.
     * @param targetVariableName The variable name of the Iterable, if known.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the Iterable as it was logged.
     */
    public String showIterable(Iterable target, String targetVariableName, int recurseLevel) {
        return ShowIterableKt.showIterable(KBoolog, target, targetVariableName, recurseLevel);
    }

    /**
     * showIterable: This will render an iterable of any kind to the HTML log. Maps sent to this function will not necessarily be redirected to showMap().
     *
     * @param target The Iterable to be rendered.
     * @return Returns the HTML rendition of the Iterable as it was logged.
     */
    public String showIterable(Iterable target) {
        return showIterable(target, Constants.NAMELESS, 0);
    }

    /**
     * showIterable: This will render an iterable of any kind to the HTML log. Maps sent to this function will not necessarily be redirected to showMap().
     *
     * @param target The Iterable to be rendered.
     * @param targetVariableName The variable name of the Iterable, if known.
     * @return Returns the HTML rendition of the Iterable as it was logged.
     */
    public String showIterable(Iterable target, String targetVariableName) {
        return showIterable(target, targetVariableName, 0);
    }

    /**
     * showIterable: This will render an iterable of any kind to the HTML log. Maps sent to this function will not necessarily be redirected to showMap().
     *
     * @param target The Iterable to be rendered.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the Iterable as it was logged.
     */
    public String showIterable(Iterable target, int recurseLevel) {
        return showIterable(target, Constants.NAMELESS, recurseLevel);
    }

    /**
     * showMap: This will render a Map of any kind to the HTML log.
     *
     * @param target The Map to be rendered.
     * @param targetVariableName The name of the Map, if known.
     * @param recurseLevel This function is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @param targetClassName Can be used to be more specific of the type of this Map.
     * @return Returns the HTML rendition of the Map as it was logged.
     */
    public String showMap(Map target, String targetVariableName, int recurseLevel, String targetClassName) {
        return ShowMapKt.showMap(KBoolog, target, targetVariableName, recurseLevel, targetClassName);
    }

    /**
     * showMap: This will render a Map of any kind to the HTML log.
     *
     * @param target The Map to be rendered.
     * @return Returns the HTML rendition of the Map as it was logged.
     */
    public String showMap(Map target) {
        return showMap(target, Constants.NAMELESS, 0, "Map");
    }

    /**
     * showMap: This will render a Map of any kind to the HTML log.
     *
     * @param target The Map to be rendered.
     * @param targetVariableName The name of the Map, if known.
     * @return Returns the HTML rendition of the Map as it was logged.
     */
    public String showMap(Map target, String targetVariableName) {
        return showMap(target, targetVariableName, 0, "Map");
    }

    /**
     * showMap: This will render a Map of any kind to the HTML log.
     *
     * @param target The Map to be rendered.
     * @param recurseLevel This function is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the Map as it was logged.
     */
    public String showMap(Map target, int recurseLevel) {
        return showMap(target, Constants.NAMELESS, recurseLevel, "Map");
    }

    /**
     * showMap: This will render a Map of any kind to the HTML log.
     *
     * @param target The Map to be rendered.
     * @param targetVariableName The name of the Map, if known.
     * @param targetClassName Can be used to be more specific of the type of this Map.
     * @return Returns the HTML rendition of the Map as it was logged.
     */
    public String showMap(Map target, String targetVariableName, String targetClassName) {
        return showMap(target, targetVariableName, 0, targetClassName);
    }

    /**
     * showMap: This will render a Map of any kind to the HTML log.
     *
     * @param target The Map to be rendered.
     * @param recurseLevel This function is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @param targetClassName Can be used to be more specific of the type of this Map.
     * @return Returns the HTML rendition of the Map as it was logged.
     */
    public String showMap(Map target, int recurseLevel, String targetClassName) {
        return showMap(target, Constants.NAMELESS, recurseLevel, targetClassName);
    }
}
