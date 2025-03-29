package hoodland.opensource.example;

import hoodland.opensource.boolog.java.junit.JUnitBoolog;

import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

public class TopLevel extends JUnitBoolog {
	private static final String TEST_SUITE_NAME = "My Awesome Test Suite";
    private static String homeFolder = System.getProperty("user.home");
    private static File outputFile = new File(homeFolder + File.separator + "Documents" + File.separator + "Test Results" + File.separator + TEST_SUITE_NAME + ".html");
	
	private TopLevel() throws FileNotFoundException {
		super(TEST_SUITE_NAME, new PrintWriter(System.out), new PrintWriter(outputFile));
	}
	
	public static TopLevel Log;
	
	static {
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

		try {
			Log = new TopLevel();
		} catch (FileNotFoundException unexpectedException) {
			System.err.println("Unable to create the top-level log!");
			unexpectedException.printStackTrace();
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    	Log.conclude();
		    }
		});
	}
}
