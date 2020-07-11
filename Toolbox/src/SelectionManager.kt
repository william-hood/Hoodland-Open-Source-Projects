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

package rockabilly.toolbox

import java.util.*

// TODO: Determine whether or not to keep this or toss it.
class SelectionManager {
    // NOTE: Java to Kotlin conversion originally used ArrayList<*> = ArrayList<Any>()
    private val selectedItems: ArrayList<Any> = ArrayList()
    fun reset() {
        selectedItems.clear()
    }

    fun addItem(thisItem: Any) {
        selectedItems.add(thisItem)    //.add(thisItem)
    }

    fun selectedItems(): Array<Any> {
        return selectedItems.toTypedArray()
    }

    fun firstSelectedItem(): Any {
        return selectedItems[0]
    }

    fun hasItems(): Boolean {
        return selectedItems.size > 0
    }
}
