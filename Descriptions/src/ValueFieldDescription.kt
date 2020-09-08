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

package hoodland.opensource.descriptions

abstract class ValueFieldDescription<T>(basisValue: T, val limits: LimitsDescription<T>)
    : FieldDescription<T>(basisValue) {
    var target: ValueFieldTargets = ValueFieldTargets.HAPPY_PATH

    override fun toString(): String {
        return if (target === ValueFieldTargets.EXPLICIT) "${target.toString()} ($basisValue)" else target.toString()
    }

    abstract val positiveMinisculeValue: T
    abstract val positiveModerateValue: T
    abstract val maximumPossibleValue: T
    abstract val minimumPossibleValue: T
    abstract val zero: T
    val origin = zero
    abstract fun add(x: T, y: T): T
    abstract fun subtract(x: T, y: T): T
    abstract fun multiply(x: T, y: T): T
    abstract fun divide(x: T, y: T): T
    abstract fun half(x: T): T
    abstract fun random(min: T, max: T): T

    val span: T
        get() = subtract(limits.upper, limits.lower)

    val negativeMinisculeValue: T
        get() = subtract(zero, positiveMinisculeValue)

    val negativeModerateValue: T
        get() = subtract(zero, positiveModerateValue)

    override val describedValue: T?
        get() {
            when (target) {
                ValueFieldTargets.EXPLICIT -> {
                    // Shouldn't be possible, but keeping for robustness...
                    if (basisValue == null) throw InappropriateDescriptionException()
                    return basisValue
                }
                ValueFieldTargets.HAPPY_PATH -> {
                    if (basisValue == null) {
                        // Shouldn't be possible, but keeping for robustness...
                        return add(limits.lower, half(span))
                    }
                    return basisValue
                }
                ValueFieldTargets.MAXIMUM_POSSIBLE_VALUE -> return maximumPossibleValue
                ValueFieldTargets.MINIMUM_POSSIBLE_VALUE -> return minimumPossibleValue
                ValueFieldTargets.AT_LOWER_LIMIT -> return limits.lower
                ValueFieldTargets.AT_UPPER_LIMIT -> return limits.upper
                ValueFieldTargets.AT_ZERO -> return zero
                ValueFieldTargets.NULL -> return null
                ValueFieldTargets.RANDOM_WITHIN_LIMITS -> return random(add(limits.lower, half(half(span))),
                        subtract(limits.upper, half(half(span))))
                ValueFieldTargets.SLIGHTLY_ABOVE_ZERO -> return positiveMinisculeValue
                ValueFieldTargets.SLIGHTLY_BELOW_ZERO -> return negativeMinisculeValue
                ValueFieldTargets.SLIGHTLY_BEYOND_LOWER_LIMIT -> return subtract(limits.lower, positiveMinisculeValue)
                ValueFieldTargets.SLIGHTLY_BEYOND_UPPER_LIMIT -> return add(limits.upper, positiveMinisculeValue)
                ValueFieldTargets.SLIGHTLY_WITHIN_LOWER_LIMIT -> return add(limits.lower, positiveMinisculeValue)
                ValueFieldTargets.SLIGHTLY_WITHIN_UPPER_LIMIT -> return subtract(limits.upper, positiveMinisculeValue)
                ValueFieldTargets.WELL_ABOVE_ZERO -> return positiveModerateValue
                ValueFieldTargets.WELL_BELOW_ZERO -> return negativeModerateValue
                ValueFieldTargets.WELL_BEYOND_LOWER_LIMIT -> return subtract(limits.lower, positiveModerateValue)
                ValueFieldTargets.WELL_BEYOND_UPPER_LIMIT -> return add(limits.upper, positiveModerateValue)
                ValueFieldTargets.WELL_WITHIN_LOWER_LIMIT -> return add(limits.lower, positiveModerateValue)
                ValueFieldTargets.WELL_WITHIN_UPPER_LIMIT -> return subtract(limits.upper, positiveModerateValue)
                ValueFieldTargets.SLIGHTLY_ABOVE_MINIMUM -> return add(minimumPossibleValue, positiveMinisculeValue)
                ValueFieldTargets.SLIGHTLY_BELOW_MAXIMUM -> return subtract(maximumPossibleValue,
                        positiveMinisculeValue)
            }
            throw NoValueException()
        }

    override val hasSpecificHappyValue: Boolean
            get() = (target === ValueFieldTargets.HAPPY_PATH) && (basisValue != null)

    override val isExplicit: Boolean
        get() = target === ValueFieldTargets.EXPLICIT

    override val isDefault: Boolean
        get() = target === ValueFieldTargets.DEFAULT

    override fun useExplicitValue(value: T?) {
        // Nulls are not allowed to be the basis for Value Field Descriptions
        if (basisValue == null) throw InappropriateDescriptionException()
        basisValue = value
        target = ValueFieldTargets.EXPLICIT
    }
}