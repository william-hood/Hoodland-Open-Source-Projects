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

import rockabilly.koarsegrind.ManufacturedTest
import rockabilly.koarsegrind.Test
import rockabilly.koarsegrind.TestCollection
import rockabilly.koarsegrind.TestFactory
import rockabilly.memoir.UNKNOWN
import rockabilly.toolbox.stderr
import java.io.File
import kotlin.reflect.full.isSubclassOf


// Based on https://dzone.com/articles/get-all-classes-within-package
private val testLoader = Thread.currentThread().getContextClassLoader()

object TestProgram {
    // This may have to be changed to take the same arguments as TestCollection
    fun run(name: String = UNKNOWN) {
        val packages = testLoader.definedPackages
        packages.forEach {
            val resources = testLoader.getResources(it.name.replace('.', File.separatorChar)).asIterator()
            resources.forEach {
                recursiveIdentify(File(it.file))
            }
        }

        TestCollection.run(name)
    }


    // TODO: Properly handle Jar files
    private fun recursiveIdentify(candidate: File) {
        if (candidate.exists()) {
            val check = candidate.listFiles()
            check.forEach {
                if (it.isDirectory) {
                    if (!it.name.contains(".")) {
                        recursiveIdentify(it)
                    }
                } else if (it.name.toLowerCase().endsWith(".class")) {
                        try {
                            val foundClass = testLoader.loadClass(it.name.substring(0, it.name.length - 6))
                            if (foundClass.kotlin.isSubclassOf(Test::class)) {
                                if (!(foundClass.kotlin.isSubclassOf(ManufacturedTest::class))) {
                                    val foundTestInstance: Test = foundClass.getDeclaredConstructor().newInstance() as Test
                                    TestCollection.add(foundTestInstance)
                                }
                            } else if (foundClass.kotlin.isSubclassOf(TestFactory::class)) {
                                val factory: TestFactory = foundClass.getDeclaredConstructor().newInstance() as TestFactory
                                TestCollection.addAll(factory.products)
                            }
                        } catch (dontCare: Throwable) {
                            // NO-OP
                            stderr.println(dontCare.message)
                        }
                    }
            }
        }
    }
}