package hoodland.opensource.memoir.java;


import hoodland.opensource.memoir.*;

import java.io.PrintWriter;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;

public class Memoir {
    private hoodland.opensource.memoir.Memoir KMemoir;

    //=== Primary Constructor

    /**
     * Primary constructor for the Memoir Java Wrapper. This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
     * @param forHTML This is the main log file. It may be left out when used as a subsection of another Memoir.
     * @param showTimestamps If you don't want time stamps with every line of the log, set this to false.
     * @param showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Memoir(String title,
                  PrintWriter forPlainText,
                  PrintWriter forHTML,
                  Boolean showTimestamps,
                  Boolean showEmojis,
                  HeaderFunction headerFunction) {
        if (headerFunction == null) {
            KMemoir = new hoodland.opensource.memoir.Memoir(
                    title,
                    forPlainText,
                    forHTML,
                    showTimestamps,
                    showEmojis,
                    (p1) -> { return "<h1>" + title + "</h1>\r\n<hr>\r\n<small><i>Powered by the Memoir Java Logging System...</i></small>\r\n\r\n"; }
            );
        } else {
            KMemoir = new hoodland.opensource.memoir.Memoir(
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
     * Alternate constructor for the Memoir Java Wrapper that assumes showing emojis & timestamps, no headerFunction and no output for plain text or HTML.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     */
    public Memoir() {
        this(Constants.UNKNOWN, null, null, true, true, null);
    }

    /**
     * Alternate constructor for the Memoir Java Wrapper that uses the title "(unknown)".
     * It assumes showing timestamps and emojis but no header function and no HTML or plain text output.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Memoir(HeaderFunction headerFunction) {
        this(Constants.UNKNOWN, null, null, true, true, headerFunction);
    }

    /**
     * Alternate constructor for the Memoir Java Wrapper that assumes showing timestamps and emojis but no header function and no HTML or plain text output.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     */
    public Memoir(String title) {
        this(title, null, null, true, true, null);
    }

    /**
     * Alternate constructor for the Memoir Java Wrapper that assumes showing timestamps and emojis but no HTML or plain text output.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Memoir(String title, HeaderFunction headerFunction) {
        this(title, null, null, true, true, headerFunction);
    }

    /**
     * Alternate constructor for the Memoir Java Wrapper that assumes showing timestamps and emojis but no header function.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
     * @param forHTML This is the main log file. It may be left out when used as a subsection of another Memoir.
     */
    public Memoir(String title, PrintWriter forPlainText, PrintWriter forHTML) {
        this(title, forPlainText, forHTML, true, true, null);
    }

    /**
     * Alternate constructor for the Memoir Java Wrapper that assumes showing timestamps and emojis.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
     * @param forHTML This is the main log file. It may be left out when used as a subsection of another Memoir.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Memoir(String title, PrintWriter forPlainText, PrintWriter forHTML, HeaderFunction headerFunction) {
        this(title, forPlainText, forHTML, true, true, headerFunction);
    }

    /**
     * Alternate constructor for the Memoir Java Wrapper that assumes no plaintext or HTML output and no header function.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param showTimestamps If you don't want time stamps with every line of the log, set this to false.
     * @param showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
     */
    public Memoir(String title, Boolean showTimestamps, Boolean showEmojis) {
        this(title, null, null, showTimestamps, showEmojis, null);
    }

    /**
     * Alternate constructor for the Memoir Java Wrapper that assumes no plaintext or HTML output.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param showTimestamps If you don't want time stamps with every line of the log, set this to false.
     * @param showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public Memoir(String title, Boolean showTimestamps, Boolean showEmojis, HeaderFunction headerFunction) {
        this(title, null, null, showTimestamps, showEmojis, headerFunction);
    }

    //=== Property Field Getters

    public String getTitle() { return KMemoir.getTitle(); }
    public PrintWriter getPlainTextPrintWriter() { return KMemoir.getForPlainText(); }
    public PrintWriter getHTMLPrintWriter() { return KMemoir.getForHTML(); }
    public Boolean isShowingTimestamps() { return KMemoir.getShowTimestamps(); }
    public Boolean isShowingEmojis() { return KMemoir.getShowEmojis(); }

    /**
     * Will return false if this memoir never got used. This may be used to decline adding an empty subsection to another Memoir.
     */
    public Boolean wasUsed() { return KMemoir.getWasUsed(); }

    //=== Functions


    /**
     * conclude: This explicitly puts the memoir in concluded status. If a printwriter for
     * HTML output had been supplied at construction time, that file will be properly closed.
     * Once a Memoir has been concluded, it is read-only. Calling this function will
     * not produce an exception if this Memoir is already concluded. Adding this memoir as
     * a subsection of another Memoir with showMemoir() will call this
     * method and force conclusion.
     *
     * @return Returns the HTML content that was (or would be) written to its HTML output file.
     * This content is what's used to add it as a subsection of another Memoir, and does not include the header.
     */
    public String conclude() { return KMemoir.conclude(); }

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
     * @param timestamp Use the function signature without this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in. Note that the time stamp will be discarded if this Memoir was created with showTimestamps=false.
     */
    public void echoPlainText(String message, String emoji, LocalDateTime timestamp) {
        KMemoir.echoPlainText(message, emoji, timestamp);
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
     * @param timestamp Use the function signature without this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in. Note that the time stamp will be discarded if this Memoir was created with showTimestamps=false.
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
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Memoir's subsection.
     * Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser
     * that views the file. Note that calling this does not result in a counterpart call to echoPlainText().
     *
     * @param message Text that is being logged.  Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser that views the file.
     * @param emoji Used as an icon to indicate the nature of the message. There are emoji constants available in Constants.kt.
     * @param timestamp Use the function signature without this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
     */
    public void writeToHTML(String message, String emoji, LocalDateTime timestamp) {
        KMemoir.writeToHTML(message, emoji, timestamp);
    }

    /**
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Memoir's subsection.
     * Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser
     * that views the file. Note that calling this does not result in a counterpart call to echoPlainText().
     * The timestamp will be assumed the current date/time.
     *
     * @param message Text that is being logged.  Any HTML tags sent through this will be sent verbatim to the HTML output, and rendered by the browser that views the file.
     * @param emoji Used as an icon to indicate the nature of the message. There are emoji constants available in Constants.kt.
     */
    public void writeToHTML(String message, String emoji) { writeToHTML(message, emoji, LocalDateTime.now()); }

    /**
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Memoir's subsection.
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
     * writeToHTML Sends output to the primary HTML file, or to the HTML used as another Memoir's subsection.
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
     * info: This is the primary way to log ordinary output with memoir. The line in the log will have a time stamp,
     * but no emoji unless one is provided. This will log to both the HTML and plain-text streams.
     *
     * @param message The information being logged.
     * @param emoji If not omitted, you can use this to designate an emoji to appear next to the line. There are emoji constants available in Constants.kt.
     */
    public void info(String message, String emoji) { KMemoir.info(message, emoji); }
    public void info(String message) { info(message, Constants.EMOJI_TEXT_BLANK_LINE); }

    /**
     * debug will show a message highlighted in yellow with a "bug" emoji accompanying it. (The plain-text
     * stream will not be highlighted.)
     *
     * @param message The information being logged.
     */
    public void debug(String message) { KMemoir.debug(message); }

    /**
     * error will show a message highlighted in yellow with an "error" emoji accompanying it. (The plain-text
     * stream will not be highlighted.)
     *
     * @param message The information being logged.
     */
    public void error(String message) { KMemoir.error(message); }

    /**
     * skipLine will log a blank line to both the HTML and plain-text streams. (It will still have a timestamp.)
     *
     */
    public void skipLine() { KMemoir.skipLine(); }


    /**
     * showMemoir will take another Memoir object, conclude it, and embed it as a subsection. If the subordinate
     * Memoir has an HTML file associated with it, it will be written out and closed.
     *
     * @param subordinate The Memoir to be embedded as a subsection. If it is not yet concluded, it will be after this method returns.
     * @param emoji If not omitted, you can use this to override the "Memoir" emoji that normally appears next to the subsection. There are emoji constants available in Constants.kt.
     * @param style If not omitted, you can use this to override the "neutral" theme the subsection will have. This does not change the appearance of the subordinate Memoir's HTML file.
     * @param recurseLevel Used to determine if this is the root level, and whether or not to send this to the HTML stream. If you don't know what you're doing with this, Use the function signatures without it.
     * @return Returns the HTML to represent the subordinate Memoir as a subsection of this one.
     */
    public String showMemoir(Memoir subordinate, String emoji, String style, int recurseLevel) {
        return KMemoir.showMemoir(subordinate.KMemoir, emoji, style, recurseLevel);
    }

    /**
     * showMemoir will take another Memoir object, conclude it, and embed it as a subsection. If the subordinate
     * Memoir has an HTML file associated with it, it will be written out and closed. A recurse level of 0 is assumed.
     *
     * @param subordinate The Memoir to be embedded as a subsection. If it is not yet concluded, it will be after this method returns.
     * @param emoji If not omitted, you can use this to override the "Memoir" emoji that normally appears next to the subsection. There are emoji constants available in Constants.kt.
     * @param style If not omitted, you can use this to override the "neutral" theme the subsection will have. This does not change the appearance of the subordinate Memoir's HTML file.
     * @return Returns the HTML to represent the subordinate Memoir as a subsection of this one.
     */
    public String showMemoir(Memoir subordinate, String emoji, String style) {
        return showMemoir(subordinate, emoji, style, 0);
    }

    /**
     * showMemoir will take another Memoir object, conclude it, and embed it as a subsection. If the subordinate
     * Memoir has an HTML file associated with it, it will be written out and closed. Assumes a "Memoir" emoji and a "neutral" theme.
     *
     * @param subordinate The Memoir to be embedded as a subsection. If it is not yet concluded, it will be after this method returns.
     * @return Returns the HTML to represent the subordinate Memoir as a subsection of this one.
     */
    public String showMemoir(Memoir subordinate) {
        return showMemoir(subordinate, Constants.EMOJI_MEMOIR, "neutral", 0);
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
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Memoir to display the outgoing content.
     * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON.
     */
    public void showHttpRequest(HttpRequest request, String bodyContentAsString, HttpFieldProcessingFunction callbackFunction) {
        if (callbackFunction == null) {
            ShowHttpMessagesKt.showHttpRequest(KMemoir, request, bodyContentAsString, null);
        } else {
            ShowHttpMessagesKt.showHttpRequest(KMemoir, request, bodyContentAsString, callbackFunction::processField);
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
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Memoir to display the outgoing content.
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
            ShowHttpMessagesKt.showHttpResponse(KMemoir, response, null);
        } else {
            ShowHttpMessagesKt.showHttpResponse(KMemoir, response, callbackFunction::processField);
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
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Memoir to display the outgoing content.
     * @param callbackFunction Optional: Supply a callback function to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON. This will be applied to BOTH the request and response.
     * @return The java.net.http.HttpResponse that was logged and returned.
     */
    public void showHttpTransaction(HttpRequest request, String bodyContentAsString, HttpFieldProcessingFunction callbackFunction) {
        if (callbackFunction == null) {
            ShowHttpMessagesKt.showHttpTransaction(KMemoir, request, bodyContentAsString, null);
        } else {
            ShowHttpMessagesKt.showHttpTransaction(KMemoir, request, bodyContentAsString, callbackFunction::processField);
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
     * @param bodyContentAsString It is impossible to get the string content of HttpRequest.BodyPublishers.ofString(). Echo the string content here if you want Memoir to display the outgoing content.
     * @return The java.net.http.HttpResponse that was logged and returned.
     */
    public void showHttpTransaction(HttpRequest request, String bodyContentAsString) {
        showHttpTransaction(request, bodyContentAsString, null);
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
    public void showHttpTransaction(HttpRequest request, HttpFieldProcessingFunction callbackFunction) {
        showHttpTransaction(request, null, callbackFunction);
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
    public void showHttpTransaction(HttpRequest request) {
        showHttpTransaction(request, null, null);
    }

    //=== ShowThrowable

    /**
     * showThrowable: This function properly renders an Exception (or any Throwable) in a readable format. The HTML
     * version provides click-to-expand views of the stack trace and causal exceptions.
     *
     * @param target The Exception, or other Throwable, to be rendered.
     * @param timestamp Omit this to use the current date/time. There are some circumstances where an event is logged after-the-fact and an explicit time stamp should be passed in.
     * @param plainTextIndent This is used for Memoir's plain-text output. Unless you know what you're doing and why this should be omitted.
     * @return Returns the HTML representation of the exception that it logged.
     */
    public String showThrowable(Throwable target, LocalDateTime timestamp, String plainTextIndent) {
        return ShowThrowableKt.showThrowable(KMemoir, target, timestamp, plainTextIndent);
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
     * @param plainTextIndent This is used for Memoir's plain-text output. Unless you know what you're doing and why this should be omitted.
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
        return ShowObjectKt.showObject(KMemoir, target, targetVariableName, recurseLevel);
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
     * IMPORTANT: Unlike in Kotlin you should not pass a Memoir to this function.
     *
     * @param target The class or object to be rendered.
     * @param targetVariableName Variable name of said class/object, if known.
     * @param recurseLevel Rendering data structures to HTML is is necessarily recursive. It will decline to recurse down beyond the constant value MAX_SHOW_OBJECT_RECURSION in Constants.kt.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String show(Object target, String targetVariableName, int recurseLevel) {
        return ShowCommonKt.show(KMemoir, target, targetVariableName, recurseLevel);
    }

    /**
     * show: This will render a class of any kind to the HTML log using the most appropriate function.
     * IMPORTANT: Unlike in Kotlin you should not pass a Memoir to this function.
     *
     * @param target The class or object to be rendered.
     * @return Returns the HTML rendition of the class/object as it was logged.
     */
    public String show(Object target) {
        return show(target, Constants.NAMELESS, 0);
    }

    /**
     * show: This will render a class of any kind to the HTML log using the most appropriate function.
     * IMPORTANT: Unlike in Kotlin you should not pass a Memoir to this function.
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
     * IMPORTANT: Unlike in Kotlin you should not pass a Memoir to this function.
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
        return ShowArrayKt.showArray(KMemoir, target, targetVariableName, recurseLevel);
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
        return ShowArrayKt.showPrimitiveArray(KMemoir, candidate, targetVariableName, recurseLevel);
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
        return ShowIterableKt.showIterable(KMemoir, target, targetVariableName, recurseLevel);
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
        return ShowMapKt.showMap(KMemoir, target, targetVariableName, recurseLevel, targetClassName);
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
