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

package hoodland.opensource.koarsegrind


/**
 * NameCollisionException: This Exception will be thrown if any test shares a name with another test. This is considered a "preclusion" in that Koarse Grind will decline to run the suite if it is thrown.
 *
 * @constructor
 *
 * @param collidedName The name that was used more than once.
 */
class NameCollisionException(collidedName: String) : Exception("It is prohibited for two tests to have the same: $collidedName")


/**
 * IdentifierCollisionException: This Exception will be thrown if any test shares an identifier with another test. This is considered a "preclusion" in that Koarse Grind will decline to run the suite if it is thrown.
 *
 * @constructor
 *
 * @param collidedIdentifier The identifier that was used more than once.
 */
class IdentifierCollisionException(collidedIdentifier: String) : Exception("It is prohibited for two tests to have the same identifier (other than null): $collidedIdentifier")




class DuplicateOutfitterException(msg: String): Exception(msg) { }
class StrayOutfitterException(msg: String): Exception(msg) { }

