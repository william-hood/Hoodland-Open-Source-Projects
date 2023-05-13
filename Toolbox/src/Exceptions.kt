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

package hoodland.opensource.toolbox

/**
 * ImpossibleCodePathException: I have sometimes had cause to believe that control flow had reached a point in
 * the code that should not be possible. Throw this at such a point to remove any doubt.
 */
class ImpossibleCodePathException : Exception("A point in the code was reached that should not be possible to get to.")

/**
 * ImproperConstructionException: Throw this in situations where an object or class has been constructed in
 * such a way as to make it unusable. Example: An array needs to be passed in but the array given was size
 * zero, or is populated with known bad data.
 *
 * @constructor
 *
 * @param message Explain why the object or class is unusable.
 */
class ImproperConstructionException(message: String) : Exception("An object or class was created in a way that precludes actual use: $message")

