package hoodland.opensource.memoir.java;

import hoodland.opensource.memoir.StringHandlingKt;

public class StringHandler {
    /**
     * isBase64: This function attempts to determine if a given string can be decoded as Base64. PROBLEM: There appear to
     * be some small english strings that technically can decode as Base64 (producing garbage).
     *
     * @param candidate The string to check if it looks like Base64
     * @return Returns true if the candidate string appears to be Base64.
     */
    public static Boolean isBase64(String candidate) {
        return StringHandlingKt.isBase64(candidate);
    }

    /**
     * fromBase64: Decodes a Base64 encoded string
     *
     * @param candidate The string to treat as Base64.
     * @return Returns the resulting decoded string.
     */
    public static String fromBase64(String candidate) {
        return StringHandlingKt.fromBase64(candidate);
    }

    /**
     * treatAsCode: Wraps the given string with HTML tags that prevent rendering of any HTML by the browser and
     * display the value as a code sample.
     *
     * @param value The string value to be wrapped as a code sample.
     * @return The wrapped string. Specifically "<pre><code><xmp>$value</xmp></code></pre>".
     */
    public static String treatAsCode(String value) {
        return StringHandlingKt.treatAsCode(value);
    }

// It is left up to the end user as to when a field should be base64 decoded or pretty-printed.
    /**
     * processString: This will be called for nearly every message or string field that Memoir tries to log. Any processing
     * necessary before actually logging the string to HTML occurs here. In certain cases, particularly headers and body
     * of HTTP messages, it may be desirable to Base64 decode and/or JSON pretty-print the string. Because it can be difficult
     * to tell which fields really are Base64 or JSON, this task is left to the client code by way of the callbackFunction
     * parameter. When rendering HTTP messages, the fieldName parameter of the callback will be the header name, or
     * the constant HTTP_MESSAGE_BODY for the message body/payload. Make any necessary changes to the supplied field and return
     * the changed value with your callbackFunction. Return the field as it was sent if no changes are needed. Do not
     * supply a callbackFunction if none is needed.
     *
     * @param fieldName The name of the field being processed.
     * @param fieldValue The actual string value being processed.
     * @param callbackFunction Optional: Supply a callback function (via a class implementing the HttpFieldProcessingFunction interface) to make on-the-fly changes to certain fields, such as decoding Base64 or pretty-printing JSON.
     * @return Returns the string after processing.
     */
    public static String processString(String fieldName, String fieldValue, HttpFieldProcessingFunction callbackFunction) {
        if (callbackFunction == null) {
            return StringHandlingKt.processString(fieldName, fieldValue, null);
        } else {
            return StringHandlingKt.processString(fieldName, fieldValue, callbackFunction::processField);
        }
    }
}