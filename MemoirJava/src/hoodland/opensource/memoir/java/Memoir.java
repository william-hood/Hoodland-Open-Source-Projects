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
     * Primary constructor for the Memoir Java Wrapper. This contains a Kotlin Memoir but does not extend it.
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

    public Memoir() {
        this(Constants.UNKNOWN, null, null, true, true, null);
    }

    public Memoir(HeaderFunction headerFunction) {
        this(Constants.UNKNOWN, null, null, true, true, headerFunction);
    }

    public Memoir(String title) {
        this(title, null, null, true, true, null);
    }

    public Memoir(String title, HeaderFunction headerFunction) {
        this(title, null, null, true, true, headerFunction);
    }

    public Memoir(String title, PrintWriter forPlainText, PrintWriter forHTML) {
        this(title, forPlainText, forHTML, true, true, null);
    }

    public Memoir(String title, PrintWriter forPlainText, PrintWriter forHTML, HeaderFunction headerFunction) {
        this(title, forPlainText, forHTML, true, true, headerFunction);
    }

    public Memoir(String title, Boolean showTimestamps, Boolean showEmojis) {
        this(title, null, null, showTimestamps, showEmojis, null);
    }

    public Memoir(String title, Boolean showTimestamps, Boolean showEmojis, HeaderFunction headerFunction) {
        this(title, null, null, showTimestamps, showEmojis, headerFunction);
    }

    //=== Property Field Getters

    public String getTitle() { return KMemoir.getTitle(); }
    public PrintWriter getForPlainText() { return KMemoir.getForPlainText(); }
    public PrintWriter getForHTML() { return KMemoir.getForHTML(); }
    public Boolean getShowTimestamps() { return KMemoir.getShowTimestamps(); }
    public Boolean getShowEmojis() { return KMemoir.getShowEmojis(); }
    public Boolean wasUsed() { return KMemoir.getWasUsed(); }

    //=== Functions

    public String conclude() { return KMemoir.conclude(); }

    public void echoPlainText(String message, String emoji, LocalDateTime timestamp) {
        KMemoir.echoPlainText(message, emoji, timestamp);
    }

    public void echoPlainText(String message, String emoji) { echoPlainText(message, emoji, LocalDateTime.now()); }

    public void echoPlainText(String message, LocalDateTime timestamp) {
        echoPlainText(message, Constants.EMOJI_TEXT_BLANK_LINE, timestamp);
    }

    public void echoPlainText(String message) {
        echoPlainText(message, Constants.EMOJI_TEXT_BLANK_LINE, LocalDateTime.now());
    }

    public void writeToHTML(String message, String emoji, LocalDateTime timestamp) {
        KMemoir.writeToHTML(message, emoji, timestamp);
    }

    public void writeToHTML(String message, String emoji) { writeToHTML(message, emoji, LocalDateTime.now()); }

    public void writeToHTML(String message, LocalDateTime timestamp) {
        writeToHTML(message, Constants.EMOJI_TEXT_BLANK_LINE, timestamp);
    }

    public void writeToHTML(String message) {
        writeToHTML(message, Constants.EMOJI_TEXT_BLANK_LINE, LocalDateTime.now());
    }

    public void info(String message, String emoji) { KMemoir.info(message, emoji); }
    public void info(String message) { info(message, Constants.EMOJI_TEXT_BLANK_LINE); }
    public void debug(String message) { KMemoir.debug(message); }
    public void error(String message) { KMemoir.error(message); }
    public void skipLine() { KMemoir.skipLine(); }

    public String showMemoir(Memoir subordinate, String emoji, String style, int recurseLevel) {
        return KMemoir.showMemoir(subordinate.KMemoir, emoji, style, recurseLevel);
    }

    public String showMemoir(Memoir subordinate, String emoji, String style) {
        return showMemoir(subordinate, emoji, style, 0);
    }

    public String showMemoir(Memoir subordinate) {
        return showMemoir(subordinate, Constants.EMOJI_MEMOIR, "neutral", 0);
    }

    //=== ShowHttpMessages
    public void showHttpRequest(HttpRequest request, String bodyContentAsString, HttpFieldProcessingFunction function) {
        if (function == null) {
            ShowHttpMessagesKt.showHttpRequest(KMemoir, request, bodyContentAsString, null);
        } else {
            ShowHttpMessagesKt.showHttpRequest(KMemoir, request, bodyContentAsString, function::processField);
        }
    }

    public void showHttpRequest(HttpRequest request, String bodyContentAsString) {
        showHttpRequest(request, bodyContentAsString, null);
    }

    public void showHttpRequest(HttpRequest request, HttpFieldProcessingFunction function) {
        showHttpRequest(request, null, function);
    }

    public void showHttpRequest(HttpRequest request) {
        showHttpRequest(request, null, null);
    }

    public void showHttpResponse(HttpResponse response, HttpFieldProcessingFunction function) {
        if (function == null) {
            ShowHttpMessagesKt.showHttpResponse(KMemoir, response, null);
        } else {
            ShowHttpMessagesKt.showHttpResponse(KMemoir, response, function::processField);
        }
    }

    public void showHttpResponse(HttpResponse response) {
        showHttpResponse(response, null);
    }

    public void showHttpTransaction(HttpRequest request, String bodyContentAsString, HttpFieldProcessingFunction function) {
        if (function == null) {
            ShowHttpMessagesKt.showHttpTransaction(KMemoir, request, bodyContentAsString, null);
        } else {
            ShowHttpMessagesKt.showHttpTransaction(KMemoir, request, bodyContentAsString, function::processField);
        }
    }

    public void showHttpTransaction(HttpRequest request, String bodyContentAsString) {
        showHttpTransaction(request, bodyContentAsString, null);
    }

    public void showHttpTransaction(HttpRequest request, HttpFieldProcessingFunction function) {
        showHttpTransaction(request, null, function);
    }

    public void showHttpTransaction(HttpRequest request) {
        showHttpTransaction(request, null, null);
    }

    //=== ShowThrowable

    public String showThrowable(Throwable target, LocalDateTime timestamp, String plainTextIndent) {
        return ShowThrowableKt.showThrowable(KMemoir, target, timestamp, plainTextIndent);
    }

    public String showThrowable(Throwable target) {
        return showThrowable(target, LocalDateTime.now(), "");
    }

    public String showThrowable(Throwable target, LocalDateTime timestamp) {
        return showThrowable(target, timestamp, "");
    }

    public String showThrowable(Throwable target, String plainTextIndent) {
        return showThrowable(target, LocalDateTime.now(), plainTextIndent);
    }

    //=== Showing Objects

    public String showObject(Object target, String targetVariableName, int recurseLevel) {
        return ShowObjectKt.showObject(KMemoir, target, targetVariableName, recurseLevel);
    }

    public String showObject(Object target) {
        return showObject(target, Constants.NAMELESS, 0);
    }

    public String showObject(Object target, String targetVariableName) {
        return showObject(target, targetVariableName, 0);
    }

    public String showObject(Object target, int recurseLevel) {
        return showObject(target, Constants.NAMELESS, recurseLevel);
    }

    public String show(Object target, String targetVariableName, int recurseLevel) {
        return ShowCommonKt.show(KMemoir, target, targetVariableName, recurseLevel);
    }

    public String show(Object target) {
        return show(target, Constants.NAMELESS, 0);
    }

    public String show(Object target, String targetVariableName) {
        return show(target, targetVariableName, 0);
    }

    public String show(Object target, int recurseLevel) {
        return show(target, Constants.NAMELESS, recurseLevel);
    }

    public String showArray(Object[] target, String targetVariableName, int recurseLevel) {
        return ShowArrayKt.showArray(KMemoir, target, targetVariableName, recurseLevel);
    }

    public String showArray(Object[] target) {
        return showArray(target, Constants.NAMELESS, 0);
    }

    public String showArray(Object[] target, String targetVariableName) {
        return showArray(target, targetVariableName, 0);
    }

    public String showArray(Object[] target, int recurseLevel) {
        return showArray(target, Constants.NAMELESS, recurseLevel);
    }

    public String showPrimitiveArray(Object target, String targetVariableName, int recurseLevel) {
        return ShowArrayKt.showPrimitiveArray(KMemoir, target, targetVariableName, recurseLevel);
    }

    public String showPrimitiveArray(Object target) {
        return showPrimitiveArray(target, Constants.NAMELESS, 0);
    }

    public String showPrimitiveArray(Object target, String targetVariableName) {
        return showPrimitiveArray(target, targetVariableName, 0);
    }

    public String showPrimitiveArray(Object target, int recurseLevel) {
        return showPrimitiveArray(target, Constants.NAMELESS, recurseLevel);
    }

    public String showIterable(Iterable target, String targetVariableName, int recurseLevel) {
        return ShowIterableKt.showIterable(KMemoir, target, targetVariableName, recurseLevel);
    }

    public String showIterable(Iterable target) {
        return showIterable(target, Constants.NAMELESS, 0);
    }

    public String showIterable(Iterable target, String targetVariableName) {
        return showIterable(target, targetVariableName, 0);
    }

    public String showIterable(Iterable target, int recurseLevel) {
        return showIterable(target, Constants.NAMELESS, recurseLevel);
    }

    public String showMap(Map target, String targetVariableName, int recurseLevel, String targetClassName) {
        return ShowMapKt.showMap(KMemoir, target, targetVariableName, recurseLevel, targetClassName);
    }

    public String showMap(Map target) {
        return showMap(target, Constants.NAMELESS, 0, "Map");
    }

    public String showMap(Map target, String targetVariableName) {
        return showMap(target, targetVariableName, 0, "Map");
    }

    public String showMap(Map target, int recurseLevel) {
        return showMap(target, Constants.NAMELESS, recurseLevel, "Map");
    }

    public String showMap(Map target, String targetVariableName, String targetClassName) {
        return showMap(target, targetVariableName, 0, targetClassName);
    }

    public String showMap(Map target, int recurseLevel, String targetClassName) {
        return showMap(target, Constants.NAMELESS, recurseLevel, targetClassName);
    }
}
