package hoodland.opensource.koarsegrind

import java.io.File
import java.lang.reflect.InvocationTargetException
import java.net.URLDecoder
import java.nio.charset.Charset
import kotlin.reflect.full.isSubclassOf

internal class Collector(
    val assembledCollection: TestCollection,
    val testLoader: ClassLoader,
    val preclusions: ArrayList<Throwable>) {
    private val foundCollections = ArrayList<TestCollection>()
    private val foundTests = ArrayList<Test>()

    init {
        val packages = testLoader.definedPackages
        packages.forEach {
            val resources = testLoader.getResources(it.name.replace('.', File.separatorChar)).asIterator()
            resources.forEach {
                // TODO: Call via object here
                recursiveCollect(
                    File(
                        URLDecoder.decode(
                            it.file, Charset.defaultCharset()
                        )
                    )
                )
            }
        }
    }

    private fun recursiveCollect(candidate: File) {
        //debuggingMemoir.info("PATH ${candidate.absolutePath}")

        if (candidate.exists()) {
            //debuggingMemoir.info("Candidate ${candidate.absolutePath} exists")
            val check = candidate.listFiles()
            check.forEach {
                //debuggingMemoir.info("Considering ${it.absolutePath}")
                if (it.isDirectory) {
                    if (!it.name.contains(".")) {
                        recursiveCollect(it)
                    }
                } else if (it.name.toLowerCase().endsWith(".class")) {
                    var attemptName = it.invariantSeparatorsPath.replace('/', '.')
                    do {
                        attemptName = attemptName.substringAfter('.')
                        try {
                            //debuggingMemoir.info("Attempting to load $attemptName")
                            val foundClass = testLoader.loadClass(attemptName.substring(0, attemptName.length - 6))
                            attemptName = "" // Prevent another loop iteration
                            //debuggingMemoir.info("foundClass.kotlin.isSubclassOf(Test::class) == ${foundClass.kotlin.isSubclassOf(Test::class)}")
                            if (foundClass.kotlin.isSubclassOf(Test::class)) {
                                //debuggingMemoir.debug("Identified ${foundClass.kotlin} as extending a KG Test")
                                if (!(foundClass.kotlin.isSubclassOf(ManufacturedTest::class))) {
                                    //debuggingMemoir.debug("Identified ${foundClass.kotlin} as NOT extending MaufacturedTest")
                                    val foundTestInstance: Test = foundClass.getDeclaredConstructor().newInstance() as Test
                                    foundTests.add(foundTestInstance)
                                }
                            } else if (foundClass.kotlin.isSubclassOf(TestFactory::class)) {
                                val factory: TestFactory = foundClass.getDeclaredConstructor().newInstance() as TestFactory
                                // We now have to separately add the TestCollecction and tests to their respective lists.
                                rootCollection.add(factory.products)
                            }
                        } catch (materialFailure: InvocationTargetException) {
                            //debuggingMemoir.showThrowable(materialFailure)
                            //debuggingMemoir.debug("MATERIAL FAILURE")
                            preclusions.add(materialFailure)
                        } catch (dontCare: Throwable) {
                            //debuggingMemoir.showThrowable(dontCare)
                            // DELIBERATE NO-OP
                            // Kotlin will hemorrhage exceptions during the process of identifying legitimate tests.
                            // Use the line below if there is need to identify failures that matter.
                            // addResult(getResultForFailureInCleanup(dontCare))
                        }
                    } while (attemptName.contains('.'))
                }
            }
        }/* else {
            debuggingMemoir.info("Candidate ${candidate.absolutePath} does not exist")
        }*/
    }
}