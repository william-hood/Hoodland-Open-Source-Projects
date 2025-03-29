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

package hoodland.opensource.toolbox
/*
	 * from http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html
	 *
      - 1xx: Informational - Request received, continuing process
      - 2xx: Success - The action was successfully received,
        understood, and accepted
      - 3xx: Redirection - Further action must be taken in order to
        complete the request
      - 4xx: Client Error - The request contains bad syntax or cannot
        be fulfilled
      - 5xx: Server Error - The server failed to fulfill an apparently
        valid request
	 */

/**
 * isInvalidStatusCode: Returns true if this Int DOES NOT represent a valid status code number. (Either less than 100 or greater than 599)
 */
val Int.isInvalidStatusCode
    get() = (this < 100 || this > 599)

/**
 * isValidStatusCode: Returns true if this Int represents a valid status code number. (Between 100 & 599)
 */
val Int.isValidStatusCode
get() = !this.isInvalidStatusCode

/**
 * isSuccessfulStatusCode: Returns true if the status code is a 2xx. Note that an invalid status code is neither successful nor an error.
 */
val Int.isSuccessfulStatusCode
get() = this.isValidStatusCode && (this.toString()[0] == '2')

/**
 * isErrorStatusCode: Returns true if the status code is a 4xx or 5xx. Note that an invalid status code is neither successful nor an error.
 */
val Int.isErrorStatusCode
    get() = this.isValidStatusCode && ((this.toString()[0] == '4') || (this.toString()[0] == '5'))

/**
 * toStatusCodeDescription: Treats this Int as an HTTP status code and tries to supply a string description of its meaning.
 *
 * @return If the Int is a known status code, this returns a string representation of that code's standard description.
 * Returns an empty string ("") if it does not have a matching description to supply.
 */
fun Int.toStatusCodeDescription(): String {
    return when (this) {
        100 -> "Continue"
        200 -> "OK"
        201 -> "Created"
        202 -> "Accepted"
        203 -> "Non-authoritative Information"
        204 -> "No Content"
        205 -> "Reset Content"
        206 -> "Partial Content"
        300 -> "Multiple Choices"
        301 -> "Moved Permanently"
        302 -> "Found"
        303 -> "See Other"
        304 -> "Not Modified"
        305 -> "Use Proxy"
        306 -> "Unused"
        307 -> "Temporary Redirect"
        400 -> "Bad Request"
        401 -> "Unauthorized"
        402 -> "Payment Required"
        403 -> "Forbidden"
        404 -> "Not Found"
        405 -> "Method Not Allowed"
        406 -> "Not Acceptable"
        407 -> "Proxy Authentication Required"
        408 -> "Request Timeout"
        409 -> "Conflict"
        410 -> "Gone"
        411 -> "Length Required"
        412 -> "Precondition Failed"
        413 -> "Request Entity Too Large"
        414 -> "Request-url Too Long"
        415 -> "Unsupported Media Type"
        416 -> "Requested Range Not Satisfiable"
        417 -> "Expectation Failed"
        500 -> "Internal Server Error"
        501 -> "Not Implemented"
        502 -> "Bad Gateway"
        503 -> "Service Unavailable"
        504 -> "Gateway Timeout"
        505 -> "HTTP Version Not Supported"
        else -> ""
    }
}