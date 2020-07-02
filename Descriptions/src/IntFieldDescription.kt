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

import rockabilly.toolbox.randomInteger


class IntFieldDescription : ValueFieldDescription<Int> {
    constructor(limitsDescription: LimitsDescription<Int>) : super(limitsDescription) {}
    constructor() : super() {}
    constructor(BasisValue: Int) : super(BasisValue) {}

    @get:Throws(InappropriateDescriptionException::class)
    override val positiveMinisculeValue: Int
        get() = 1

    // PROBLEM: If limits are from 0 to 30 this is not moderate.
    @get:Throws(InappropriateDescriptionException::class)
    override val positiveModerateValue: Int
        get() =// PROBLEM: If limits are from 0 to 30 this is not moderate.
            100

    @get:Throws(InappropriateDescriptionException::class)
    override val maximumPossibleValue: Int
        get() = Int.MAX_VALUE

    @get:Throws(InappropriateDescriptionException::class)
    override val minimumPossibleValue: Int
        get() = Int.MIN_VALUE

    @get:Throws(InappropriateDescriptionException::class)
    override val zeroOrOrigin: Int
        get() = 0

    @Throws(InappropriateDescriptionException::class)
    override fun add(x: Int, y: Int): Int {
        return x + y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun subtract(x: Int, y: Int): Int {
        return x - y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun multiply(x: Int, y: Int): Int {
        return x * y
    }

    @Throws(InappropriateDescriptionException::class)
    override fun divide(x: Int, y: Int): Int {
        return Math.round(x / y.toFloat())
    }

    @Throws(InappropriateDescriptionException::class)
    override fun half(x: Int): Int {
        return divide(x, 2)
    }

    @Throws(InappropriateDescriptionException::class)
    override fun random(min: Int, max: Int): Int {
        return add(min, randomInteger(min, max))
    }
}