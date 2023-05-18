// Copyright (c) 2023 William Arthur Hood
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights to
// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
// of the Software, and to permit persons to whom the Software is furnished
// to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.

package hoodland.opensource.toolbox.java;

import hoodland.opensource.toolbox.StringExtensionsKt;
import hoodland.opensource.toolbox.VerticalJustification;

/**
 * Provides various string manipulation functions.
 */
public class StringHelpers {

    /**
     * createStringFromBasisCharacter: Creates a string composed of the requested number of the
     * basis character in a row.
     * @param basisChar The character to use. Every character in the string will be this.
     * @param length The total number of characters in the string.
     * @return A string consisting of exactly (length) characters in a row, all of which are the basis character.
     */
    public static String createStringFromBasisCharacter(char basisChar, int length) {
        return StringExtensionsKt.createStringFromBasisCharacter(basisChar, length);
    }

    /**
     * repeatString: Creates a new string with the requested number of the string it is given.
     * @param target The basis string which will be repeated.
     * @param count The number of times to repeat the target string.
     * @return A new string composed of exactly (count) copies of the (target) string.
     */
    public static String repeatString(String target, int count) {
        return StringExtensionsKt.repeatString(target, count);
    }

    /**
     * getDimensional
     * @param x
     * @param y
     * @return Given x and y returns "(x x y)"
     */
    public static String getDimensional(int x, int y) {
        return StringExtensionsKt.getDimensional(x, y);
    }

    /**
     * stringsMatch: Compares strings, even if one or both are null.
     * @param thisString
     * @param theOther
     * @return true if both strings are null or if both are exactly the same content. false otherwise.
     */
    public static Boolean stringsMatch(String thisString, String theOther) {
        return StringExtensionsKt.matches(thisString, theOther);
    }

    /**
     * stringsMatchCaseInspecific: Compares strings without considering upper or lower case, even if one or both are null.
     * @param thisString
     * @param theOther
     * @return true if both strings are null or if both are exactly the same content without distinguishing between upper and lower case. false otherwise.
     */
    public static Boolean stringsMatchCaseInspecific(String thisString, String theOther) {
        return StringExtensionsKt.matchesCaseInspecific(thisString, theOther);
    }

    /**
     * Encloses the target string in parentheses. Given "this string" returns "(this string)".
     * @param target
     * @return the target string in parentheses.
     */
    public static String makeParenthetic(String target) {
        return StringExtensionsKt.makeParenthetic(target);
    }


    /**
     * Encloses the target string in double quotes.
     * @param target
     * @return the target string enclosed in double quotes.
     */
    public static String makeQuoted(String target) {
        return StringExtensionsKt.makeQuoted(target);
    }

    /**
     * Encloses the target string in single quotes. Given "this string" returns "`this string`".
     * @param target
     * @return the target string in parentheses.
     */
    public static String makeSingleQuoted(String target) {
        return StringExtensionsKt.makeSingleQuoted(target);
    }

    /**
     * removeCarriageReturns: Removes all carriage returns (\r) from the target string.
     * DOES NOT ALSO REMOVE LINE FEED (\n) CHARACTERS. This function can be useful for
     * changing Windows-style endlines (\r\n) into Unix/Linux endlines (just \n).
     * @param target
     * @return The target string without any carriage return characters.
     */
    public static String removeCarriageReturns(String target) {
        return StringExtensionsKt.removeCarriageReturns(target);
    }

    /**
     * justify: Takes the target string and adds spaces and "Carriage Return/Line Feed" (Windows style)
     * endlines to produce a rendition justified to the given number of columns.
     * Windows-style endlines are used because macOS and Linux handle them without issue.
     * @param target The string to operate on.
     * @param columns The number of columns to justify to.
     * @return The original string rendered as justified to the specified number of columns.
     */
    public static String justify(String target, int columns) {
        return StringExtensionsKt.justify(target, columns);
    }

    /**
     * justify: Takes the target string and adds spaces and "Carriage Return/Line Feed" (Windows style)
     * endlines to produce a rendition justified to the default of 75 columns.
     * Windows-style endlines are used because macOS and Linux handle them without issue.
     * @param target The string to operate on.
     * @return The original string rendered as justified to 75 columns.
     */
    public static String justify(String target) {
        return justify(target, 75);
    }

    /**
     * prependEveryLineWith: Used to add a string to the beginning of every line in the target string.
     * THIS ASSUMES WINDOWS STYLE LINE ENDINGS (\r\n)
     * @param target
     * @param prependString This string to prepend every line with.
     * @return The target string with every line prepended by the requested string (prependString).
     */
    public static String prependEveryLineWith(String target, String prependString) {
        return StringExtensionsKt.prependEveryLineWith(target, prependString);
    }

    /**
     * indentEveryLineBy: Every line of the target string is prepended by (indentSize) space characters.
     * @param target
     * @param indentSize The number of spaces to indent each line by.
     * @return The target string rendered with each line indented.
     */
    public static String indentEveryLineBy(String target, int indentSize) {
        return StringExtensionsKt.indentEveryLineBy(target, indentSize);
    }

    /**
     * indentEveryLineBy5Chars: Every line of the target string is prepended by 5 space characters.
     * @param target
     * @return The target string rendered with each line indented by 5 spaces.
     */
    public static String indentEveryLineBy5Chars(String target) {
        return indentEveryLineBy(target, 5);
    }

    /**
     * padSides: Takes the target string and pads both sides with enough of the padding
     * character to make (totalSize) columns. This assumes the target string to be one line.
     * @param target
     * @param totalSize The total number of columns wide the string should be.
     * @param paddingChar The character to use for padding. Typically this is a blank space (' ').
     * @return The target string padded on both sides to make it (totalSize) characters wide.
     */
    public static String padSides(String target, int totalSize, char paddingChar) {
        return StringExtensionsKt.padSides(target, totalSize, paddingChar);
    }

    /**
     * padSides: Takes the target string and pads both sides with enough spaces
     * to make (totalSize) columns. This assumes the target string to be one line.
     * @param target
     * @param totalSize The total number of columns wide the string should be.
     * @return The target string padded on both sides to make it (totalSize) characters wide.
     */
    public static String padSides(String target, int totalSize) {
        return padSides(target, totalSize, ' ');
    }

    /**
     * padVerticalTop: Pads the target multi-line string with vertical rows at the top.
     * @param target
     * @param totalRows The total number of rows that should be in the message.
     * @param addHorizontalSpaces Set to true to add horizontal spaces to make every line the same width.
     * @return The target string padded vertically as requested.
     */
    public static String padVerticalTop(String target, int totalRows, Boolean addHorizontalSpaces) {
        return StringExtensionsKt.padVertical(target, totalRows, VerticalJustification.TOP, addHorizontalSpaces);
    }

    /**
     * padVerticalCenter: Pads the target multi-line string with vertical rows at both the top and bottom.
     * @param target
     * @param totalRows The total number of rows that should be in the message.
     * @param addHorizontalSpaces Set to true to add horizontal spaces to make every line the same width.
     * @return The target string padded vertically as requested.
     */
    public static String padVerticalCenter(String target, int totalRows, Boolean addHorizontalSpaces) {
        return StringExtensionsKt.padVertical(target, totalRows, VerticalJustification.CENTER, addHorizontalSpaces);
    }

    /**
     * padVerticalBottom: Pads the target multi-line string with vertical rows at the bottom.
     * @param target
     * @param totalRows The total number of rows that should be in the message.
     * @param addHorizontalSpaces Set to true to add horizontal spaces to make every line the same width.
     * @return The target string padded vertically as requested.
     */
    public static String padVerticalBottom(String target, int totalRows, Boolean addHorizontalSpaces) {
        return StringExtensionsKt.padVertical(target, totalRows, VerticalJustification.BOTTOM, addHorizontalSpaces);
    }

    /**
     * padLeft: Pads the target string on the left with the specified character to the total width of columns requested.
     * This assumes the target string to be one line.
     * @param target
     * @param totalWidth The total width the string should be after padding.
     * @param paddingChar The character to use for padding. Typically this is a blank space (' ').
     * @return The padded string as specified.
     */
    public static String padLeft(String target, int totalWidth, char paddingChar) {
        return StringExtensionsKt.padLeft(target, totalWidth, paddingChar);
    }

    /**
     * padLeft: Pads the target string on the left with spaces to the total width of columns requested.
     * This assumes the target string to be one line.
     * @param target
     * @param totalWidth The total width the string should be after padding.
     * @return The padded string as specified.
     */
    public static String padLeft(String target, int totalWidth) {
        return padLeft(target, totalWidth, ' ');
    }

    /**
     * padRight: Pads the target string on the right with the specified character to the total width of columns requested.
     * This assumes the target string to be one line.
     * @param target
     * @param totalWidth The total width the string should be after padding.
     * @param paddingChar The character to use for padding. Typically this is a blank space (' ').
     * @return The padded string as specified.
     */
    public static String padRight(String target, int totalWidth, char paddingChar) {
        return StringExtensionsKt.padRight(target, totalWidth, paddingChar);
    }

    /**
     * padRight: Pads the target string on the right with spaces to the total width of columns requested.
     * This assumes the target string to be one line.
     * @param target
     * @param totalWidth The total width the string should be after padding.
     * @return The padded string as specified.
     */
    public static String padRight(String target, int totalWidth) {
        return padRight(target, totalWidth, ' ');
    }
}
