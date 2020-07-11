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

import java.util.*

class FloatFieldDescription : ValueFieldDescription<Float> {
    constructor(limitsDescription: LimitsDescription<Float>) : super(limitsDescription) {}
    constructor() : super() {}
    constructor(BasisValue: Float) : super(BasisValue) {}

    @get:Throws(InappropriateDescriptionException::class)
    override val positiveMinisculeValue: Float
        get() = 1.toFloat()

    @get:Throws(InappropriateDescriptionException::class)
    override val positiveModerateValue: Float
        get() = 100.toFloat()

    @get:Throws(InappropriateDescriptionException::class)
    override val maximumPossibleValue: Float
        get() = Float.MAX_VALUE

    @get:Throws(InappropriateDescriptionException::class)
    override val minimumPossibleValue: Float
        get() = Float.MIN_VALUE

    @get:Throws(InappropriateDescriptionException::class)
    override val zeroOrOrigin: Float
        get() = 0.toFloat()

    @Throws(InappropriateDescriptionException::class)
    override fun add(x: Float, y: Float): Float {
        return x + y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun subtract(x: Float, y: Float): Float {
        return x - y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun multiply(x: Float, y: Float): Float {
        return x * y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun divide(x: Float, y: Float): Float {
        return x / y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun half(x: Float): Float {
        return divide(x, 2.toFloat())
    }

    @Throws(InappropriateDescriptionException::class)
    override fun random(min: Float, max: Float): Float {
        return min + Random().nextFloat() * (max - min)
    }
}