package hoodland.opensource.memoir.java.junit;

import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

import hoodland.opensource.memoir.java.Constants;
import hoodland.opensource.memoir.java.HeaderFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.TestAbortedException;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingSupplier;

public class JUnitMemoir extends hoodland.opensource.memoir.java.Memoir {
    public enum Status { PASSING, INCONCLUSIVE, FAILING, UNSET };

    /**
     * Reflect the color coding of this Memoir, when embedded in another Memoir such as
     * a top-level or higher level log, is determined by its status. PASSING ->
     * Green; FAILING -> Red; INCONCLUSIVE or UNSET -> Yellow. The default Status
     * is UNSET. You should let the reportCondition() method be the only thing
     * that sets this field unless you know what you're doing and why.
     */
    public Status status = Status.UNSET;

    /**
     * The color coding of this Memoir, when embedded in another Memoir such as
     * a top-level or higher level log, is determined by its status. PASSING ->
     * Green; FAILING -> Red; INCONCLUSIVE or UNSET -> Yellow. When an assertion
     * through this Memoir instance is carried out, it reports its status via
     * this function. The status starts as UNSET. One or more passing tests moves
     * the status to PASSING. One or more failing tests makes the status
     * FAILING and this Memoir can no longer become Passing unless the programmer
     * forces it via the 'status' field. If an assumption fails, the status
     * becomes INCONCLUSIVE. Again this Memoir's status can not become Passing
     * or Failing again unless the programmer forces it by directly setting
     * the 'status' field.
     * @param thisCondition
     */
    public void reportCondition(Status thisCondition) {
        switch (thisCondition) {
            case PASSING:
                if (status == Status.UNSET) {
                    status = Status.PASSING;
                }
                break;
            case FAILING:
                switch (status) {
                    case PASSING:
                    case UNSET:
                        status = Status.FAILING;
                        break;
                    default:
                        // Deliberate NO-OP
                }
                break;
            case INCONCLUSIVE:
                status = Status.INCONCLUSIVE;
                break;
            default:
                // Deliberate NO-OP as UNSET should not be passed in.
        }
    }

    //=== Primary Constructor

    /**
     * Primary constructor for the JUnit extended Memoir Java Wrapper. This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * This version contains extra wrapper functions to make working with JUnit easier.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
     * @param forHTML This is the main log file. It may be left out when used as a subsection of another Memoir.
     * @param showTimestamps If you don't want time stamps with every line of the log, set this to false.
     * @param showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public JUnitMemoir(String title,
                  PrintWriter forPlainText,
                  PrintWriter forHTML,
                  Boolean showTimestamps,
                  Boolean showEmojis,
                  HeaderFunction headerFunction) {
        super(title, forPlainText, forHTML, showTimestamps, showEmojis, headerFunction);
    }

    //=== Alternate Constructors

    /**
     * Alternate constructor for the JUnit extended Memoir Java Wrapper that assumes showing emojis & timestamps, no headerFunction and no output for plain text or HTML.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     */
    public JUnitMemoir() {
        this(Constants.UNKNOWN, null, null, true, true, null);
    }

    /**
     * Alternate constructor for the JUnit extended Memoir Java Wrapper that uses the title "(unknown)".
     * It assumes showing timestamps and emojis but no header function and no HTML or plain text output.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public JUnitMemoir(HeaderFunction headerFunction) {
        this(Constants.UNKNOWN, null, null, true, true, headerFunction);
    }

    /**
     * Alternate constructor for the JUnit extended Memoir Java Wrapper that assumes showing timestamps and emojis but no header function and no HTML or plain text output.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     */
    public JUnitMemoir(String title) {
        this(title, null, null, true, true, null);
    }

    /**
     * Alternate constructor for the JUnit extended Memoir Java Wrapper that assumes showing timestamps and emojis but no HTML or plain text output.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public JUnitMemoir(String title, HeaderFunction headerFunction) {
        this(title, null, null, true, true, headerFunction);
    }

    /**
     * Alternate constructor for the JUnit extended Memoir Java Wrapper that assumes showing timestamps and emojis but no header function.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
     * @param forHTML This is the main log file. It may be left out when used as a subsection of another Memoir.
     */
    public JUnitMemoir(String title, PrintWriter forPlainText, PrintWriter forHTML) {
        this(title, forPlainText, forHTML, true, true, null);
    }

    /**
     * Alternate constructor for the JUnit extended Memoir Java Wrapper that assumes showing timestamps and emojis.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param forPlainText Typically this is pointed at stdout for console output. This can also be pointed at a plain text file.
     * @param forHTML This is the main log file. It may be left out when used as a subsection of another Memoir.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public JUnitMemoir(String title, PrintWriter forPlainText, PrintWriter forHTML, HeaderFunction headerFunction) {
        this(title, forPlainText, forHTML, true, true, headerFunction);
    }

    /**
     * Alternate constructor for the JUnit extended Memoir Java Wrapper that assumes no plaintext or HTML output and no header function.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param showTimestamps If you don't want time stamps with every line of the log, set this to false.
     * @param showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
     */
    public JUnitMemoir(String title, Boolean showTimestamps, Boolean showEmojis) {
        this(title, null, null, showTimestamps, showEmojis, null);
    }

    /**
     * Alternate constructor for the JUnit extended Memoir Java Wrapper that assumes no plaintext or HTML output.
     * This contains a Kotlin Memoir but does not extend it nor expose it in any way.
     * Memoir is a logging system designed to produce rich, readable HTML-based output with appropriate
     * console output accompanying it. A Memoir may be a root-level log file, a subsection of another Memoir, or both.
     * It includes methods to render objects, HTTP transactions, exceptions, collections and other Memoirs
     * in click-to-expand fashion.
     *
     * @param title This will be indicated at the top of the file in the header if this is a root-level Memoir. For a subsection it appears in bold above the click-to-expand portion.
     * @param showTimestamps If you don't want time stamps with every line of the log, set this to false.
     * @param showEmojis Set this to false and no lines will display an Emoji even if one is supplied.
     * @param headerFunction Use this to override the default header and make your own. Implement the displayHeader() method in a HeaderFunction interface.
     */
    public JUnitMemoir(String title, Boolean showTimestamps, Boolean showEmojis, HeaderFunction headerFunction) {
        this(title, null, null, showTimestamps, showEmojis, headerFunction);
    }

    /**
     * Use this function to embed another JUnitMemoir within this one. The embedded
     * JUnitMemoir will be color coded according to its status.
     * @param subordinate The JUnitMemoir you wish to embed in this one. The subordinate
     *                    JUnitMemoir will be concluded once you embed it and it
     *                    will no lnge rbe possible to write to it.
     * @return Returns HTML text representing the embedded JUnitMemoir. Ignore
     * the return value unless you know what you're doing and why.
     */
    public String showJUnitMemoir(JUnitMemoir subordinate) {
        String style;
        String emoji;

        switch (subordinate.status) {
            case PASSING:
                style = "passing_test_result";
                emoji = Constants.EMOJI_PASSING_TEST;
                break;
            case FAILING:
                style = "failing_test_result";
                emoji = Constants.EMOJI_FAILING_TEST;
                break;
            default:
                style = "inconclusive_test_result";
                emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
                break;
        }

        return showMemoir(subordinate, emoji, style);
    }

    /**
     * This will call on JUnit's Assertions.fail() to fail the test without
     * supplying a message. The log will show "(programmer forced failure)".
     * @return According to JUnit's documentation, the generic return type
     * allows this method to be used
     * directly as a single-statement lambda expression.
     */
    public <V> V fail() {
        try {
            return Assertions.fail();
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info("(programmer forced failure)", Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            reportCondition(Status.FAILING);
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * This will call on JUnit's Assertions.fail() with the message you supply.
     * The log will show that message.
     * @param message - The message to show in the log.
     * @return According to JUnit's documentation, the generic return type
     *      allows this method to be used
     *      directly as a single-statement lambda expression.
     */
    public <V> V fail(String message) {
        try {
            return Assertions.fail(message);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            reportCondition(Status.FAILING);
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * This will call on JUnit's Assertions.fail() to fail the test
     * with the given exception as the cause. Memoir will display the
     * cause with its showThrowable() function.
     * @param cause The exception or throwable causing the failure.
     * @return According to JUnit's documentation, the generic return type
     * allows this method to be used directly as a single-statement
     * lambda expression.
     */
    public <V> V fail(Throwable cause) {
        try {
            return Assertions.fail(cause);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info("Failed because the following was thrown...", Constants.EMOJI_FAILING_TEST);
            showThrowable(cause);
            throw err;
        } catch (Throwable unexpectedErr) {
            reportCondition(Status.FAILING);
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * This will call on JUnit's Assertions.fail() to fail the test
     * with the given exception as the cause. Memoir will display the
     * cause with its showThrowable() function after first logging
     * the message.
     * @param message State in plain language why the test is failing.
     * @param cause The exception or throwable causing the failure.
     * @return According to JUnit's documentation, the generic return type
     * allows this method to be used directly as a single-statement
     * lambda expression.
     */
    public <V> V fail(String message, Throwable cause) {
        try {
            return Assertions.fail(cause);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            showThrowable(cause);
            throw err;
        } catch (Throwable unexpectedErr) {
            reportCondition(Status.FAILING);
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the supplied boolean condition is true.
     * The pass fail status will be logged.
     * @param condition A boolean condition that must be true to pass the assertion.
     */
    public void assertTrue(boolean condition) {
        try {
            reportCondition(Status.PASSING);
            Assertions.assertTrue(condition);
            info("(true as expected)", Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info("A condition expected to be true was false.", Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            reportCondition(Status.FAILING);
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the supplied boolean condition is true.
     * The message will be logged, along with an emoji to indicate pass fail status.
     * @param condition A boolean condition that must be true to pass the assertion.
     * @param message State in plain language what you are asserting.
     */
    public void assertTrue(boolean condition, String message) {
        try {
            Assertions.assertTrue(condition);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the supplied boolean condition is true.
     * The pass fail status will be logged.
     * @param condition A boolean condition that must be true to pass the assertion.
     */
    public void assertTrue(BooleanSupplier condition) {
        try {
            Assertions.assertTrue(condition);
            reportCondition(Status.PASSING);
            info("(true as expected)", Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info("A supplied condition expected to be true was false.", Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            reportCondition(Status.FAILING);
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the supplied boolean condition is true.
     * The message will be logged, along with an emoji to indicate pass fail status.
     * @param condition A boolean condition that must be true to pass the assertion.
     * @param message State in plain language what you are asserting.
     */
    public void assertTrue(BooleanSupplier condition, String message) {
        try {
            Assertions.assertTrue(condition);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied ThrowingSupplier
     * completes before the given timeout is exceeded.
     * @param timeout Duration within which execution must first complete.
     * @param supplier Will be executed in a different thread than the calling code.
     * @param message State in plain language what you are asserting.
     * @return If the assertion passes then the supplier's result is returned.
     */
    public <T> T assertTimeoutPreemptively(Duration timeout, ThrowingSupplier<T> supplier, String message) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            reportCondition(Status.PASSING);
            return Assertions.assertTimeoutPreemptively(timeout, supplier, message);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info(message, emoji);
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied ThrowingSupplier
     * completes before the given timeout is exceeded.
     * @param timeout Duration within which execution must first complete.
     * @param supplier Will be executed in a different thread than the calling code.
     * @return If the assertion passes then the supplier's result is returned.
     */
    public <T> T assertTimeoutPreemptively(Duration timeout, ThrowingSupplier<T> supplier) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            reportCondition(Status.PASSING);
            return Assertions.assertTimeoutPreemptively(timeout, supplier);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assert Timeout Preemptively", emoji);
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied Executable
     * completes before the given timeout is exceeded.
     * @param timeout Duration within which execution must first complete.
     * @param executable Will be executed in a different thread than the calling code.
     * @param message State in plain language what you are asserting.
     * @return If the assertion passes then the supplier's result is returned.
     */
    public void assertTimeoutPreemptively(Duration timeout, Executable executable, String message) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assertions.assertTimeoutPreemptively(timeout, executable, message);
            reportCondition(Status.PASSING);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info(message, emoji);
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied Executable
     * completes before the given timeout is exceeded.
     * @param timeout Duration within which execution must first complete.
     * @param executable Will be executed in a different thread than the calling code.
     * @return If the assertion passes then the supplier's result is returned.
     */
    public void assertTimeoutPreemptively(Duration timeout, Executable executable) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assertions.assertTimeoutPreemptively(timeout, executable);
            reportCondition(Status.PASSING);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assert Timeout Preemptively", emoji);
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied ThrowingSupplier
     * completes before the given timeout is exceeded.
     * @param timeout Duration within which execution must first complete.
     * @param supplier Will be executed in the SAME thread as the calling code. Thus
     *                 will NOT be aborted if the timeout is exceeded.
     * @param message State in plain language what you are asserting.
     * @return If the assertion passes then the supplier's result is returned.
     */
    public <T> T assertTimeout(Duration timeout, ThrowingSupplier<T> supplier, String message) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            reportCondition(Status.PASSING);
            return Assertions.assertTimeout(timeout, supplier, message);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info(message, emoji);
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied ThrowingSupplier
     * completes before the given timeout is exceeded.
     * @param timeout Duration within which execution must first complete.
     * @param supplier Will be executed in the SAME thread as the calling code. Thus
     *                 will NOT be aborted if the timeout is exceeded.
     * @return If the assertion passes then the supplier's result is returned.
     */
    public <T> T assertTimeout(Duration timeout, ThrowingSupplier<T> supplier) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            reportCondition(Status.PASSING);
            return Assertions.assertTimeout(timeout, supplier);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assert Timeout", emoji);
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied Executable
     * completes before the given timeout is exceeded.
     * @param timeout Duration within which execution must first complete.
     * @param executable Will be executed in the SAME thread as the calling code. Thus
     *                 will NOT be aborted if the timeout is exceeded.
     * @param message State in plain language what you are asserting.
     * @return If the assertion passes then the supplier's result is returned.
     */
    public void assertTimeout(Duration timeout, Executable executable, String message) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assertions.assertTimeout(timeout, executable, message);
            reportCondition(Status.PASSING);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info(message, emoji);
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied Executable
     * completes before the given timeout is exceeded.
     * @param timeout Duration within which execution must first complete.
     * @param executable Will be executed in the SAME thread as the calling code. Thus
     *                 will NOT be aborted if the timeout is exceeded.
     * @return If the assertion passes then the supplier's result is returned.
     */
    public void assertTimeout(Duration timeout, Executable executable) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assertions.assertTimeout(timeout, executable);
            reportCondition(Status.PASSING);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assert Timeout", emoji);
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied Executable throws
     * the expected exception type.
     * @param expectedType The type of exception that should be thrown.
     * @param executable Will be executed either to completion or the throwing of an exception.
     * @param message State in plain language what you are asserting.
     * @return The actual exception thrown will be returned.
     */
    public <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable, String message) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            reportCondition(Status.PASSING);
            return Assertions.assertThrows(expectedType, executable, message);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info(message, emoji);
        }
    }

    /**
     * Through JUnit asserts that execution of the supplied Executable throws
     * the expected exception type.
     * @param expectedType The type of exception that should be thrown.
     * @param executable Will be executed either to completion or the throwing of an exception.
     * @return The actual exception thrown will be returned.
     */
    public <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            reportCondition(Status.PASSING);
            return Assertions.assertThrows(expectedType, executable);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Expected to throw " + expectedType.getSimpleName(), emoji);
        }
    }

    /**
     * Through JUnit asserts that the expected and actual are the same object.
     * @param expected The object you expect to get.
     * @param actual The object you actually got.
     * @param message State in plain language what you are asserting.
     */
    public void assertSame(Object expected, Object actual, String message) {
        try {
            Assertions.assertSame(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual are the same object.
     * @param expected The object you expect to get.
     * @param actual The object you actually got.
     */
    public void assertSame(Object expected, Object actual) {
        final String msg = "Assert Objects are Same";
        try {
            Assertions.assertSame(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual are NOT the same object.
     * @param expected The object you expect to NOT get.
     * @param actual The object you actually got.
     * @param message State in plain language what you are asserting.
     */
    public void assertNotSame(Object expected, Object actual, String message) {
        try {
            Assertions.assertNotSame(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual are NOT the same object.
     * @param expected The object you expect to NOT get.
     * @param actual The object you actually got.
     */
    public void assertNotSame(Object expected, Object actual) {
        final String msg = "Assert Objects are not Same";
        try {
            Assertions.assertNotSame(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the object in question is null.
     * @param actual The object you actually got.
     * @param message State in plain language what you are asserting.
     */
    public void assertNull(Object actual, String message) {
        try {
            Assertions.assertNull(actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the object in question is null.
     * @param actual The object you actually got.
     */
    public void assertNull(Object actual) {
        final String msg = "Assert Object is Null";
        try {
            Assertions.assertNull(actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the object in question is NOT null.
     * @param actual The object you actually got.
     * @param message State in plain language what you are asserting.
     */
    public void assertNotNull(Object actual, String message) {
        try {
            Assertions.assertNotNull(actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the object in question is NOT null.
     * @param actual The object you actually got.
     */
    public void assertNotNull(Object actual) {
        final String msg = "Assert Object is Null";
        try {
            Assertions.assertNotNull(actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the supplied boolean condition is false.
     * The pass fail status will be logged.
     * @param condition A boolean condition that must be true to pass the assertion.
     */
    public void assertFalse(boolean condition) {
        try {
            reportCondition(Status.PASSING);
            Assertions.assertFalse(condition);
            info("(true as expected)", Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info("A condition expected to be false was true.", Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            reportCondition(Status.FAILING);
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the supplied boolean condition is false.
     * The message will be logged, along with an emoji to indicate pass fail status.
     * @param condition A boolean condition that must be true to pass the assertion.
     * @param message State in plain language what you are asserting.
     */
    public void assertFalse(boolean condition, String message) {
        try {
            Assertions.assertFalse(condition);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the supplied boolean condition is false.
     * The pass fail status will be logged.
     * @param condition A boolean condition that must be true to pass the assertion.
     */
    public void assertFalse(BooleanSupplier condition) {
        try {
            Assertions.assertFalse(condition);
            reportCondition(Status.PASSING);
            info("(true as expected)", Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info("A supplied condition expected to be false was true.", Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            reportCondition(Status.FAILING);
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the supplied boolean condition is false.
     * The message will be logged, along with an emoji to indicate pass fail status.
     * @param condition A boolean condition that must be true to pass the assertion.
     * @param message State in plain language what you are asserting.
     */
    public void assertFalse(BooleanSupplier condition, String message) {
        try {
            Assertions.assertFalse(condition);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual objects are NOT equal in value.
     * @param expected The object you expect.
     * @param actual The actual object you are testing.
     * @param message State in plain language what you are asserting.
     */
    public void assertNotEquals(Object expected, Object actual, String message) {
        try {
            Assertions.assertNotEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual objects are equal in value.
     * @param expected The object you expect.
     * @param actual The actual object you are testing.
     */
    public void assertNotEquals(Object expected, Object actual) {
        final String msg = "Assert Objects are not Equals";
        try {
            Assertions.assertNotEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual lists of Strings match each other.
     * @param expectedLines The expected list of Strings.
     * @param actualLines The actual list of Strings being tested.
     */
    public void assertLinesMatch(List<String> expectedLines, List<String> actualLines) {
        final String msg = "Assert Lines Match";
        try {
            Assertions.assertLinesMatch(expectedLines, actualLines);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual iterables are deeply equal.
     * @param expected The expected iterable taken as "known good."
     * @param actual The actual iterable being tested.
     * @param message State in plain language what you are asserting.
     */
    public void assertIterableEquals(Iterable<?> expected, Iterable<?> actual, String message) {
        try {
            Assertions.assertIterableEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual iterables are deeply equal.
     * @param expected The expected iterable taken as "known good."
     * @param actual The actual iterable being tested.
     */
    public void assertIterableEquals(Iterable<?> expected, Iterable<?> actual) {
        final String msg = "Assert Iterables are Equal";
        try {
            Assertions.assertIterableEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual objects are equal in value.
     * @param expected The object you expect.
     * @param actual The actual object you are testing.
     * @param message State in plain language what you are asserting.
     */
    public void assertEquals(Object expected, Object actual, String message) {
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual objects are equal in value.
     * @param expected The object you expect.
     * @param actual The actual object you are testing.
     */
    public void assertEquals(Object expected, Object actual) {
        final String msg = "Assert Objects are Equals";
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual bytes are equal in value.
     * @param expected The byte you expect.
     * @param actual The actual byte you are testing.
     * @param message State in plain language what you are asserting.
     */
    public void assertEquals(byte expected, byte actual, String message) {
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual bytes are equal in value.
     * @param expected The byte you expect.
     * @param actual The actual byte you are testing.
     */
    public void assertEquals(byte expected, byte actual) {
        final String msg = "Assert bytes are Equals";
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual chars are equal in value.
     * @param expected The char you expect.
     * @param actual The actual char you are testing.
     * @param message State in plain language what you are asserting.
     */
    public void assertEquals(char expected, char actual, String message) {
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual chars are equal in value.
     * @param expected The char you expect.
     * @param actual The actual char you are testing.
     */
    public void assertEquals(char expected, char actual) {
        final String msg = "Assert chars are Equals";
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual doubles are equal in value.
     * @param expected The double you expect.
     * @param actual The actual double you are testing.
     * @param message State in plain language what you are asserting.
     */
    public void assertEquals(double expected, double actual, String message) {
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual doubles are equal in value.
     * @param expected The double you expect.
     * @param actual The actual double you are testing.
     */
    public void assertEquals(double expected, double actual) {
        final String msg = "Assert doubles are Equals";
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual floats are equal in value.
     * @param expected The float you expect.
     * @param actual The actual float you are testing.
     * @param message State in plain language what you are asserting.
     */
    public void assertEquals(float expected, float actual, String message) {
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual floats are equal in value.
     * @param expected The float you expect.
     * @param actual The actual float you are testing.
     */
    public void assertEquals(float expected, float actual) {
        final String msg = "Assert floats are Equals";
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual ints are equal in value.
     * @param expected The int you expect.
     * @param actual The actual int you are testing.
     * @param message State in plain language what you are asserting.
     */
    public void assertEquals(int expected, int actual, String message) {
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual ints are equal in value.
     * @param expected The int you expect.
     * @param actual The actual int you are testing.
     */
    public void assertEquals(int expected, int actual) {
        final String msg = "Assert ints are Equals";
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual longs are equal in value.
     * @param expected The long you expect.
     * @param actual The actual long you are testing.
     * @param message State in plain language what you are asserting.
     */
    public void assertEquals(long expected, long actual, String message) {
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual longs are equal in value.
     * @param expected The long you expect.
     * @param actual The actual long you are testing.
     */
    public void assertEquals(long expected, long actual) {
        final String msg = "Assert longs are Equals";
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual shorts are equal in value.
     * @param expected The short you expect.
     * @param actual The actual short you are testing.
     * @param message State in plain language what you are asserting.
     */
    public void assertEquals(short expected, short actual, String message) {
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual shorts are equal in value.
     * @param expected The short you expect.
     * @param actual The actual short you are testing.
     */
    public void assertEquals(short expected, short actual) {
        final String msg = "Assert shorts are Equals";
        try {
            Assertions.assertEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     * @param message State in plain language what you are asserting.
     */
    public void assertArrayEquals(Object[] expected, Object[] actual, String message) {
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     */
    public void assertArrayEquals(Object[] expected, Object[] actual) {
        final String msg = "Assert Arrays of Objects are Equals";
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     * @param message State in plain language what you are asserting.
     */
    public void assertArrayEquals(byte[] expected, byte[] actual, String message) {
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     */
    public void assertArrayEquals(byte[] expected, byte[] actual) {
        final String msg = "Assert Arrays of bytes are Equals";
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     * @param message State in plain language what you are asserting.
     */
    public void assertArrayEquals(char[] expected, char[] actual, String message) {
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     */
    public void assertArrayEquals(char[] expected, char[] actual) {
        final String msg = "Assert Arrays of chars are Equals";
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     * @param message State in plain language what you are asserting.
     */
    public void assertArrayEquals(double[] expected, double[] actual, String message) {
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     */
    public void assertArrayEquals(double[] expected, double[] actual) {
        final String msg = "Assert Arrays of doubles are Equals";
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     * @param message State in plain language what you are asserting.
     */
    public void assertArrayEquals(float[] expected, float[] actual, String message) {
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     */
    public void assertArrayEquals(float[] expected, float[] actual) {
        final String msg = "Assert Arrays of floats are Equals";
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     * @param message State in plain language what you are asserting.
     */
    public void assertArrayEquals(int[] expected, int[] actual, String message) {
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     */
    public void assertArrayEquals(int[] expected, int[] actual) {
        final String msg = "Assert Arrays of ints are Equals";
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     * @param message State in plain language what you are asserting.
     */
    public void assertArrayEquals(long[] expected, long[] actual, String message) {
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     */
    public void assertArrayEquals(long[] expected, long[] actual) {
        final String msg = "Assert Arrays of longs are Equals";
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     * @param message State in plain language what you are asserting.
     */
    public void assertArrayEquals(short[] expected, short[] actual, String message) {
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    /**
     * Through JUnit asserts that the expected and actual arrays are equal.
     * @param expected The expected "known good" array.
     * @param actual The actual array being tested.
     */
    public void assertArrayEquals(short[] expected, short[] actual) {
        final String msg = "Assert Arrays of shorts are Equals";
        try {
            Assertions.assertArrayEquals(expected, actual);
            reportCondition(Status.PASSING);
            info(msg, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            info(msg, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }

    public void assertAll(Executable... executables) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assertions.assertAll(executables);
            reportCondition(Status.PASSING);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assert all supplied executables don't throw exceptions", emoji);
        }
    }

    /**
     * Through JUnit asserts that all supplied executables do NOT throw exceptions.
     * @param executables The supplied executables in a Stream.
     */
    public void assertAll(Stream<Executable> executables) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assertions.assertAll(executables);
            reportCondition(Status.PASSING);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assert all supplied executables don't throw exceptions", emoji);
        }
    }

    /**
     * Through JUnit asserts that all supplied executables do NOT throw exceptions.
     * @param executables The supplied executables in a vararg.
     */
    public void assertAll(String heading, Executable... executables) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assertions.assertAll(heading, executables);
            reportCondition(Status.PASSING);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assert all supplied executables don't throw exceptions", emoji);
        }
    }

    /**
     * Through JUnit asserts that all supplied executables do NOT throw exceptions.
     * @param executables The supplied executables in a Stream.
     * @param heading Will be included in the message string for the MultipleFailuresError.
     */
    public void assertAll(String heading, Stream<Executable> executables) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assertions.assertAll(heading, executables);
            reportCondition(Status.PASSING);
        } catch (AssertionFailedError err) {
            reportCondition(Status.FAILING);
            emoji = Constants.EMOJI_FAILING_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assert all supplied executables don't throw exceptions", emoji);
        }
    }




    /*
    // For now, not implementing wrappers for any of the
    // functions with Supplier<String> messageSupplier
    // until I'm familiar with how they work. If it's
    // possible and frequent for them to return a
    // different item with get() each time then
    // implementing the wrappers could be complicated
    // because I'd need to get a message but still preserve
    // it for JUnit Assertions to consume.
    public <V> V fail(Supplier<String> messageSupplier) {
        try {
            return Assertions.fail(messageSupplier);
        } catch (AssertionFailedError err) {
            isFailing = true;
            info(messageSupplier.get(), Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }
    */

    // ASSUMPTIONS
    // ===========

    /**
     * Through JUnit tests if the boolean assumption condition is false.
     * If it's not false a TestAbortedException is thrown. (This does
     * not explicitly fail the test.)
     * @param assumption The boolean condition being tested.
     */
    public void assumeFalse(boolean assumption) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumeFalse(assumption);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("A condition assumed false was true instead", emoji);
        }
    }

    /**
     * Through JUnit tests if the boolean assumption condition is false.
     * If it's not false a TestAbortedException is thrown. (This does
     * not explicitly fail the test.)
     * @param assumption The boolean condition being tested.
     * @param message State in plain language what the test assumes.
     */
    public void assumeFalse(boolean assumption, String message) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumeFalse(assumption, message);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info(message, emoji);
        }
    }

    /**
     * Through JUnit tests if the supply of boolean assumption conditions are false.
     * If it's not false a TestAbortedException is thrown. (This does
     * not explicitly fail the test.)
     * @param assumptionSupplier The supply of boolean conditions being tested.
     */
    public void assumeFalse(BooleanSupplier assumptionSupplier) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumeFalse(assumptionSupplier);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("A condition assumed false was true instead", emoji);
        }
    }

    /**
     * Through JUnit tests if the supply of boolean assumption conditions are false.
     * If it's not false a TestAbortedException is thrown. (This does
     * not explicitly fail the test.)
     * @param assumptionSupplier The supply of boolean conditions being tested.
     * @param message State in plain language what the test assumes.
     */
    public void assumeFalse(BooleanSupplier assumptionSupplier, String message) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumeFalse(assumptionSupplier, message);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info(message, emoji);
        }
    }

    /**
     * Through JUnit tests if the boolean assumption condition is true.
     * If it's not true a TestAbortedException is thrown. (This does
     * not explicitly fail the test.)
     * @param assumption The boolean condition being tested.
     */
    public void assumeTrue(boolean assumption) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumeTrue(assumption);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("A condition assumed true was false instead", emoji);
        }
    }

    /**
     * Through JUnit tests if the boolean assumption condition is true.
     * If it's not true a TestAbortedException is thrown. (This does
     * not explicitly fail the test.)
     * @param assumption The boolean condition being tested.
     * @param message State in plain language what the test assumes.
     */
    public void assumeTrue(boolean assumption, String message) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumeTrue(assumption, message);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info(message, emoji);
        }
    }

    /**
     * Through JUnit tests if the supply of boolean assumption conditions are true.
     * If it's not true a TestAbortedException is thrown. (This does
     * not explicitly fail the test.)
     * @param assumptionSupplier The supply of boolean conditions being tested.
     */
    public void assumeTrue(BooleanSupplier assumptionSupplier) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumeTrue(assumptionSupplier);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("A condition assumed true was false instead", emoji);
        }
    }

    /**
     * Through JUnit tests if the supply of boolean assumption conditions are true.
     * If it's not true a TestAbortedException is thrown. (This does
     * not explicitly fail the test.)
     * @param assumptionSupplier The supply of boolean conditions being tested.
     * @param message State in plain language what the test assumes.
     */
    public void assumeTrue(BooleanSupplier assumptionSupplier, String message) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumeTrue(assumptionSupplier, message);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info(message, emoji);
        }
    }

    /**
     * Esecutes the supplied executable, but ONLY if the assumption is true.
     * Unlike other assumption methods, this will NOT abort the test if it fails.
     * @param assumption The boolean condition being tested.
     * @param executable The executable to run if the assumption is true.
     */
    public void assumingThat(boolean assumption, Executable executable) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumingThat(assumption, executable);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assumption not valid; Executable not run", emoji);
        }
    }

    /**
     * Esecutes the supplied executable, but ONLY if the assumption is true.
     * Unlike other assumption methods, this will NOT abort the test if it fails.
     * @param assumptionSupplier The supply of boolean conditions being tested.
     * @param executable The executable to run if the assumption is true.
     */
    public void assumingThat(BooleanSupplier assumptionSupplier, Executable executable) {
        String emoji = Constants.EMOJI_PASSING_TEST;
        try {
            Assumptions.assumingThat(assumptionSupplier, executable);
            reportCondition(Status.PASSING);
        } catch (TestAbortedException err) {
            reportCondition(Status.INCONCLUSIVE);
            emoji = Constants.EMOJI_INCONCLUSIVE_TEST;
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        } finally {
            info("Assumption not valid; Executable not run", emoji);
        }
    }
}
