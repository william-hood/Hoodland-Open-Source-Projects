import hoodland.opensource.memoir.java.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MemoirJavaExample {
    public static void main(String[] args) throws FileNotFoundException {
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
            log.info("Look at the class \"TestStruct\" at the bottom of this source code file. Let's render one!");


        } catch (Throwable thisProblem) {
            log.showThrowable(thisProblem);
        } finally {
            log.conclude();
        }
    }
}
