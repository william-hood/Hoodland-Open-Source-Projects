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

abstract class ValueFieldDescription<T>() : FieldDescription<T>() {
    var target: ValueFieldTargets = ValueFieldTargets.HAPPY_PATH
    private var bounds: LimitsDescription<T>? = null
    val limits: LimitsDescription<T>?
        get() = bounds

    constructor(limitsDescription: LimitsDescription<T>?) : this() {
        bounds = limitsDescription
    }

    constructor(BasisValue: T) : this() {
        basisValue = BasisValue
    }

    constructor(BasisValue: T, limitsDescription: LimitsDescription<T>?) : this() {
        basisValue = BasisValue
        bounds = limitsDescription
    }

    val isLimited: Boolean
        get() = bounds != null

    override fun toString(): String {
        return if (target === ValueFieldTargets.EXPLICIT) target.toString().toString() + " (" + basisValue + ")" else target.toString()
    }

    @get:Throws(InappropriateDescriptionException::class)
    abstract val positiveMinisculeValue: T

    @get:Throws(InappropriateDescriptionException::class)
    abstract val positiveModerateValue: T

    @get:Throws(InappropriateDescriptionException::class)
    abstract val maximumPossibleValue: T

    @get:Throws(InappropriateDescriptionException::class)
    abstract val minimumPossibleValue: T

    @get:Throws(InappropriateDescriptionException::class)
    abstract val zeroOrOrigin: T

    @Throws(InappropriateDescriptionException::class)
    abstract fun add(x: T, y: T): T

    @Throws(InappropriateDescriptionException::class)
    abstract fun subtract(x: T, y: T): T

    @Throws(InappropriateDescriptionException::class)
    abstract fun multiply(x: T, y: T): T

    @Throws(InappropriateDescriptionException::class)
    abstract fun divide(x: T, y: T): T

    @Throws(InappropriateDescriptionException::class)
    abstract fun half(x: T): T

    @Throws(InappropriateDescriptionException::class)
    abstract fun random(min: T, max: T): T

    @Throws(InappropriateDescriptionException::class)
    fun size(): T {
        return subtract(bounds!!.upperLimit, bounds!!.lowerLimit)
    }

    @get:Throws(InappropriateDescriptionException::class)
    val negativeMinisculeValue: T
        get() = subtract(zeroOrOrigin, positiveMinisculeValue)

    @get:Throws(InappropriateDescriptionException::class)
    val negativeModerateValue: T
        get() = subtract(zeroOrOrigin, positiveModerateValue)

    @get:Throws(NoValueException::class, InappropriateDescriptionException::class)
    override val describedValue: T?
        get() {
            when (target) {
                ValueFieldTargets.EXPLICIT -> {
                    if (basisValue == null) throw InappropriateDescriptionException()
                    return basisValue
                }
                ValueFieldTargets.HAPPY_PATH -> {
                    if (basisValue == null) {
                        if (!isLimited) throw InappropriateDescriptionException()
                        return add(bounds!!.lowerLimit, half(size()))
                    }
                    return basisValue
                }
                ValueFieldTargets.MAXIMUM_POSSIBLE_VALUE -> return maximumPossibleValue
                ValueFieldTargets.MINIMUM_POSSIBLE_VALUE -> return minimumPossibleValue
                ValueFieldTargets.AT_LOWER_LIMIT -> return bounds!!.lowerLimit
                ValueFieldTargets.AT_UPPER_LIMIT -> return bounds!!.upperLimit
                ValueFieldTargets.AT_ZERO -> return zeroOrOrigin
                ValueFieldTargets.NULL -> return null
                ValueFieldTargets.RANDOM_WITHIN_LIMITS -> return random(add(bounds!!.lowerLimit, half(half(size()))),
                        subtract(bounds!!.upperLimit, half(half(size()))))
                ValueFieldTargets.SLIGHTLY_ABOVE_ZERO -> return positiveMinisculeValue
                ValueFieldTargets.SLIGHTLY_BELOW_ZERO -> return negativeMinisculeValue
                ValueFieldTargets.SLIGHTLY_BEYOND_LOWER_LIMIT -> return subtract(bounds!!.lowerLimit, positiveMinisculeValue)
                ValueFieldTargets.SLIGHTLY_BEYOND_UPPER_LIMIT -> return add(bounds!!.upperLimit, positiveMinisculeValue)
                ValueFieldTargets.SLIGHTLY_WITHIN_LOWER_LIMIT -> return add(bounds!!.lowerLimit, positiveMinisculeValue)
                ValueFieldTargets.SLIGHTLY_WITHIN_UPPER_LIMIT -> return subtract(bounds!!.upperLimit, positiveMinisculeValue)
                ValueFieldTargets.WELL_ABOVE_ZERO -> return positiveModerateValue
                ValueFieldTargets.WELL_BELOW_ZERO -> return negativeModerateValue
                ValueFieldTargets.WELL_BEYOND_LOWER_LIMIT -> return subtract(bounds!!.lowerLimit, positiveModerateValue)
                ValueFieldTargets.WELL_BEYOND_UPPER_LIMIT -> return add(bounds!!.upperLimit, positiveModerateValue)
                ValueFieldTargets.WELL_WITHIN_LOWER_LIMIT -> return add(bounds!!.lowerLimit, positiveModerateValue)
                ValueFieldTargets.WELL_WITHIN_UPPER_LIMIT -> return subtract(bounds!!.upperLimit, positiveModerateValue)
                ValueFieldTargets.SLIGHTLY_ABOVE_MINIMUM -> return add(minimumPossibleValue, positiveMinisculeValue)
                ValueFieldTargets.SLIGHTLY_BELOW_MAXIMUM -> return subtract(maximumPossibleValue,
                        positiveMinisculeValue)
            }
            throw NoValueException()
        }

    override fun hasSpecificHappyValue(): Boolean {
        return target === ValueFieldTargets.HAPPY_PATH && basisValue != null
    }

    override val isExplicit: Boolean
        get() = target === ValueFieldTargets.EXPLICIT

    override val isDefault: Boolean
        get() = target === ValueFieldTargets.DEFAULT

    override fun setExplicitValue(value: T) {
        basisValue = value
        target = ValueFieldTargets.EXPLICIT
    }
}