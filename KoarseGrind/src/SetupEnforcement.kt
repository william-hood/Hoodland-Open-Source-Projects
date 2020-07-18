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

package hoodland.opensource.koarsegrind

// Client code is prohibited from using Setup to alter
//  * The name of the test
//  * The test case ID
//  * The priority level of the test
//  * Adding results in Setup is also forbidden.
internal class SetupEnforcement(basis: Test) {
    private val contentSize = basis.Results.size //Should this be .count() ???
    private val name = basis.name
    private val identifier = basis.identifier
    private val priority = basis.priority

    fun matches(candidate: SetupEnforcement): Boolean {
        if (identifier != candidate.identifier) return false
        if (name != candidate.name) return false
        if (contentSize != candidate.contentSize) return false
        return if (priority !== candidate.priority) false else true
    }
}