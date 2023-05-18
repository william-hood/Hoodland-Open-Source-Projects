// Copyright (c) 2023 William Arthur Hood
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

package hoodland.opensource.toolbox.java;

import hoodland.opensource.toolbox.StatusCodeDescriptionKt;

/**
 * Used for converting an integer HTTP status code to a concise text description.
 */
public class StatusCodeDescription {

    /**
     * isInvalidStatusCode: Returns true if this Int DOES NOT represent a valid status code number. (Either less than 100 or greater than 599)
     */
    public static Boolean isInvalidStatusCode(int value) {
        return StatusCodeDescriptionKt.isInvalidStatusCode(value);
    }

    /**
     * isValidStatusCode: Returns true if this Int represents a valid status code number. (Between 100 & 599)
     */
    public static Boolean isValidStatusCode(int value) {
        return StatusCodeDescriptionKt.isValidStatusCode(value);
    }

    /**
     * isSuccessfulStatusCode: Returns true if the status code is a 2xx. Note that an invalid status code is neither successful nor an error.
     */
    public static Boolean isSuccessfulStatusCode(int value) {
        return StatusCodeDescriptionKt.isSuccessfulStatusCode(value);
    }

    /**
     * isErrorStatusCode: Returns true if the status code is a 4xx or 5xx. Note that an invalid status code is neither successful nor an error.
     */
    public static Boolean isErrorStatusCode(int value) {
        return StatusCodeDescriptionKt.isErrorStatusCode(value);
    }

    /**
     * StatusCodeDescription.of(): Treats this Int as an HTTP status code and tries to supply a string description of its meaning.
     * @param value The status code integer value to operate on.
     * @return If the Int is a known status code, this returns a string representation of that code's standard description.
     * Returns an empty string ("") if it does not have a matching description to supply.
     */
    public static String of(int value) {
        return StatusCodeDescriptionKt.toStatusCodeDescription(value);
    }
}
