package hoodland.opensource.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import hoodland.opensource.memoir.java.Constants;
import hoodland.opensource.memoir.java.junit.JUnitMemoir;
import hoodland.opensource.toolbox.java.Tools;

/**
 * This is a template to use when creating a new JUnit project that logs with Memoir.
 * @author William A. Hood - william.arthur.hood@gmail.com
 *
 */

// TODO: Class level log.

class JUnit_Memoir_Example_Template {
	private static JUnitMemoir classLog;
	private JUnitMemoir log;
	private TestInfo testInfo;
	
	void concludeTestLog() {
		classLog.showJUnitMemoir(log);
		classLog.reportCondition(log.status);
	}
	
	@BeforeAll
	static void classSetup() {
		classLog = new JUnitMemoir("Example Template", Tools.stdout, null);
	}
	
	@AfterAll
	static void classCleanup() {
		TopLevel.Log.showJUnitMemoir(classLog);
	}
	
	@BeforeEach
	void setUp(TestInfo testInfoIncoming) throws Exception {
		testInfo = testInfoIncoming;
		log = new JUnitMemoir(testInfo.getDisplayName(), Tools.stdout, null);
		JUnitMemoir setupLog = new JUnitMemoir("Setup");
		setupLog.info("This is where we set everything up!");
		setupLog.info("You can also verify proper configuration here.");
		log.showMemoir(setupLog, Constants.EMOJI_SETUP, "neutral");
	}

	@AfterEach
	void tearDown() throws Exception {
		JUnitMemoir tearDownLog = new JUnitMemoir("Tear Down");
		tearDownLog.info("This is where we clean everything up!");
		tearDownLog.info("You can also double-check proper configuration here.");
		log.showMemoir(tearDownLog, Constants.EMOJI_CLEANUP, "neutral");
		concludeTestLog();
	}

	@Test
	@DisplayName("First Test")
	void test() {
		log.info("Testing something...");
		
		//log.fail("Forcing a fail");
		log.assertTrue(true, "Forcing a true assert");
	}

	@Test
	@DisplayName("Second Test")
	void testAnother() {
		log.info("Testing first criterion...");
		log.assertTrue(true);
		
		log.info("Testing next criterion...");
		log.assertEquals(1, 1, "One should equal itself");

		log.info("Testing third criterion...");
		log.assertSame(2, 3.14159, "This should fail");
	}

}
