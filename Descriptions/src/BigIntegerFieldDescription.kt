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

class BigIntegerFieldDescription : ValueFieldDescription<BigInteger?> {
    constructor(
            limitsDescription: LimitsDescription<BigInteger?>?) : super(limitsDescription) {
    }

    constructor() : super() {}
    constructor(BasisValue: BigInteger?) : super(BasisValue) {}

    @get:Throws(InappropriateDescriptionException::class)
    val positiveMinisculeValue: BigInteger
        get() = BigInteger.ONE

    @get:Throws(InappropriateDescriptionException::class)
    val positiveModerateValue: BigInteger
        get() = BigInteger.valueOf(100.toLong())

    // Going by spec, there is no max or min
    @get:Throws(InappropriateDescriptionException::class)
    val maximumPossibleValue: BigInteger
        get() {
            // Going by spec, there is no max or min
            throw InappropriateDescriptionException()
        }

    // Going by spec, there is no max or min
    @get:Throws(InappropriateDescriptionException::class)
    val minimumPossibleValue: BigInteger
        get() {
            // Going by spec, there is no max or min
            throw InappropriateDescriptionException()
        }

    @get:Throws(InappropriateDescriptionException::class)
    val zeroOrOrigin: BigInteger
        get() = BigInteger.ZERO

    @Throws(InappropriateDescriptionException::class)
    fun add(x: BigInteger, y: BigInteger?): BigInteger {
        return x.add(y)
    }

    @Throws(InappropriateDescriptionException::class)
    fun subtract(x: BigInteger, y: BigInteger?): BigInteger {
        return x.subtract(y)
    }

    @Throws(InappropriateDescriptionException::class)
    fun multiply(x: BigInteger, y: BigInteger?): BigInteger {
        return x.multiply(y)
    }

    @Throws(InappropriateDescriptionException::class)
    fun divide(x: BigInteger, y: BigInteger?): BigInteger {
        return x.divide(y)
    }

    @Throws(InappropriateDescriptionException::class)
    fun half(x: BigInteger): BigInteger {
        return divide(x, BigInteger.valueOf(2.toLong()))
    }

    @Throws(InappropriateDescriptionException::class)
    fun random(min: BigInteger?, max: BigInteger?): BigInteger {
        // return add(min, new Random().nextInt(subtract(max, min)));
        // Will implement support later
        throw InappropriateDescriptionException()
    }
}