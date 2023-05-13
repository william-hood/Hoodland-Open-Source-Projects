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

import hoodland.opensource.toolbox.Parser;

import java.util.ArrayList;

public class MatrixFile<T> {
    private hoodland.opensource.toolbox.MatrixFile<T> KMatrixFile;

    public static final char DEFAULT_DELIMITER = hoodland.opensource.toolbox.MatrixFile.DEFAULT_DELIMITER;
    public static final int DEFAULT_SPACING = hoodland.opensource.toolbox.MatrixFile.DEFAULT_SPACING;

    public ArrayList<ArrayList<T>> getAllData() {
        return KMatrixFile.getAllData();
    }

    public MatrixFile(char delimiter, int spacing, String... columnNames) {
        KMatrixFile = new hoodland.opensource.toolbox.MatrixFile<T>(delimiter, spacing, columnNames);
    }

    public MatrixFile(String... columnNames) {
        this(DEFAULT_DELIMITER, DEFAULT_SPACING, columnNames);
    }

    private MatrixFile(hoodland.opensource.toolbox.MatrixFile innerKotlinMatrixFile) {
        KMatrixFile = innerKotlinMatrixFile;
    }

    public void addDataRow(ArrayList<T> newDataRow) {
        KMatrixFile.addDataRow(newDataRow);
    }

    public void addDataRow(T... newData) {
        KMatrixFile.addDataRow(newData);
    }

    public void removeDataRow(String columnName, T value) {
        KMatrixFile.removeDataRow(columnName, value);
    }

    public int getSize() {
        return KMatrixFile.getSize();
    }

    public ArrayList<T> getDataRow(String columnName, T value) {
        return KMatrixFile.getDataRow(columnName, value);
    }

    public Boolean hasDataRow(String columnName, T value) {
        return KMatrixFile.hasDataRow(columnName, value);
    }

    public void write(String completeFilePath, Boolean append) {
        KMatrixFile.write(completeFilePath, append);
    }

    public void write(String completeFilePath) {
        write(completeFilePath, true);
    }

    public static Parser<Integer> IntParser = hoodland.opensource.toolbox.IntParser.INSTANCE;
    public static Parser<Float> FloatParser = hoodland.opensource.toolbox.FloatParser.INSTANCE;
    public static Parser<String> StringParser = hoodland.opensource.toolbox.StringParser.INSTANCE;

    public static <T>MatrixFile<T> read(String completeFilePath, char delimitingChar, Parser<T> parser) {
        return new MatrixFile<T>(hoodland.opensource.toolbox.MatrixFile.Companion.read(completeFilePath, delimitingChar, parser));
    }

    public static <T>MatrixFile<T> read(String completeFilePath, Parser<T> parser) {
        return read(completeFilePath, DEFAULT_DELIMITER, parser);
    }
}
