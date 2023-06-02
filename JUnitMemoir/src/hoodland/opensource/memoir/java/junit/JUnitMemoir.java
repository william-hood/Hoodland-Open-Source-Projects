package hoodland.opensource.memoir.java.junit;

import java.io.PrintWriter;
import java.util.function.Supplier;

import hoodland.opensource.memoir.java.Constants;
import hoodland.opensource.memoir.java.HeaderFunction;
import org.junit.jupiter.api.Assertions;

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
        } catch (AssertionError err) {
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
        } catch (AssertionError err) {
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
        } catch (AssertionError err) {
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
        } catch (AssertionError err) {
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
            Assertions.assertTrue(condition);
            reportCondition(Status.PASSING);
            info("(true as expected)", Constants.EMOJI_PASSING_TEST);
        } catch (AssertionError err) {
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
            reportCondition(Status.PASSING);
            Assertions.assertTrue(condition);
            info(message, Constants.EMOJI_PASSING_TEST);
        } catch (AssertionError err) {
            reportCondition(Status.FAILING);
            info(message, Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }







    /*
    // For now, not implementing wrappers for any of the
    // functions that take Supplier<*> until I'm familiar with
    // how they work. If it's possible and frequent for them
    // to return a different item with get() each time then
    // implementing the wrappers could be complicated.
    public <V> V fail(Supplier<String> messageSupplier) {
        try {
            return Assertions.fail(messageSupplier);
        } catch (AssertionError err) {
            isFailing = true;
            info(messageSupplier.get(), Constants.EMOJI_FAILING_TEST);
            throw err;
        } catch (Throwable unexpectedErr) {
            showThrowable(unexpectedErr);
            throw unexpectedErr;
        }
    }
    */
}
