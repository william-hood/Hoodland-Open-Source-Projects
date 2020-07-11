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

import rockabilly.memoir.*
import rockabilly.toolbox.stdout
import java.io.File
import java.net.URI
import java.net.http.HttpRequest

internal class TestStruct() {
    var name = "Hi"
    var value = 7
    var otherValue = 42.9
    var child: TestStruct? = null
    private var troll = "nya-nya!"
    var rogue = mapOf("LOTR" to "Sauron", "Star Wars" to "Darth Vader", "It" to "Pennywise")
}

fun main(args: Array<String>) {
    val homeFolder = System.getProperty("user.home")
    val outputFile = File("$homeFolder${File.separator}Documents${File.separator}Test Results${File.separator}Memoir Example.html")

    val memoir = Memoir("Kotlin Memoir Test", stdout, outputFile.printWriter())

    try {
        //val request = HttpRequest(HttpVerb.GET, "https://httpbin.org/get?param1=latida&param2=tweedledee&param3=whatever")
        //val request = HttpRequest(HttpVerb.GET, "http://neverssl.com")
        //val request = HttpRequest(HttpVerb.GET, "http://vbcknxfwztdmlhrs.neverssl.com/online")
        //val request = HttpRequest(HttpVerb.GET, "http://cnn.com")
        // http://openjdk.java.net/

        val request = HttpRequest.newBuilder()
                .uri(URI.create("http://vbcknxfwztdmlhrs.neverssl.com/online"))
                .build()
        memoir.showObject(request)

        //val response = memoir.ShowHttpTransaction(request)
        //memoir.Debug("Upon return response body = ${response.body()}")

        memoir.showHttpTransaction(request)

        val arrayCheck = intArrayOf(1, 5, 7, 9, 42, 781)
        memoir.show(arrayCheck, "arrayCheck")
        val peopleToAge = mapOf("Alice" to 20, "Bob" to 21, "George" to 68)
        memoir.show(peopleToAge, "peopleToAge")

        memoir.testStyle("old_parchment")

        val check = TestStruct()
        val inner = TestStruct()
        check.child = inner

        memoir.show(check, "check")

        val arrayOfObjects = Array<TestStruct>(3, {i -> check})
        arrayOfObjects[1] = inner

        memoir.show(arrayOfObjects, "arrayOfObjects")

        memoir.info("This is a test")
        memoir.debug("Debug message here!")
        memoir.error("Uh oh!")

        memoir.testException()

        memoir.testStyle("decaf_green")

        memoir.info("This is a test")
        memoir.info("This is a test")

        memoir.testStyle("decaf_orange")

        memoir.info("This is a test")
        memoir.info("This is a test")

        memoir.testStyle("decaf_green_light_roast")

        memoir.info("This is a test")
        memoir.info("This is a test")

        memoir.testStyle("decaf_orange_light_roast")

        memoir.info("This is a test")
        memoir.info("This is a test")

        memoir.testStyle("desert_horizon")

        memoir.info("This is a test")
        memoir.info("This is a test")
        memoir.info("This is a test")
        memoir.info("This is a test")

        val subLog = Memoir("Sub Log", stdout)
        subLog.info("First line of the sub log")
        subLog.info("Second line of the sub log")

        val justSomeTest = Memoir("Test Something", stdout)
        justSomeTest.info("This happened")
        justSomeTest.info("Then this")
        justSomeTest.info("Also this")

        justSomeTest.info("This check passed", EMOJI_PASSING_TEST)
        justSomeTest.info("So did this", EMOJI_PASSING_TEST)
        justSomeTest.info("But not this", EMOJI_FAILING_TEST)

        subLog.showMemoir(justSomeTest, EMOJI_FAILING_TEST, "failing_test_result")

        subLog.debug("Third line of the sub log")
        subLog.info("Fourth line of the sub log")

        memoir.showMemoir(subLog)
    } catch (thisProblem: Throwable) {
        memoir.showThrowable(thisProblem)
    } finally {
        memoir.conclude()
    }
}

fun Memoir.testStyle(style: String) {
    val styleTest = Memoir("This is $style", stdout)
    styleTest.info("This happened")
    styleTest.info("Then this")
    styleTest.info("Also this")
    styleTest.info("This check passed", EMOJI_PASSING_TEST)
    this.showMemoir(styleTest, style = style)
}

fun Memoir.testException() {
    try {
        throw NullPointerException("Who da punk?!?!?")
    } catch (thisProblem: Throwable) {
        val fakeExceptionForDemo = Exception("Just a fake exception to test this thing!")
        thisProblem.initCause(fakeExceptionForDemo)
        this.showThrowable(thisProblem)
    }
}