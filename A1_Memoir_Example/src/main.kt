/*

READ ME: MEMOIR EXAMPLE (some editors will hide all but the first line of this comment)

Memoir is an HTML-based rich logging system capable of visual renditions of
arrays, maps, iterables, HTTP requests & responses, Exceptions (or any
other Throwable), and any other class or object. One Memoir instance can even
embed another as a log subsection.

When used for program debugging, HTTP request/response debugging, activity logging,
or any other purpose, output from Memoir will easier to read and work with than
ordinary console output (though it does provide counterpart output to the console
in addition to its HTML log file).

This example is designed for use with IntelliJ IDEA CE. To run it, right-click the green
triangle to the left of the main() function and select the first item at the top
("Run MainKt"). A folder "Test Results" will be created in your documents folder. The
output file "Memoir Example.html" will be created the new "Test Results" directory.

Memoir is the integrated log system for the Koarse Grind test framework.

 */

import hoodland.opensource.memoir.*
import hoodland.opensource.toolbox.stdout
import java.io.File
import java.net.URI
import java.net.http.HttpRequest

fun main(args: Array<String>) {
    val homeFolder = System.getProperty("user.home")
    val outputFile = File("$homeFolder${File.separator}Documents${File.separator}Test Results${File.separator}Memoir Example.html")

    val log = Memoir("Memoir Example", stdout, outputFile.printWriter())

    try {
        log.skipLine()
        log.info("Memoir is an HTML-based rich logging system capable of visual renditions of arrays, maps, iterables, HTTP requests & responses, exceptions (or any other Throwable), and any other class or object. One Memoir instance can even embed another as a log subsection.")
        log.info("When used for debugging control flow, HTTP requests & responses, activity logging, or any other purpose, output from Memoir will easier to read and work with than ordinary console output (though it does provide counterpart output to the console in addition to its HTML log file).")
        log.info("All of the above messages represent \"normal\" log output with the .info() function.")
        log.info("When debugging a program, you might need a single line of information to stand out.")
        log.info("If you use the .debug() function instead of .info() the message will be highlighted in yellow like this...")
        log.debug("Memoir is the integrated log system for the Koarse Grind test framework.")
        log.info("Similar to that is the .error() function. The only difference is an icon in the log identifying the line as an error...")
        log.error("Uh-oh... That wasn't supposed to happen!")
        log.skipLine()
        log.info("Why would you want to log directly to HTML?")
        log.info("Because it's very hard, using ordinary plain-text logging, to visualize the workings of a cloud service, test suite, or other back-end process.")
        log.info("Let's suppose you need to check on the state of a data structure at a certain point in the program.")
        log.info("Look at the class \"TestStruct\" at the bottom of this source code file. Let's render one!")

        val check = TestStruct()
        val inner = TestStruct()
        check.child = inner

        log.show(check, "check")
        log.info("\uD83D\uDC46 ...but wait there's more!!! Hover over that and see if there's stuff you can click!")
        log.info("Specifically, try and click-to-expand the Map and the child TestStruct.")
        log.skipLine()


        log.info("Memoir can be very useful for testing HTTP Requests. Let's use Java's standard HTTP client to send a request and get a response.")

        val request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get?param1=latida&param2=tweedledee&param3=whatever"))
                .build()

        log.showHttpTransaction(request)
        log.skipLine()

        log.info("Let's think of all the heart-felt love and warmth you feel trying to make sense of an exception stacktrace from console output.")
        log.info("We're going to deliberately throw an exception with a simulated causal exception, catch it, and render it.")

        log.testException()
        log.skipLine()

        log.info("Memoir knows a few other tricks. Here's an array...")
        val arrayCheck = intArrayOf(1, 5, 7, 9, 42, 781)
        log.show(arrayCheck, "arrayCheck")
        log.skipLine()
        log.info("Here's a map")
        val peopleToAge = mapOf("Alice" to 20, "Bob" to 21, "George" to 68)
        log.show(peopleToAge, "peopleToAge")

        log.show(subLog(check, inner))

        log.debug("One caveat: If you .conclude() a Memoir, it's done. That function closes any output streams and makes it read-only.")
        log.info("A Memoir also gets concluded if you embed it in another memoir with either the .show() or .showMemoir() functions.")
        log.skipLine()
        log.info("Well, that's the demo. Go forth and do great things!")
    } catch (thisProblem: Throwable) {
        log.showThrowable(thisProblem)
    } finally {
        log.conclude()
    }
}

internal class TestStruct() {
    var name = "Hi"
    var value = 7
    var otherValue = 42.9
    var child: TestStruct? = null
    private var troll = "nya-nya!"
    var rogue = mapOf("LOTR" to "Sauron", "Star Wars" to "Darth Vader", "It" to "Pennywise")
}

fun Memoir.testException() {
    try {
        makeAndPrint()
    } catch (thisProblem: Throwable) {
        val fakeExceptionForDemo = Exception("Just a fake exception to test this thing!")
        thisProblem.initCause(fakeExceptionForDemo)
        this.showThrowable(thisProblem)
    }
}

fun makeAndPrint() {
    printSomeItem(intArrayOf(1, 5, 7, 9))
}

fun printSomeItem(doa: IntArray) {
    System.out.println(doa[46378]);
}

internal fun subLog(check: TestStruct, inner: TestStruct): Memoir {
    val log = Memoir("Click this to see one of Memoir's biggest tricks!", stdout)
    log.info("The truth is that all of the stuff above could've been put into it's own little click-to-expand subsection.")
    log.info("A Memoir can embed another Memoir. Time stamps, icons, and all!")
    log.info("Let's take up lots of space by rendering an array of those TestStruct things...")

    val arrayOfObjects = Array<TestStruct>(3, {i -> check})
    arrayOfObjects[1] = inner

    log.show(arrayOfObjects, "arrayOfObjects")

    log.info("Let's repeat some of the things we did in the old log, just for show...")
    log.debug("Memoir is the integrated log system for the Koarse Grind test framework.")
    log.error("Uh-oh... That wasn't supposed to happen!")
    log.skipLine()


    log.info("Memoir can be very useful for testing HTTP Requests. Let's use Java's standard HTTP client to send a request and get a response.")

    val payload = "This is the request body"
    val request = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/get?param1=latida&param2=tweedledee&param3=whatever"))
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build()

    log.showHttpTransaction(request, payload)
    log.skipLine()

    log.info("Let's think of all the heart-felt love and warmth you feel trying to make sense of an exception stacktrace from console output.")
    log.info("We're going to deliberately throw an exception with a simulated causal exception, catch it, and render it.")

    log.testException()
    log.skipLine()

    val subLog = Memoir("Keep on embedding Memoirs within Memoirs within Memoirs", stdout)
    subLog.info("Memoir knows a few other tricks. Here's an array...")
    val arrayCheck = intArrayOf(1, 5, 7, 9, 42, 781)
    subLog.show(arrayCheck, "arrayCheck")
    subLog.skipLine()
    subLog.info("Here's a map")
    val peopleToAge = mapOf("Alice" to 20, "Bob" to 21, "George" to 68)
    subLog.show(peopleToAge, "peopleToAge")

    log.showMemoir(subLog, style = "inconclusive_test_result")
    return log
}