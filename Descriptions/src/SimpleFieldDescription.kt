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

class SimpleFieldDescription<T>() : FieldDescription<T>() {
    var target = SimpleFieldTargets.HAPPY_PATH

    constructor(BasisValue: T) : this() { this.basisValue = BasisValue }

    override val hasSpecificHappyValue: Boolean
        get() = target === SimpleFieldTargets.HAPPY_PATH && basisValue != null

    override val isExplicit: Boolean
        get() = (target === SimpleFieldTargets.EXPLICIT)

    override val isDefault: Boolean
        get() = (target === SimpleFieldTargets.DEFAULT)

    override val describedValue: T?
        get() {
        when (target) {
            SimpleFieldTargets.DEFAULT -> throw NoValueException()
            SimpleFieldTargets.HAPPY_PATH, SimpleFieldTargets.EXPLICIT -> {
                if (basisValue == null) throw InappropriateDescriptionException()
                return basisValue
            }
        }
        return null
    }

    override fun toString(): String {
        return if (target === SimpleFieldTargets.EXPLICIT) "$target ($basisValue)" else target.toString()
    }

    override fun useExplicitValue(value: T?) {
        basisValue = value
        target = SimpleFieldTargets.EXPLICIT
    }

}