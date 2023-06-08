import hoodland.opensource.memoir.java.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Map;

import static java.util.Map.entry;

public class MemoirJavaExample {
    public static void main(String[] args) throws FileNotFoundException {
        // The line below will force a workaround so at least the HTML
        // output is correct when running on Windows. You may also need
        // to get the new Windows Terminal and Set Windows to use UTF-8
        // by default (like every other OS does). To do this open Windows
        // Settings, then search for and go into Language Settings. Scroll
        // down and click "Administrative language settings", then
        // click "Change system locale" and check the box labeled
        // "Beta: Use Unicode UTF-8 for worldwide language support".
        // As of June 2023 the line below is only necessary on Microsoft
        // Windows and only when not running via IntelliJ or Eclipse.
        System.setProperty("file.encoding", "UTF-8");

        String homeFolder = System.getProperty("user.home");
        File outputFile = new File(homeFolder + File.separator + "Documents" + File.separator + "Test Results" + File.separator + "Memoir Java Example.html");
        Memoir log = new Memoir("Memoir Example (from Java)", new PrintWriter(System.out), new PrintWriter(outputFile));

        try {
            log.skipLine();
            log.info("Memoir is an HTML-based rich logging system capable of visual renditions of arrays, maps, iterables, HTTP requests & responses, exceptions (or any other Throwable), and any other class or object. One Memoir instance can even embed another as a log subsection.");
            log.info("When used for debugging control flow, HTTP requests & responses, activity logging, or any other purpose, output from Memoir will easier to read and work with than ordinary console output (though it does provide counterpart output to the console in addition to its HTML log file).");
            log.info("All of the above messages represent \"normal\" log output with the .info() function.");
            log.info("When debugging a program, you might need a single line of information to stand out.");
            log.info("If you use the .debug() function instead of .info() the message will be highlighted in yellow like this...");
            log.debug("Memoir is the integrated log system for the Koarse Grind test framework.");
            log.info("Similar to that is the .error() function. The only difference is an icon in the log identifying the line as an error...");
            log.error("Uh-oh... That wasn't supposed to happen!");
            log.skipLine();
            log.info("Why would you want to log directly to HTML?");
            log.info("Because it's very hard, using ordinary plain-text logging, to visualize the workings of a cloud service, test suite, or other back-end process.");
            log.info("Let's suppose you need to check on the state of a data structure at a certain point in the program.");
            log.info("Look at the class \"TestStruct\" in the source code file \"TestStruct.java\". Let's render one!");

            TestStruct check = new TestStruct();
            TestStruct inner = new TestStruct();
            check.child = inner;
            log.show(check, "check");

            log.info("\uD83D\uDC46 ...but wait there's more!!! Hover over that and see if there's stuff you can click!");
            log.info("Specifically, try and click-to-expand the Map and the child TestStruct.");
            log.skipLine();

            log.info("Memoir can be very useful for testing HTTP Requests. Let's use Java's standard HTTP client to send a request and get a response.");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/get?param1=latida&param2=tweedledee&param3=whatever"))
                    .build();

            log.showHttpTransaction(request);
            log.skipLine();

            log.info("Let's think of all the heart-felt love and warmth you feel trying to make sense of an exception stacktrace from console output.");
            log.info("We're going to deliberately throw an exception with a simulated causal exception, catch it, and render it.");
            testException(log);
            log.skipLine();

            log.info("Memoir knows a few other tricks. Here's an array...");
            int[] arrayCheck = {1, 5, 7, 9, 42, 781};
            log.show(arrayCheck, "arrayCheck");
            log.skipLine();

            log.info("Here's a map");
            Map<String, Integer> peopleToAge = Map.ofEntries(
                    entry("Alice", 20),
                    entry("Bob", 21),
                    entry("George", 68)
            );
            log.show(peopleToAge, "peopleToAge");

            log.showMemoir(subLog(check, inner));

            log.debug("One caveat: If you .conclude() a Memoir, it's done. That function closes any output streams and makes it read-only.");
            log.info("A Memoir also gets concluded if you embed it in another memoir with either the .show() or .showMemoir() functions.");
            log.skipLine();
            log.info("Well, that's the demo. Go forth and do great things!");
        } catch (Throwable thisProblem) {
            log.showThrowable(thisProblem);
        } finally {
            log.conclude();
        }
    }

    public static void testException(Memoir log) {
        try {
            makeAndPrint();
        } catch (Throwable thisProblem) {
            Exception fakeExceptionForDemo = new Exception("Just a fake exception to test this thing!");
            thisProblem.initCause(fakeExceptionForDemo);
            log.showThrowable(thisProblem);
        }
    }

    public static void makeAndPrint() {
        int[] doa = {1, 5, 7, 9};
        printSomeItem(doa);
    }

    public static void printSomeItem(int[] doa) {
        System.out.println(doa[46378]);
    }

    public static Memoir subLog(TestStruct check, TestStruct inner) {

        Memoir log = new Memoir("Click this to see one of Memoir's biggest tricks!", new PrintWriter(System.out), null);
        log.info("The truth is that all of the stuff above could've been put into it's own little click-to-expand subsection.");
        log.info("A Memoir can embed another Memoir. Time stamps, icons, and all!");
        log.info("Let's take up lots of space by rendering an array of those TestStruct things...");

        TestStruct[] arrayOfObjects = {check, check, check};
        arrayOfObjects[1] = inner;

        log.show(arrayOfObjects, "arrayOfObjects");

        log.info("Let's repeat some of the things we did in the old log, just for show...");
        log.debug("Memoir is the integrated log system for the Koarse Grind test framework.");
        log.error("Uh-oh... That wasn't supposed to happen!");
        log.skipLine();


        log.info("Memoir can be very useful for testing HTTP Requests. Let's use Java's standard HTTP client to send a request and get a response.");

        String payload = "This is the request body";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get?param1=latida&param2=tweedledee&param3=whatever"))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        log.showHttpTransaction(request, payload);
        log.skipLine();

        log.info("Let's think of all the heart-felt love and warmth you feel trying to make sense of an exception stacktrace from console output.");
        log.info("We're going to deliberately throw an exception with a simulated causal exception, catch it, and render it.");

        testException(log);
        log.skipLine();

        Memoir subLog = new Memoir("Click Here for some known issues with the Java wrapper for Memoir", new PrintWriter(System.out), null);
        subLog.info("Classes may need to be of public scope to shown by show() or showObject()", "▪");
        subLog.info("Because of the way the Java wrapper works, Memoirs must be embedded with showMemoir() and not with show()", "▪");
        subLog.info("Because Memoir itself is written in Kotlin, primitive types displayed by showObject(), such as int or String, will show as \"kotlin.Int\" or \"kotlin.String\"", "▪");

        log.showMemoir(subLog, Constants.EMOJI_MEMOIR, "inconclusive_test_result");
        return log;
    }
}
