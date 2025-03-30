/*

READ ME: KOARSE GRIND EXAMPLE FOR JAVA (some editors will hide all but the first line of this comment)

This example is designed for use with IntelliJ IDEA CE. To run it, right-click the green
triangle to the left of the main() function and select the first item at the top
("Run MainKt"). A folder "Test Results" will be created in your documents folder. The
test program output will be created in a new time-stamped subdirectory from there.
Within the output directory will be an "All Tests.html" file, a CSV spreadsheet of
the test results, and PASS/FAIL prefixed artifact directories for each test that ran.

➡️ THIS IS AN EXAMPLE TEST SUITE AND MANY TESTS WON'T PASS FOR ILLUSTRATIVE PURPOSES ⬅️

 */

// ‼️ IMPORTANT ‼️ If a module name, or the name of its directory, contains a space tests in it will not be run.
// So don't do that!

import hoodland.opensource.boolog.java.Boolog;
import hoodland.opensource.koarsegrind.*;

public class Main {
	
	public static String STATUS_CATEGORY = "Status Examples";

    public static void main(String[] args) {
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

        // Aside from that, only this line below is necessary to start
        // your tests. Koarse Grind will automatically locate all
        // Test & TestFactory classes in the classpath, instantiate
        // and run one of each it finds.
        TestProgram.INSTANCE.run("Koarse Grind in Java", Boolog.THEME_DEFAULT, args);
    }
}