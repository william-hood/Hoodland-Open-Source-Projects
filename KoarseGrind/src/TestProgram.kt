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
import java.io.File
import kotlin.reflect.full.isSubclassOf


// Based on https://dzone.com/articles/get-all-classes-within-package
private val testLoader = Thread.currentThread().getContextClassLoader()

object TestProgram {
    fun Run() {
        val packages = testLoader.definedPackages
        packages.forEach {
            val resources = testLoader.getResources(it.name.replace('.', File.separatorChar)).asIterator()
            resources.forEach {
                recursiveIdentify(File(it.file))
            }
        }

        TestCollection.Run()
    }


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
                                TestCollection.addAll(factory.Products)
                            }
                        } catch (dontCare: Throwable) {
                            // NO-OP
                            System.err.println(dontCare.message)
                        }
                    }
            }
        }
    }
}

    /*

    fun Run(SuiteName: String = UNSET_DESCRIPTION, outputFolder: String? = null, vararg specificTestIds: String) {
        TestCollection.name = this::class.simpleName.toString()
        var outputRoot = "$DEFAULT_PARENT_FOLDER${File.separatorChar}DateTimeHere ${availableTests.name}"

        if (outputFolder != null) {
            outputRoot = outputFolder + File.separatorChar + availableTests.name
        }

        val check = java.lang.ClassLoader.getSystemClassLoader().
        val testFinder: ServiceLoader<Test> = ServiceLoader.load(Test::class.java, java.lang.ClassLoader.getSystemClassLoader())
        testFinder.forEach {
            if (it != null) {
                if (! (it is ManufacturedTest)) {
                    availableTests.add(it)
                }
            }
        }

        val factoryFinder: ServiceLoader<TestFactory> = ServiceLoader.load(TestFactory::class.java)
        factoryFinder.forEach {
            if (it != null) {
                availableTests.addAll(it.Products)
            }
        }

        availableTests.RunTestCollection(MatchList(), outputRoot)
    }
}
     */