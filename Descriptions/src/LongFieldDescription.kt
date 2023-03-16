// Copyright (c) 2020, 2023 William Arthur Hood
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

import hoodland.opensource.toolbox.nextLong
import java.util.*
import kotlin.math.roundToLong

class LongFieldDescription(basisValue: Long, limits: LongLimitsDescription) : ValueFieldDescription<Long>(basisValue, limits) {

    override val positiveMinisculeValue: Long
        get() = 1.toLong()

    override val positiveModerateValue: Long
        get() = 100.toLong()

    override val maximumPossibleValue: Long
        get() = Long.MAX_VALUE

    override val minimumPossibleValue: Long
        get() = Long.MIN_VALUE

    override val zero: Long
        get() = 0.toLong()

    override fun add(x: Long, y: Long): Long {
        return x + y
    }

    override fun subtract(x: Long, y: Long): Long {
        return x - y
    }

    override fun multiply(x: Long, y: Long): Long {
        return x * y
    }

    override fun divide(x: Long, y: Long): Long {
        return (x.toDouble() / y.toDouble()).roundToLong()
    }

    override fun half(x: Long): Long {
        return divide(x, 2.toLong())
    }

    override fun random(min: Long, max: Long): Long {
        return add(min, nextLong(Random(), subtract(max, min)))
    }

    override fun isGreaterThan(x: Long, y: Long): Boolean {
        return x > y
    }

    override fun isLessThan(x: Long, y: Long): Boolean {
        return x < y
    }
}