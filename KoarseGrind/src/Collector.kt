// Copyright (c) 2023, 2025 William Arthur Hood
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

import java.io.File
import java.lang.reflect.InvocationTargetException
import java.net.URLDecoder
import java.nio.charset.Charset
import kotlin.reflect.full.isSubclassOf


// TODO: Properly handle Jar files in the classpath (or verify if it already does)
internal class Collector(
    val assembledCollection: TestCategory,
    val testLoader: ClassLoader,
    val preclusions: ArrayList<Throwable>) {
    private val foundTests = ArrayList<Test>()
    private val foundOutfitters = ArrayList<Outfitter>()
    private val usedNames = HashSet<String>()
    private val usedIdentifiers = HashSet<String>()

    init {
        val packages = testLoader.definedPackages
        packages.forEach {
            val resources = testLoader.getResources(it.name.replace('.', File.separatorChar)).asIterator()
            resources.forEach {
                recursiveCollect(
                    File(
                        URLDecoder.decode(
                            it.file, Charset.defaultCharset()
                        )
                    )
                )
            }
        }

        // We now construct the tree of collections and populate them with tests.
        foundTests.forEach{
            // Force empty string to be treated as null.
            var effectivePath = it.categoryPath
            if (effectivePath == "") {
                effectivePath = null
            }
            if (effectivePath == null) {
                assembledCollection.add(it)
            } else {
                val splitPath = effectivePath.split(CATEGORY_PATH_DELIMITER)
                val addedCategory = assembledCollection.addCategory(splitPath)
                addedCategory.add(it)
            }
        }

        // Also put the outfitters where they belong.
        // It is considered an error if an outfitter is added to a category that is not there.
        // In other words, it won't create an empty category and put an outfitter in it. The
        // category must have been created because a test declared it, or because it is the top-level.
        // It is also an error if two different outfitters declare the same category.
        // An outfitter declaring the empty string as it's category path (the default) is
        // assigned to the root category.
        foundOutfitters.forEach {
            if (it.categoryPath == null) {
                // This goes to the top-level category
                if (assembledCollection.outfitter != null) {
                    preclusions.add(DuplicateOutfitterException("More than one Outfitter is assigned to the top-level category. This occurs when more than one Outfitter's categoryPath field is left default (blank)."))
                } else {
                    assembledCollection.outfitter = it
                }
            } else {
                // This goes to a non-root category below the top-level.
                val splitPath = it.categoryPath.split(CATEGORY_PATH_DELIMITER)
                val declaredCategory = assembledCollection.getCategory(splitPath)
                if (declaredCategory == null) {
                    preclusions.add(StrayOutfitterException("An Outfitter tried to declare a category of ${it.categoryPath} but no tests are in that category. A category with an Outfitter but no tests is not allowed."))
                } else {
                    if (declaredCategory.outfitter != null) {
                        preclusions.add(DuplicateOutfitterException("More than one Outfitter tried to declare a category of ${it.categoryPath}. A TestCategory may only have one Outfitter. Determine which Outfitter does not belong to ${it.categoryPath} and either correct the path or delete the Outfitter."))
                    } else {
                        declaredCategory.outfitter = it
                    }
                }
            }
        }
    }

    private fun recursiveCollect(candidate: File) {
        //debuggingBoolog.info("PATH ${candidate.absolutePath}")

        if (candidate.exists()) {
            //debuggingBoolog.info("Candidate ${candidate.absolutePath} exists")
            val check = candidate.listFiles()
            check.forEach {
                //debuggingBoolog.info("Considering ${it.absolutePath}")
                if (it.isDirectory) {
                    if (!it.name.contains(".")) {
                        recursiveCollect(it)
                    }
                } else if (it.name.lowercase().endsWith(".class")) {
                    var attemptName = it.invariantSeparatorsPath.replace('/', '.')
                    do {
                        attemptName = attemptName.substringAfter('.')
                        try {
                            //debuggingBoolog.info("Attempting to load $attemptName")
                            val foundClass = testLoader.loadClass(attemptName.substring(0, attemptName.length - 6))
                            attemptName = "" // Prevent another loop iteration
                            //debuggingBoolog.info("foundClass.kotlin.isSubclassOf(Test::class) == ${foundClass.kotlin.isSubclassOf(Test::class)}")
                            if (foundClass.kotlin.isSubclassOf(Test::class)) {
                                //debuggingBoolog.debug("Identified ${foundClass.kotlin} as extending a KG Test")
                                if (!(foundClass.kotlin.isSubclassOf(ManufacturedTest::class))) {
                                    //debuggingBoolog.debug("Identified ${foundClass.kotlin} as NOT extending MaufacturedTest")
                                    val foundTestInstance: Test = foundClass.getDeclaredConstructor().newInstance() as Test
                                    foundTests.add(foundTestInstance)
                                    addName(foundTestInstance.name)
                                    addIdentifier(foundTestInstance.identifier)
                                } else {
                                    if (foundClass.kotlin.isSubclassOf(Outfitter::class)) {
                                        //debuggingBoolog.debug("Identified ${foundClass.kotlin} as extending an Outfitter")
                                        val foundOutfitterInstance: Outfitter = foundClass.getDeclaredConstructor().newInstance() as Outfitter
                                        foundOutfitters.add(foundOutfitterInstance)
                                    }
                                }
                            } else if (foundClass.kotlin.isSubclassOf(TestFactory::class)) {
                                val factory: TestFactory = foundClass.getDeclaredConstructor().newInstance() as TestFactory
                                factory.producedTests.forEach {
                                    foundTests.add(it)
                                    addName(it.name)
                                    addIdentifier(it.identifier)
                                }
                            }
                        } catch (materialFailure: InvocationTargetException) {
                            //debuggingBoolog.showThrowable(materialFailure)
                            //debuggingBoolog.debug("MATERIAL FAILURE")
                            preclusions.add(materialFailure)
                        } catch (dontCare: Throwable) {
                            //debuggingBoolog.showThrowable(dontCare)
                            // DELIBERATE NO-OP
                            // Kotlin will hemorrhage exceptions during the process of identifying legitimate tests.
                            // Use the line below if there is need to identify failures that matter.
                            // addResult(getResultForFailureInCleanup(dontCare))
                        }
                    } while (attemptName.contains('.'))
                }
            }
        }/* else {
            debuggingBoolog.info("Candidate ${candidate.absolutePath} does not exist")
        }*/
    }

    fun addName(thisName: String) {
        if (usedNames.contains(thisName)) {
            preclusions.add(NameCollisionException(thisName))
        } else {
            usedNames.add(thisName)
        }
    }

    fun addIdentifier(thisIdentifier: String) {
        if (thisIdentifier.length > 0) {
            if (usedIdentifiers.contains(thisIdentifier)) {
                preclusions.add(IdentifierCollisionException(thisIdentifier))
            } else {
                usedIdentifiers.add(thisIdentifier)
            }
        }
    }
}