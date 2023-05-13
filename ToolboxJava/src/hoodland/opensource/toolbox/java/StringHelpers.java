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
    public static String createStringFromBasisCharacter(char basisChar, int length) {
        return StringExtensionsKt.createStringFromBasisCharacter(basisChar, length);
    }

    public static String repeatString(String target, int count) {
        return StringExtensionsKt.repeatString(target, count);
    }

    public static String getDimensional(int x, int y) {
        return StringExtensionsKt.getDimensional(x, y);
    }

// TODO: Reverse is not in the Kotlin version. Built into Kotlin.
// TODO: IsBlank is not in the Kotlin version. Built into Kotlin.

    public static Boolean stringsMatch(String thisString, String theOther) {
        return StringExtensionsKt.matches(thisString, theOther);
    }

    public static Boolean StringsMatchCaseInspecific(String thisString, String theOther) {
        return StringExtensionsKt.matchesCaseInspecific(thisString, theOther);
    }

    public static String makeParenthetic(String target) {
        return StringExtensionsKt.makeParenthetic(target);
    }

    public static String makeQuoted(String target) {
        return StringExtensionsKt.makeQuoted(target);
    }

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
