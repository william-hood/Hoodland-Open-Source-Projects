package hoodland.opensource.example;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import hoodland.opensource.boolog.java.junit.JUnitBoolog;
import hoodland.opensource.toolbox.java.Tools;

/**
 * This is an example of a more interesting test that logs with Boolog.
 * @author William A. Hood - william.arthur.hood@gmail.com
 *
 */
class More_Interesting_Test_Example {
	private static JUnitBoolog classLog;
	private JUnitBoolog log;
	private TestInfo testInfo;
	
	void concludeTestLog() {
		classLog.showJUnitBoolog(log);
		classLog.reportCondition(log.status);
	}
	
	@BeforeAll
	static void classSetup() {
		classLog = new JUnitBoolog("More Interesting Test Example", Tools.stdout, null);
	}
	
	@AfterAll
	static void classCleanup() {
		TopLevel.Log.showJUnitBoolog(classLog);
	}
	
	@BeforeEach
	void setUp(TestInfo testInfoIncoming) throws Exception {
		testInfo = testInfoIncoming;
		log = new JUnitBoolog(testInfo.getDisplayName(), Tools.stdout, null);
		//JUnitBoolog setupLog = new JUnitBoolog("Setup");
		//setupLog.info("This is where we set everything up!");
		//setupLog.info("You can also verify proper configuration here.");
		//log.showBoolog(setupLog, Constants.EMOJI_SETUP, "neutral");
	}

	@AfterEach
	void tearDown() throws Exception {
		//JUnitBoolog tearDownLog = new JUnitBoolog("Tear Down");
		//tearDownLog.info("This is where we clean everything up!");
		//tearDownLog.info("You can also double-check proper configuration here.");
		//log.showBoolog(tearDownLog, Constants.EMOJI_CLEANUP, "neutral");
		concludeTestLog();
	}
	
	@Test
	@DisplayName("Exception Test Example")
	void exception_test() {
		Throwable candidate = getTestException(log);
		log.assertNotNull(candidate, "An exception was thrown");
		log.assertNotNull(candidate.getCause(), "The thrown exception also has a causal exception");
		log.assertEquals(candidate.getClass().getSimpleName(), "ArrayIndexOutOfBoundsException", "The exception was an ArrayIndexOutOfBoundsException");
	}

	@Test
	@DisplayName("API Call Example Test")
	void api_call_test() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get?param1=latida&param2=tweedledee&param3=whatever"))
                .build();

        HttpResponse response = log.showHttpTransaction(request);
        
        log.assertEquals(response.statusCode(), 200, "Response should have a 200 status code");
        log.debug("You will need a library such as GSON to parse the JSON response content.");
	}

	@Test
	@DisplayName("Object Test Example")
	void object_test() {
		log.info("Constructing the test object...");
		TestStruct testObject = new TestStruct();
		log.show(testObject);
		
		log.assertEquals(testObject.test1.get("LOTR"), "Sauron", "Sauron should be the villain in Lord of the Rings");
		log.assertEquals(testObject.test1.get("Star Wars"), "Darth Vader", "Darth Vader should be the villain in Star Wars");
		log.assertEquals(testObject.test1.get("It"), "Pennywise", "Pennywise should be the villain in It");

		log.assertEquals(testObject.troll, "nya-nya!", "The 'troll' field is set to the expected value.");
		log.assertEquals(testObject.otherValue, 42.9, "The 'otherValue' field is set to the expected value.");
	}

	// Inner Class for the object test
	public class TestStruct {
	    public String name = "Hi";
	    public int value = 7;
	    public double otherValue = 42.9;
	    public TestStruct child = null;
	    private String troll = "nya-nya!";
	
	    public Map<String, String> test1 = Map.of(
	            "LOTR", "Sauron",
	            "Star Wars", "Darth Vader",
	            "It", "Pennywise"
	            );
	}
	
	// Make an exception for the Exception Test

    private Throwable getTestException(JUnitBoolog log) {
        try {
            makeAndPrint();
        } catch (Throwable thisProblem) {
            Exception fakeExceptionForDemo = new Exception("Just a fake exception to test this thing!");
            thisProblem.initCause(fakeExceptionForDemo);
            log.showThrowable(thisProblem);
            return thisProblem;
        }
        
        return null;
    }

    private void makeAndPrint() {
        int[] doa = {1, 5, 7, 9};
        printSomeItem(doa);
    }

    private void printSomeItem(int[] doa) {
        System.out.println(doa[46378]);
    }
}
