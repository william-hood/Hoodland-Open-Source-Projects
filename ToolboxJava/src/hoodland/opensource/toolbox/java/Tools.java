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

public class Tools {
    public static PrintWriter stdout = ToolsKt.getStdout();
    public static PrintWriter stderr = ToolsKt.getStderr();
    public static String quickTimestamp = ToolsKt.getQuickTimestamp();

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
