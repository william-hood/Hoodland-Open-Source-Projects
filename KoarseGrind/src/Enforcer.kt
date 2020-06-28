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

package rockabilly.koarsegrind

import rockabilly.toolbox.UnsetString

private const val DEFAULT_SHOULD_BE_FALSE_EXPLAINATION = "Expected boolean false. (Actual value was "
class Enforcer(conditionalType: TestConditionalType, owner: Test) {
    private var condType = conditionalType
    private var parent = owner

    private fun enforce(condition: Boolean, explaination: String) {
        parent.AddResult(condType.toTestResult(condition, explaination))
    }

    fun ShouldBeTrue(condition: Boolean, explaination: String = "Expected boolean true. (Actual value was $condition)") {
        enforce(condition, explaination)
    }

    fun ShouldBeFalse(condition: Boolean, explaination: String = "$DEFAULT_SHOULD_BE_FALSE_EXPLAINATION$condition)") {
        enforce(!condition, explaination)
    }

    fun ShouldNotBeTrue(condition: Boolean, explaination: String = "$DEFAULT_SHOULD_BE_FALSE_EXPLAINATION$condition)") {
        ShouldBeFalse(condition, explaination)
    }

    fun ShouldBeEqual(candidateA: Any?, candidateB: Any?, explaination: String = "Expected equal values. (Actual values were $candidateA and $candidateB)") {
        if (candidateA != null) {
            enforce(candidateA.equals(candidateB), explaination)
        } else {
            enforce(candidateB == null, explaination)
        }
    }

    fun ShouldNotBeEqual(candidateA: Any?, candidateB: Any?, explaination: String = "Expected unequal values. (Actual values were $candidateA and $candidateB)") {
        if (candidateA != null) {
            enforce(!candidateA.equals(candidateB), explaination)
        } else {
            enforce(candidateB != null, explaination)
        }
    }

    fun ShouldBeNull(candidate: Any?, explaination: String = "Expected null. (Actual values was $candidate)") {
        enforce(candidate == null, explaination)
    }

    fun ShouldNotBeNull(candidate: Any?, explaination: String = "Expected non-null. (Actual values was $candidate)") {
        enforce(candidate != null, explaination)
    }

    private fun enforceEmpty(actualCount: Int, explaination: String = "Expected an empty set. (Actually had $actualCount items)") {
        enforce(actualCount == 0, explaination)
    }

    fun ShouldBeEmpty(candidate: Map<*, *>, explaination: String = UnsetString) {
        val actualCount = candidate.count()
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: Iterable<*>, explaination: String = UnsetString) {
        val actualCount = candidate.count()
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: Array<*>, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: IntArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: FloatArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: ShortArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: LongArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: DoubleArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: CharArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: ByteArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    fun ShouldBeEmpty(candidate: BooleanArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explaination)
        }
    }

    private fun enforceNotEmpty(actualCount: Int, explaination: String = "Expected a non-empty set. (Actually had $actualCount items)") {
        enforce(actualCount > 0, explaination)
    }

    fun ShouldNotBeEmpty(candidate: Map<*, *>, explaination: String = UnsetString) {
        val actualCount = candidate.count()
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: Iterable<*>, explaination: String = UnsetString) {
        val actualCount = candidate.count()
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: Array<*>, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: IntArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: FloatArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: ShortArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: LongArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: DoubleArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: CharArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: ByteArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }

    fun ShouldNotBeEmpty(candidate: BooleanArray, explaination: String = UnsetString) {
        val actualCount = candidate.size
        if (explaination == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explaination)
        }
    }
}