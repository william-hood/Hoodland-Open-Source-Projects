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

package hoodland.opensource.descriptions

enum class DateFieldTargets {
    DEFAULT, NULL, HAPPY_PATH, EXPLICIT, MAXIMUM_POSSIBLE_VALUE, RANDOM_WITHIN_LIMITS, SLIGHTLY_BELOW_MAXIMUM, SLIGHTLY_ABOVE_MINIMUM, MINIMUM_POSSIBLE_VALUE, WELL_BEYOND_UPPER_LIMIT, SLIGHTLY_BEYOND_UPPER_LIMIT, AT_UPPER_LIMIT, SLIGHTLY_WITHIN_UPPER_LIMIT, WELL_WITHIN_UPPER_LIMIT, WELL_IN_FUTURE, SLIGHTLY_IN_FUTURE, AT_PRESENT, SLIGHTLY_IN_PAST, WELL_IN_PAST, WELL_BEYOND_LOWER_LIMIT, SLIGHTLY_BEYOND_LOWER_LIMIT, AT_LOWER_LIMIT, SLIGHTLY_WITHIN_LOWER_LIMIT, WELL_WITHIN_LOWER_LIMIT, FIVE_DIGIT_YEAR, FOUR_DIGIT_YEAR, THREE_DIGIT_YEAR, TWO_DIGIT_YEAR, SINGLE_DIGIT_YEAR, TWO_DIGIT_MONTH, SINGLE_DIGIT_MONTH, TWO_DIGIT_DAY, SINGLE_DIGIT_DAY, SINGLE_DIGIT_MONTH_AND_DAY;

    override fun toString(): String {
        when (this) {
            AT_LOWER_LIMIT -> return "At Lower Limit"
            AT_UPPER_LIMIT -> return "At Upper Limit"
            AT_PRESENT -> return "Now"
            EXPLICIT -> return "Explicit Value"
            HAPPY_PATH -> return "Happy Path"
            MAXIMUM_POSSIBLE_VALUE -> return "Maximum Possible"
            MINIMUM_POSSIBLE_VALUE -> return "Minimum Possible"
            NULL -> return "Explicit Null"
            RANDOM_WITHIN_LIMITS -> return "Random Within Limits"
            SLIGHTLY_ABOVE_MINIMUM -> return "Slightly Above Minimum"
            SLIGHTLY_IN_FUTURE -> return "Slightly into the Future"
            SLIGHTLY_BELOW_MAXIMUM -> return "Slightly Below Maximum"
            SLIGHTLY_IN_PAST -> return "Slightly into the Past"
            SLIGHTLY_BEYOND_LOWER_LIMIT -> return "Slightly Beyond Lower Limit"
            SLIGHTLY_BEYOND_UPPER_LIMIT -> return "Slightly Beyond Upper Limit"
            SLIGHTLY_WITHIN_LOWER_LIMIT -> return "Slightly Within Lower Limit"
            SLIGHTLY_WITHIN_UPPER_LIMIT -> return "Slightly Within Upper Limit"
            WELL_IN_FUTURE -> return "Well into the Future"
            WELL_IN_PAST -> return "Well into the Past"
            WELL_BEYOND_LOWER_LIMIT -> return "Well Beyond Lower Limit"
            WELL_BEYOND_UPPER_LIMIT -> return "Well Beyond Upper Limit"
            WELL_WITHIN_LOWER_LIMIT -> return "Well Within Lower Limit"
            WELL_WITHIN_UPPER_LIMIT -> return "Well Within Upper Limit"
            FIVE_DIGIT_YEAR -> return "Five Digit Year"
            FOUR_DIGIT_YEAR -> return "Four Digit Year"
            THREE_DIGIT_YEAR -> return "Three Digit Year"
            TWO_DIGIT_YEAR -> return "Two Digit Year"
            SINGLE_DIGIT_YEAR -> return "Single Digit Year"
            TWO_DIGIT_MONTH -> return "Two Digit Month"
            SINGLE_DIGIT_MONTH -> return "Single Digit Month"
            TWO_DIGIT_DAY -> return "Two Digit Day"
            SINGLE_DIGIT_DAY -> return "Single Digit Day"
            SINGLE_DIGIT_MONTH_AND_DAY -> return "Single Digit Month and Day"
            else -> return "Left Default"
        }
    }

    val isLimitRelevant: Boolean
        get() {
            when (this) {
                WELL_BEYOND_LOWER_LIMIT, WELL_BEYOND_UPPER_LIMIT, WELL_WITHIN_LOWER_LIMIT, WELL_WITHIN_UPPER_LIMIT, SLIGHTLY_BEYOND_LOWER_LIMIT, SLIGHTLY_BEYOND_UPPER_LIMIT, SLIGHTLY_WITHIN_LOWER_LIMIT, SLIGHTLY_WITHIN_UPPER_LIMIT, RANDOM_WITHIN_LIMITS, AT_LOWER_LIMIT, AT_UPPER_LIMIT -> return true
                else -> return false
            }
        }

    val isDigitRelevant: Boolean
        get() {
            when (this) {
                FIVE_DIGIT_YEAR, FOUR_DIGIT_YEAR, THREE_DIGIT_YEAR, TWO_DIGIT_YEAR, SINGLE_DIGIT_YEAR, TWO_DIGIT_MONTH, SINGLE_DIGIT_MONTH, TWO_DIGIT_DAY, SINGLE_DIGIT_DAY, SINGLE_DIGIT_MONTH_AND_DAY -> return true
                else -> return false
            }
        }

    val isHappyOrExplicit: Boolean
        get() {
            if (this == HAPPY_PATH) return true
            return if (this == EXPLICIT) true else false
        }
}