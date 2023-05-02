package hoodland.opensource.memoir.java;

import hoodland.opensource.memoir.StringHandlingKt;

public class StringHandler {
    public static Boolean isBase64(String candidate) {
        return StringHandlingKt.isBase64(candidate);
    }

    public static String fromBase64(String candidate) {
        return StringHandlingKt.fromBase64(candidate);
    }

    public static String treatAsCode(String value) {
        return StringHandlingKt.treatAsCode(value);
    }

    public static String processString(String fieldName, String fieldValue, HttpFieldProcessingFunction function) {
        if (function == null) {
            return StringHandlingKt.processString(fieldName, fieldValue, null);
        } else {
            return StringHandlingKt.processString(fieldName, fieldValue, function::processField);
        }
    }
}
