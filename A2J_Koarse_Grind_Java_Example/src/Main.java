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

import hoodland.opensource.koarsegrind.*;

public class Main {
	
	public static String STATUS_CATEGORY = "Status Examples";

    public static void main(String[] args) {
        TestProgram.INSTANCE.run("Koarse Grind in Java", args);
    }
}