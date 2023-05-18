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

import hoodland.opensource.toolbox.ToolsKt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Random;

/**
 * Provides miscellaneous programming helper and tool functions.
 */
public class Tools {

    /**
     * Provides a PrintWriter pointed at stdout
     */
    public static PrintWriter stdout = ToolsKt.getStdout();

    /**
     * Provides a PrintWriter pointed at stderr
     */
    public static PrintWriter stderr = ToolsKt.getStderr();

    /**
     * Provides a plaintext date and time formatted as yyyy-MM-dd kk-mm-ss.SSS
     */
    public static String quickTimestamp = ToolsKt.getQuickTimestamp();

    /**
     * Given a path to an existing file, provides an open BufferedReader for it.
     */
    public static BufferedReader openForReading(String filePath) throws FileNotFoundException {
        return ToolsKt.openForReading(filePath);
    }

    public static String filterOutNonPrintables(String candidate) {
        return ToolsKt.filterOutNonPrintables(candidate);
    }

    public static String robustGetString(Object candidate) {
        return ToolsKt.robustGetString(candidate);
    }

    public static String getOperatingSystemName() {
        return ToolsKt.getOperatingSystemName();
    }

    public static String getCurrentWorkingDirectory() {
        return ToolsKt.getCurrentWorkingDirectory();
    }

    public static String getUserHomeFolder() {
        return ToolsKt.getUserHomeFolder();
    }

    /**
     * Provides a random Long integer between 0 and n
     * @param rng A random number generator (Random class) that you have already instantiated.
     * @param n The upper bound of the random Long integer. 0 is the lower bound.
     * @return a random Long integer between 0 and n
     */
    public static Long nextLong(Random rng, Long n) {
        return ToolsKt.nextLong(rng, n);
    }

    public static int randomInt(int min, int max) {
        return ToolsKt.randomInt(min, max);
    }

    public static int randomInteger(int min, int max) {
        return randomInt(min, max);
    }

    public static Boolean stringArrayContains(String[] candidateArray, String candidateString) {
        return ToolsKt.stringArrayContains(candidateArray, candidateString);
    }

    public static Boolean stringArrayContainsCaseInspecific(String[] candidateArray, String candidateString) {
        return ToolsKt.stringArrayContainsCaseInspecific(candidateArray, candidateString);
    }

    public static ArrayList<String> sortMarkupTags(String target) {
        return ToolsKt.sortMarkupTags(target);
    }

    public static ArrayList<AbstractMap.SimpleEntry<String, String>> queryParamsAsNameValuePairs(URL target) {
        return ToolsKt.getQueryParamsAsNameValuePairs(target);
    }

    public static Long crc32ChecksumValue(File target) {
        return ToolsKt.getCrc32ChecksumValue(target);
    }
}
