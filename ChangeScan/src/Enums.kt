// Copyright (c) 2020 William Arthur Hood
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

package hoodland.opensource.changescan

enum class Categories {
    UNSET,
    Directory,
    File,
    Pattern
}

enum class DifferenceTypes(val description: String) {
    CHECKSUM_DIFFERS("Checksum Differs"),
    CANDIDATE_LARGER("Increased in size"),
    CANDIDATE_SMALLER("Decreased in size"),
    ATTRIBUTES_DIFFER("Attributes Differ"), // This is a relic from the C# version
    CREATIONTIME_CANDIDATE_MORE_RECENT("Latest file created more recently"),
    CREATIONTIME_ORIGINAL_MORE_RECENT("Original file created more recently"),
    LASTACCESS_CANDIDATE_MORE_RECENT("Latest file accessed more recently"),
    LASTACCESS_ORIGINAL_MORE_RECENT("Original file accessed more recently"),
    LASTWRITE_CANDIDATE_MORE_RECENT("Latest file written to more recently"),
    LASTWRITE_ORIGINAL_MORE_RECENT("Original file written to more recently");

    override fun toString(): String {
        return description
    }
}