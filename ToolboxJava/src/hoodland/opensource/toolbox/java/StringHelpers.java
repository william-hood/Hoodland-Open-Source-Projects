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
     * Encloses the target string in parentheses. Given "this string" returns "(this string)".
     * @param target
     * @return the target string in parentheses.
     */
    public static String makeSingleQuoted(String target) {
        return StringExtensionsKt.makeSingleQuoted(target);
    }

    public static String removeCarriageReturns(String target) {
        return StringExtensionsKt.removeCarriageReturns(target);
    }

    public static String justify(String target, int columns) {
        return StringExtensionsKt.justify(target, columns);
    }

    public static String justify(String target) {
        return justify(target, 75);
    }

    public static String prependEveryLineWith(String target, String prependString) {
        return StringExtensionsKt.prependEveryLineWith(target, prependString);
    }

    public static String indentEveryLineBy(String target, int indentSize) {
        return StringExtensionsKt.indentEveryLineBy(target, indentSize);
    }

    public static String indentEveryLineBy5Chars(String target) {
        return indentEveryLineBy(target, 5);
    }

    public static String padSides(String target, int totalSize, char paddingChar) {
        return StringExtensionsKt.padSides(target, totalSize, paddingChar);
    }

    public static String padSides(String target, int totalSize) {
        return padSides(target, totalSize, ' ');
    }

    public static String padVerticalTop(String target, int totalRows, Boolean addHorizontalSpaces) {
        return StringExtensionsKt.padVertical(target, totalRows, VerticalJustification.TOP, addHorizontalSpaces);
    }

    public static String padVerticalCenter(String target, int totalRows, Boolean addHorizontalSpaces) {
        return StringExtensionsKt.padVertical(target, totalRows, VerticalJustification.CENTER, addHorizontalSpaces);
    }

    public static String padVerticalBottom(String target, int totalRows, Boolean addHorizontalSpaces) {
        return StringExtensionsKt.padVertical(target, totalRows, VerticalJustification.BOTTOM, addHorizontalSpaces);
    }

    public static String padLeft(String target, int totalWidth, char paddingChar) {
        return StringExtensionsKt.padLeft(target, totalWidth, paddingChar);
    }

    public static String padLeft(String target, int totalWidth) {
        return padLeft(target, totalWidth, ' ');
    }

    public static String padRight(String target, int totalWidth, char paddingChar) {
        return StringExtensionsKt.padRight(target, totalWidth, paddingChar);
    }

    public static String padRight(String target, int totalWidth) {
        return padRight(target, totalWidth, ' ');
    }
}
