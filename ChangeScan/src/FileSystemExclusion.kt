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

package hoodland.opensource.changescan

import hoodland.opensource.toolbox.UNSET_STRING
import java.io.File


class FilesystemExclusion(ChosenCategory: Categories, Description: String) {
    private var category = Categories.UNSET
    private var specifics: String = UNSET_STRING
    fun excludes(candidate: String): Boolean {
        when (category) {
            Categories.Directory -> if (File(candidate).isDirectory) {
                if (candidate == specifics) {
                    return true
                }
            }
            Categories.File -> if (File(candidate).isFile) {
                if (candidate == specifics) {
                    return true
                }
            }
            else -> return candidate.contains(specifics)
        }
        return false
    }

    override fun toString(): String {
        return "$category $specifics"
    }

    init {
        category = ChosenCategory
        specifics = Description
    }
}
