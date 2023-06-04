package hoodland.opensource.example;

import hoodland.opensource.memoir.java.junit.JUnitMemoir;

import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

public class TopLevel extends JUnitMemoir {
	private static final String TEST_SUITE_NAME = "My Awesome Test Suite";
    private static String homeFolder = System.getProperty("user.home");
    private static File outputFile = new File(homeFolder + File.separator + "Documents" + File.separator + "Test Results" + File.separator + TEST_SUITE_NAME + ".html");
	
	private TopLevel() throws FileNotFoundException {
		super(TEST_SUITE_NAME, new PrintWriter(System.out), new PrintWriter(outputFile));
	}
	
	public static TopLevel Log;
	
	static {
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
