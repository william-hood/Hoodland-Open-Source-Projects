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

import hoodland.opensource.descriptions.StringFieldTargets
import hoodland.opensource.toolbox.SubnameFactory

class StringFieldSubnameDescription : StringFieldDescription {
    constructor(basisValue: String) : super(basisValue) {
        subname = SubnameFactory()
    }

    constructor(initialIndex: Long) : super("") {
        subname = SubnameFactory(initialIndex)
    }

    constructor(basisValue: String, initialIndex: Long) : super(basisValue) {
        subname = SubnameFactory(initialIndex)
    }

    var subname: SubnameFactory? = null

    @get:Throws(NoValueException::class, InappropriateDescriptionException::class)
    override val describedValue: String?
        get() {
            if (target === StringFieldTargets.HAPPY_PATH
                    || target === StringFieldTargets.EXPLICIT) {
                val tmp = StringBuilder()

                if (basisValue != null) {
                    tmp.append(basisValue)
                }

                subname?.let {
                    tmp.append(it.nextSubname)
                }

                return tmp.toString()
            }
            return super.describedValue
        }
}