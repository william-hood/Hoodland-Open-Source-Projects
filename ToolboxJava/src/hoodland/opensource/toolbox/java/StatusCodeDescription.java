package hoodland.opensource.toolbox.java;

import hoodland.opensource.toolbox.StatusCodeDescriptionKt;

public class StatusCodeDescription {
    public static Boolean isInvalidStatusCode(int value) {
        return StatusCodeDescriptionKt.isInvalidStatusCode(value);
    }

    public static Boolean isValidStatusCode(int value) {
        return StatusCodeDescriptionKt.isValidStatusCode(value);
    }

    public static Boolean isSuccessfulStatusCode(int value) {
        return StatusCodeDescriptionKt.isSuccessfulStatusCode(value);
    }

    public static Boolean isErrorStatusCode(int value) {
        return StatusCodeDescriptionKt.isErrorStatusCode(value);
    }

    public static String of(int value) {
        return StatusCodeDescriptionKt.toStatusCodeDescription(value);
    }
}
