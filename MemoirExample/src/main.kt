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
        memoir.ShowObject(request)

        //val response = memoir.ShowHttpTransaction(request)
        //memoir.Debug("Upon return response body = ${response.body()}")

        memoir.ShowHttpTransaction(request)

        val arrayCheck = intArrayOf(1, 5, 7, 9, 42, 781)
        memoir.Show(arrayCheck, "arrayCheck")
        val peopleToAge = mapOf("Alice" to 20, "Bob" to 21, "George" to 68)
        memoir.Show(peopleToAge, "peopleToAge")

        memoir.testStyle("old_parchment")

        val check = TestStruct()
        val inner = TestStruct()
        check.child = inner

        memoir.Show(check, "check")

        val arrayOfObjects = Array<TestStruct>(3, {i -> check})
        arrayOfObjects[1] = inner

        memoir.Show(arrayOfObjects, "arrayOfObjects")

        memoir.Info("This is a test")
        memoir.Debug("Debug message here!")
        memoir.Error("Uh oh!")

        memoir.testException()

        memoir.testStyle("decaf_green")

        memoir.Info("This is a test")
        memoir.Info("This is a test")

        memoir.testStyle("decaf_orange")

        memoir.Info("This is a test")
        memoir.Info("This is a test")

        memoir.testStyle("decaf_green_light_roast")

        memoir.Info("This is a test")
        memoir.Info("This is a test")

        memoir.testStyle("decaf_orange_light_roast")

        memoir.Info("This is a test")
        memoir.Info("This is a test")

        memoir.testStyle("desert_horizon")

        memoir.Info("This is a test")
        memoir.Info("This is a test")
        memoir.Info("This is a test")
        memoir.Info("This is a test")

        val subLog = Memoir("Sub Log", stdout)
        subLog.Info("First line of the sub log")
        subLog.Info("Second line of the sub log")

        val justSomeTest = Memoir("Test Something", stdout)
        justSomeTest.Info("This happened")
        justSomeTest.Info("Then this")
        justSomeTest.Info("Also this")

        justSomeTest.Info("This check passed", EMOJI_PASSING_TEST)
        justSomeTest.Info("So did this", EMOJI_PASSING_TEST)
        justSomeTest.Info("But not this", EMOJI_FAILING_TEST)

        subLog.ShowMemoir(justSomeTest, EMOJI_FAILING_TEST, "failing_test_result")

        subLog.Debug("Third line of the sub log")
        subLog.Info("Fourth line of the sub log")

        memoir.ShowMemoir(subLog)
    } catch (thisProblem: Throwable) {
        memoir.ShowThrowable(thisProblem)
    } finally {
        memoir.Conclude()
    }
}

fun Memoir.testStyle(style: String) {
    val styleTest = Memoir("This is $style", stdout)
    styleTest.Info("This happened")
    styleTest.Info("Then this")
    styleTest.Info("Also this")
    styleTest.Info("This check passed", EMOJI_PASSING_TEST)
    this.ShowMemoir(styleTest, style = style)
}

fun Memoir.testException() {
    try {
        throw NullPointerException("Who da punk?!?!?")
    } catch (thisProblem: Throwable) {
        val fakeExceptionForDemo = Exception("Just a fake exception to test this thing!")
        thisProblem.initCause(fakeExceptionForDemo)
        this.ShowThrowable(thisProblem)
    }
}