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

private const val DEFAULT_SHOULD_BE_FALSE_EXPLANATION = "Expected boolean false. (Actual value was "
class Enforcer(conditionalType: TestConditionalType, owner: Test) {
    private var condType = conditionalType
    private var parent = owner

    private fun enforce(condition: Boolean, explanation: String) {
        parent.AddResult(condType.toTestResult(condition, explanation))
    }

    fun shouldBeTrue(condition: Boolean, explanation: String = "Expected boolean true. (Actual value was $condition)") {
        enforce(condition, explanation)
    }

    fun shouldBeFalse(condition: Boolean, explanation: String = "$DEFAULT_SHOULD_BE_FALSE_EXPLANATION$condition)") {
        enforce(!condition, explanation)
    }

    fun shouldNotBeTrue(condition: Boolean, explanation: String = "$DEFAULT_SHOULD_BE_FALSE_EXPLANATION$condition)") {
        shouldBeFalse(condition, explanation)
    }

    fun shouldBeEqual(candidateA: Any?, candidateB: Any?, explanation: String = "Expected equal values. (Actual values were $candidateA and $candidateB)") {
        if (candidateA != null) {
            enforce(candidateA.equals(candidateB), explanation)
        } else {
            enforce(candidateB == null, explanation)
        }
    }

    fun shouldNotBeEqual(candidateA: Any?, candidateB: Any?, explanation: String = "Expected unequal values. (Actual values were $candidateA and $candidateB)") {
        if (candidateA != null) {
            enforce(!candidateA.equals(candidateB), explanation)
        } else {
            enforce(candidateB != null, explanation)
        }
    }

    fun shouldBeNull(candidate: Any?, explanation: String = "Expected null. (Actual values was $candidate)") {
        enforce(candidate == null, explanation)
    }

    fun shouldNotBeNull(candidate: Any?, explanation: String = "Expected non-null. (Actual values was $candidate)") {
        enforce(candidate != null, explanation)
    }

    private fun enforceEmpty(actualCount: Int, explanation: String = "Expected an empty set. (Actually had $actualCount items)") {
        enforce(actualCount == 0, explanation)
    }

    fun shouldBeEmpty(candidate: Map<*, *>, explanation: String = UnsetString) {
        val actualCount = candidate.count()
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: Iterable<*>, explanation: String = UnsetString) {
        val actualCount = candidate.count()
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: Array<*>, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: IntArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: FloatArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: ShortArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: LongArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: DoubleArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: CharArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: ByteArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    fun shouldBeEmpty(candidate: BooleanArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceEmpty(actualCount)
        } else {
            enforceEmpty(actualCount, explanation)
        }
    }

    private fun enforceNotEmpty(actualCount: Int, explanation: String = "Expected a non-empty set. (Actually had $actualCount items)") {
        enforce(actualCount > 0, explanation)
    }

    fun shouldNotBeEmpty(candidate: Map<*, *>, explanation: String = UnsetString) {
        val actualCount = candidate.count()
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: Iterable<*>, explanation: String = UnsetString) {
        val actualCount = candidate.count()
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: Array<*>, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: IntArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: FloatArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: ShortArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: LongArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: DoubleArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: CharArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: ByteArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }

    fun shouldNotBeEmpty(candidate: BooleanArray, explanation: String = UnsetString) {
        val actualCount = candidate.size
        if (explanation == UnsetString) {
            enforceNotEmpty(actualCount)
        } else {
            enforceNotEmpty(actualCount, explanation)
        }
    }
}