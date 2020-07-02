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

class DoubleFieldDescription : ValueFieldDescription<Double> {
    constructor(limitsDescription: LimitsDescription<Double>) : super(limitsDescription) {}
    constructor() : super() {}
    constructor(BasisValue: Double) : super(BasisValue) {}

    @get:Throws(InappropriateDescriptionException::class)
    override val positiveMinisculeValue: Double
        get() = 1.toDouble()

    @get:Throws(InappropriateDescriptionException::class)
    override val positiveModerateValue: Double
        get() = 100.toDouble()

    @get:Throws(InappropriateDescriptionException::class)
    override val maximumPossibleValue: Double
        get() = Double.MAX_VALUE

    @get:Throws(InappropriateDescriptionException::class)
    override val minimumPossibleValue: Double
        get() = Double.MIN_VALUE

    @get:Throws(InappropriateDescriptionException::class)
    override val zeroOrOrigin: Double
        get() = 0.toDouble()

    @Throws(InappropriateDescriptionException::class)
    override fun add(x: Double, y: Double): Double {
        return x + y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun subtract(x: Double, y: Double): Double {
        return x - y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun multiply(x: Double, y: Double): Double {
        return x * y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun divide(x: Double, y: Double): Double {
        return x / y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun half(x: Double): Double {
        return divide(x, 2.toDouble())
    }

    @Throws(InappropriateDescriptionException::class)
    override fun random(min: Double, max: Double): Double {
        // return add(min, new Random().nextInt(subtract(max, min)));
        // TODO: Will implement support later
        throw InappropriateDescriptionException()
    }
}