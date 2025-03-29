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

import java.math.BigInteger
import java.util.*

class DoubleFieldDescription(basisValue: Double, limits: DoubleLimitsDescription) : ValueFieldDescription<Double>(basisValue, limits) {

    override val positiveMinisculeValue: Double
        get() = 1.toDouble()

    override val positiveModerateValue: Double
        get() = 100.toDouble()

    override val maximumPossibleValue: Double
        get() = Double.MAX_VALUE

    override val minimumPossibleValue: Double
        get() = Double.MIN_VALUE

    override val zero: Double
        get() = 0.toDouble()

    override fun add(x: Double, y: Double): Double {
        return x + y
    }

    override fun subtract(x: Double, y: Double): Double {
        return x - y
    }

    override fun multiply(x: Double, y: Double): Double {
        return x * y
    }

    override fun divide(x: Double, y: Double): Double {
        return x / y
    }

    override fun half(x: Double): Double {
        return divide(x, 2.toDouble())
    }

    override fun random(min: Double, max: Double): Double {
        return min + Random().nextDouble() * (max - min)
    }

    override fun isGreaterThan(x: Double, y: Double): Boolean {
        return x > y
    }

    override fun isLessThan(x: Double, y: Double): Boolean {
        return x < y
    }
}