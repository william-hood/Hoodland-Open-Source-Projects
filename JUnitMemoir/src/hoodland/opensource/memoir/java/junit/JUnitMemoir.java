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

    public Status status = Status.UNSET;

    private void reportCondition(Status thisCondition) {
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
