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

package rockabilly.descriptions

import rockabilly.toolbox.nextLong
import java.time.LocalDate
import java.util.*

class DateFieldDescription() : FieldDescription<LocalDate>() {
    var target = DateFieldTargets.HAPPY_PATH
    private var bounds: DateLimitsDescription? = null

    constructor(limitsDescription: DateLimitsDescription?) : this() { bounds = limitsDescription }
    constructor(BasisValue: LocalDate, limitsDescription: DateLimitsDescription? = null): this(limitsDescription) { basisValue = BasisValue }

    val limits: DateLimitsDescription?
        get() = bounds

    val isLimited: Boolean
        get() = (bounds != null)

    override fun toString(): String {
        return if (target === DateFieldTargets.EXPLICIT) "$target ($basisValue)" else target.toString()
    }

    val positiveMinisculeValue: LocalDate
        get() = LocalDate.now().plusDays(1)

    val negativeMinisculeValue: LocalDate
        get() = LocalDate.now().minusDays(1)

    val positiveModerateValue: LocalDate
        get() = LocalDate.now().plusYears(1).plusMonths(6)

    val negativeModerateValue: LocalDate
        get() = LocalDate.now().minusYears(1).minusMonths(6)

    val maximumPossibleValue: LocalDate
        get() = LocalDate.MAX

    val minimumPossibleValue
        get() = LocalDate.MIN

    val present: LocalDate
        get() = LocalDate.now()

    fun middle(min: LocalDate, max: LocalDate): LocalDate {
        val delta = (max.toEpochDay() - min.toEpochDay()).div(2)
        return LocalDate.ofEpochDay(min.toEpochDay() + delta)
    }

    fun random(min: LocalDate, max: LocalDate): LocalDate {
        val delta = nextLong(Random(), max.toEpochDay() - min.toEpochDay())
        return LocalDate.ofEpochDay(min.toEpochDay() + delta)
    }

    val sizeInDays: Long
        get() = bounds!!.upperLimit.toEpochDay() - bounds!!.lowerLimit.toEpochDay()

    override val describedValue: LocalDate?
        get() {
            when (target) {
                DateFieldTargets.EXPLICIT -> {
                    if (basisValue == null) throw InappropriateDescriptionException()
                    return basisValue
                }
                DateFieldTargets.HAPPY_PATH -> {
                    if (basisValue == null) {
                        if (!isLimited) throw InappropriateDescriptionException()
                        return middle(bounds!!.lowerLimit, bounds!!.upperLimit)
                    }
                    return basisValue
                }
                DateFieldTargets.MAXIMUM_POSSIBLE_VALUE -> return maximumPossibleValue
                DateFieldTargets.MINIMUM_POSSIBLE_VALUE -> return minimumPossibleValue
                DateFieldTargets.AT_LOWER_LIMIT -> return bounds!!.lowerLimit
                DateFieldTargets.AT_UPPER_LIMIT -> return bounds!!.upperLimit
                DateFieldTargets.AT_PRESENT -> return present
                DateFieldTargets.NULL -> return null
                DateFieldTargets.RANDOM_WITHIN_LIMITS -> return random(bounds!!.lowerLimit.plusDays(1), bounds!!.upperLimit.minusDays(1))
                DateFieldTargets.SLIGHTLY_IN_FUTURE -> return positiveMinisculeValue
                DateFieldTargets.SLIGHTLY_IN_PAST -> return negativeMinisculeValue
                DateFieldTargets.SLIGHTLY_BEYOND_LOWER_LIMIT -> return bounds!!.lowerLimit.minusDays(1)
                DateFieldTargets.SLIGHTLY_BEYOND_UPPER_LIMIT -> return bounds!!.upperLimit.plusDays(1)
                DateFieldTargets.SLIGHTLY_WITHIN_LOWER_LIMIT -> return bounds!!.lowerLimit.plusDays(1)
                DateFieldTargets.SLIGHTLY_WITHIN_UPPER_LIMIT -> return bounds!!.upperLimit.minusDays(1)
                DateFieldTargets.WELL_IN_FUTURE -> return positiveModerateValue
                DateFieldTargets.WELL_IN_PAST -> return negativeModerateValue
                DateFieldTargets.WELL_BEYOND_LOWER_LIMIT -> return bounds!!.lowerLimit.minusDays(400)
                DateFieldTargets.WELL_BEYOND_UPPER_LIMIT -> return bounds!!.upperLimit.plusDays(400)
                DateFieldTargets.WELL_WITHIN_LOWER_LIMIT -> return bounds!!.lowerLimit.plusDays(400)
                DateFieldTargets.WELL_WITHIN_UPPER_LIMIT -> return bounds!!.upperLimit.minusDays(400)
                DateFieldTargets.SLIGHTLY_ABOVE_MINIMUM -> return minimumPossibleValue.plusDays(1)
                DateFieldTargets.SLIGHTLY_BELOW_MAXIMUM -> return maximumPossibleValue.minusDays(1)
                DateFieldTargets.FIVE_DIGIT_YEAR -> return LocalDate.of(10000 + present.year, present.month, present.dayOfMonth)
                DateFieldTargets.FOUR_DIGIT_YEAR -> return present
                DateFieldTargets.THREE_DIGIT_YEAR -> return LocalDate.of(789, present.month, present.dayOfMonth)
                DateFieldTargets.TWO_DIGIT_YEAR -> return LocalDate.of(79, present.month, present.dayOfMonth)
                DateFieldTargets.SINGLE_DIGIT_YEAR -> return LocalDate.of(7, present.month, present.dayOfMonth)
                DateFieldTargets.TWO_DIGIT_MONTH -> return LocalDate.of(present.year, 11, present.dayOfMonth)
                DateFieldTargets.SINGLE_DIGIT_MONTH -> return LocalDate.of(present.year, 7, present.dayOfMonth)
                DateFieldTargets.TWO_DIGIT_DAY -> return LocalDate.of(present.year, present.month, 14)
                DateFieldTargets.SINGLE_DIGIT_DAY -> return LocalDate.of(present.year, present.month, 7)
                DateFieldTargets.SINGLE_DIGIT_MONTH_AND_DAY -> return LocalDate.of(present.year, 5, 3)
                else -> throw NoValueException()
            }
        }

    override fun hasSpecificHappyValue(): Boolean {
        return target === DateFieldTargets.HAPPY_PATH && basisValue != null
    }

    override val isExplicit: Boolean
        get() = (target === DateFieldTargets.EXPLICIT)

    override val isDefault: Boolean
    get() = (target === DateFieldTargets.DEFAULT)

    override fun setExplicitValue(value: LocalDate) {
        basisValue = value
        target = DateFieldTargets.EXPLICIT
    }
}