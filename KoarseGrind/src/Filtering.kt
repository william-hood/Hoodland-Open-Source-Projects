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

package hoodland.opensource.koarsegrind

enum class FilterType {
    INCLUDE, EXCLUDE
}

enum class FilterTarget {
    CATEGORIES, IDENTIFIERS, NAMES
}

class Filter(
        val filterType: FilterType,
        val target: FilterTarget,
        val matchIfContains: ArrayList<String>
) {
    fun isMatch(candidate: Test): Boolean {
        matchIfContains.forEach {
            when(target) {
                FilterTarget.CATEGORIES -> {
                    candidate.categories.forEach { thisCategory ->
                        if (thisCategory.contains(it, true)) {
                            return true
                        }
                    }
                }
                FilterTarget.IDENTIFIERS -> if (candidate.identifier.contains(it, true)) { return true }
                FilterTarget.NAMES -> if (candidate.name.contains(it, true)) { return true }
            }
        }

        return false
    }
}

class FilterSet(val filters: ArrayList<Filter> = ArrayList<Filter>()) {
    fun shouldRun(candidate: Test): Boolean {
        if (includes(candidate)) {
            if (doesNotExclude(candidate)) {
                return true
            }
        }

        return false
    }

    // Pass 1: Candidate must match at least one INCLUDE filters
    private fun includes(candidate: Test): Boolean {
        var tally = 0
        filters.forEach {
            if (it.filterType === FilterType.INCLUDE) {
                tally++
                if (it.isMatch(candidate)) { return true }
            }
        }

        // Return true if there were no INCLUDE filters. False otherwise.
        return (tally < 1)
    }

    // Pass 2: Candidate must NOT match even one of the EXCLUDE filters
    private  fun doesNotExclude(candidate: Test): Boolean {
        filters.forEach {
            if (it.filterType === FilterType.EXCLUDE) {
                if (it.isMatch(candidate)) { return false }
            }
        }

        return true
    }
}