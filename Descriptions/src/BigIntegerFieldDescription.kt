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

import java.math.BigInteger
import java.util.*

class BigIntegerFieldDescription : ValueFieldDescription<BigInteger> {
    constructor(
            limitsDescription: LimitsDescription<BigInteger>) : super(limitsDescription) {
    }

    constructor() : super() {}
    constructor(BasisValue: BigInteger) : super(BasisValue) {}

    @get:Throws(InappropriateDescriptionException::class)
    override val positiveMinisculeValue: BigInteger
        get() = BigInteger.ONE

    @get:Throws(InappropriateDescriptionException::class)
    override val positiveModerateValue: BigInteger
        get() = BigInteger.valueOf(100.toLong())

    // Going by spec, there is no max or min
    @get:Throws(InappropriateDescriptionException::class)
    override val maximumPossibleValue: BigInteger
        get() {
            // Going by spec, there is no max or min
            throw InappropriateDescriptionException()
        }

    // Going by spec, there is no max or min
    @get:Throws(InappropriateDescriptionException::class)
    override val minimumPossibleValue: BigInteger
        get() {
            // Going by spec, there is no max or min
            throw InappropriateDescriptionException()
        }

    @get:Throws(InappropriateDescriptionException::class)
    override val zeroOrOrigin: BigInteger
        get() = BigInteger.ZERO

    @Throws(InappropriateDescriptionException::class)
    override fun add(x: BigInteger, y: BigInteger): BigInteger {
        return x.add(y)
    }

    @Throws(InappropriateDescriptionException::class)
    override fun subtract(x: BigInteger, y: BigInteger): BigInteger {
        return x.subtract(y)
    }

    @Throws(InappropriateDescriptionException::class)
    override fun multiply(x: BigInteger, y: BigInteger): BigInteger {
        return x.multiply(y)
    }

    @Throws(InappropriateDescriptionException::class)
    override fun divide(x: BigInteger, y: BigInteger): BigInteger {
        return x.divide(y)
    }

    @Throws(InappropriateDescriptionException::class)
    override fun half(x: BigInteger): BigInteger {
        return divide(x, BigInteger.valueOf(2.toLong()))
    }

    // Based on https://www.tutorialspoint.com/how-to-generate-a-random-biginteger-value-in-java
    @Throws(InappropriateDescriptionException::class)
    override fun random(min: BigInteger, max: BigInteger): BigInteger {
        val delta = max.subtract(min)
        val random = Random()
        val length = max.bitLength()

        var result = BigInteger(length, random)

        if (result.compareTo(min) < 0) {
            result = result.add(min)
        }

        if (result.compareTo(delta) >= 0) {
            result = result.mod(delta).add(min)
        }

        return result
    }
}