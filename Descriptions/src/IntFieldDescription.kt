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

import hoodland.opensource.toolbox.randomInteger
import kotlin.math.roundToInt

class IntFieldDescription(basisValue: Int, limits: IntLimitsDescription) : ValueFieldDescription<Int>(basisValue, limits) {

    override val positiveMinisculeValue: Int
        get() = 1

    override val positiveModerateValue: Int
        get() {
            if (limits.upper > 200) {
                return 100
            }

            val result = limits.upper / 3
            if (result < 2) { throw InappropriateDescriptionException() }
            return result
        }

    override val maximumPossibleValue: Int
        get() = Int.MAX_VALUE

    override val minimumPossibleValue: Int
        get() = Int.MIN_VALUE

    override val zero: Int
        get() = 0

    override fun add(x: Int, y: Int): Int {
        return x + y
    }

    override fun subtract(x: Int, y: Int): Int {
        return x - y
    }

    override fun multiply(x: Int, y: Int): Int {
        return x * y
    }

    override fun divide(x: Int, y: Int): Int {
        return (x.toDouble() / y.toDouble()).roundToInt()
    }

    override fun half(x: Int): Int {
        return divide(x, 2)
    }

    override fun random(min: Int, max: Int): Int {
        return add(min, randomInteger(min, max))
    }
}