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

package hoodland.opensource.koarsegrind

/**
 * FilterType:
 * Used by a test runner program in constructing a test filter. Determines whether the filter describes
 * what tests to include or exclude. If both an include and exclude filter exist, include is applied first
 * then exclude is applied to the results of that. If only an exclude filter is used, it applies to all tests.
 */
enum class FilterType {
    INCLUDE, EXCLUDE
}

/**
 * FilterTarget:
 * Used by a test runner program in constructing a test filter. Determines if a filter applies to any of
 * a test's categories, its identifier, or its name.
 */
enum class FilterTarget {
    CATEGORIES, IDENTIFIERS, NAMES
}

/**
 * Filter:
 *
 * Used by a test runner program in constructing a test filter. Typically, many filters are combined into a FilterSet.
 *
 * @property filterType: Determines whether the filter describes what tests to include or exclude.
 * @property target: Determines if a filter applies to any of a test's categories, its identifier, or its name.
 * @property matchIfContains: The filter matches if the filter target contains any of these as a substring. In the case of categories, every category is checked against every one of these.
 */
class Filter(
        val filterType: FilterType,
        val target: FilterTarget,
        val matchIfContains: ArrayList<String>
) {
    fun isMatch(candidate: Test): Boolean {
        matchIfContains.forEach {
            when(target) {
                FilterTarget.CATEGORIES -> {
                    if (candidate.categoryPath != null) {
                        if (candidate.categoryPath.contains(it, true)) {
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

/**
 * FilterSet:
 *
 * Used by a test runner program to determine tests that do or do not run. All filters contained in this
 * set are applied against all tests in the classpath, and all manufactured
 * tests after they are created, to determine which tests will actually run (and thus which tests will be
 * considered in determining the overall status).
 *
 * @property filters: Add as many individual filters as desired. Be careful not to filter out every single available test.
 */
class FilterSet(val filters: ArrayList<Filter> = ArrayList<Filter>()) {

    /**
     * shouldRun:
     *
     * Used to evaluate if a test should be run and its results counted.
     *
     * @param candidate: The test under evaluation. If it included, then not excluded, it will run and its results will count against the overall status.
     * @return True if the given test should be run, and counted for results, based on the entire list of filters.
     */
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