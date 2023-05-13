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

    /**
     * @return The entire matrix of data without headers.
     */
    public ArrayList<ArrayList<T>> getAllData() {
        return KMatrixFile.getAllData();
    }

    /**
     * MatrixFile: Manages a new or existing file with delimited data, such as comma or tab separated values.
     * @param delimiter delimiter The character to serve as the delimiter. This is normally a comma ','. A semicolon ';' is also a good choice.
     * @param spacing Number of spaces after the delimiter before the next cell of data starts. Defaults to 1.
     * @param columnNames The names of the columns to use. These will be the headers in the first row of the data.
     */
    public MatrixFile(char delimiter, int spacing, String... columnNames) {
        KMatrixFile = new hoodland.opensource.toolbox.MatrixFile<T>(delimiter, spacing, columnNames);
    }

    /**
     * MatrixFile: Manages a new or existing file with delimited data, such as comma or tab separated values.
     * This will use the default delimiter (a comma ',') and the default spacing of 1.
     * @param columnNames The names of the columns to use. These will be the headers in the first row of the data.
     */
    public MatrixFile(String... columnNames) {
        this(DEFAULT_DELIMITER, DEFAULT_SPACING, columnNames);
    }

    private MatrixFile(hoodland.opensource.toolbox.MatrixFile innerKotlinMatrixFile) {
        KMatrixFile = innerKotlinMatrixFile;
    }

    /**
     * addDataRow: Adds the supplied ArrayList of data as a new row at the bottom of the file. Size of the list matching the number of columns is not enforced. (Omitting columns on the end, or adding extra ones, is allowed.)
     *
     * @param newDataRow: An ArrayList of this MatrixFile's data type.
     */
    public void addDataRow(ArrayList<T> newDataRow) {
        KMatrixFile.addDataRow(newDataRow);
    }

    /**
     * addDataRow: Adds the supplied list of data as a new row at the bottom of the file. Size of the list matching the number of columns is not enforced. (Omitting columns on the end, or adding extra ones, is allowed.)
     *
     * @param newData One or more parameters of the MatrixFile's data type. Size of the list matching the number of columns is not enforced. (Omitting columns on the end, or adding extra ones, is allowed.)
     */
    public void addDataRow(T... newData) {
        KMatrixFile.addDataRow(newData);
    }

    /**
     * removeDataRow: Removes the entire row of data where the named column contains the supplied value.
     * If more than one exists, only the first one from the top is removed.
     *
     * @param columnName Name of the column to match the value.
     * @param value Value to match in the supplied column.
     */
    public void removeDataRow(String columnName, T value) {
        KMatrixFile.removeDataRow(columnName, value);
    }

    /**
     * @return The size, which is the number of headers.
     */
    public int getSize() {
        return KMatrixFile.getSize();
    }

    /**
     * getDataRow: Gets the entire row of data where the named column contains the supplied value.
     * If more than one exists, the first one from the top is retrieved.
     *
     * @param columnName Name of the column to match the value.
     * @param value Value to match in the supplied column.
     * @return Returns the first row of data where the column name and supplied value match.
     */
    public ArrayList<T> getDataRow(String columnName, T value) {
        return KMatrixFile.getDataRow(columnName, value);
    }

    /**
     * hasDataRow: Use this to determine if a row of data exists where the named column has the supplied value.
     *
     * @param columnName Name of the column to match the value.
     * @param value Value to match in the supplied column.
     * @return Returns true if at least one row exists where the named column has the supplied value. Returns false if no match is found.
     */
    public Boolean hasDataRow(String columnName, T value) {
        return KMatrixFile.hasDataRow(columnName, value);
    }

    /**
     * write: This writes all of this MatrixFile's data to the supplied file name. It can optionally append to the end of an existing file.
     *
     * @param completeFilePath The fully qualified path and file name to write to.
     * @param append Set this to true if you want the output appended to an existing file.
     */
    public void write(String completeFilePath, Boolean append) {
        KMatrixFile.write(completeFilePath, append);
    }

    /**
     * write: This writes all of this MatrixFile's data to the supplied file name. It will append to the end of an existing file.
     *
     * @param completeFilePath The fully qualified path and file name to write to.
     */
    public void write(String completeFilePath) {
        write(completeFilePath, true);
    }

    public static Parser<Integer> IntParser = hoodland.opensource.toolbox.IntParser.INSTANCE;
    public static Parser<Float> FloatParser = hoodland.opensource.toolbox.FloatParser.INSTANCE;
    public static Parser<String> StringParser = hoodland.opensource.toolbox.StringParser.INSTANCE;

    /**
     * read: Constructs a MatrixFile instance based on the contents of an existing file.
     * Use String for the type if you're not sure.
     * @param completeFilePath The fully qualified path and file name to write to.
     * @param delimitingChar The character the file uses as a delimiter. This is typically a comma or semicolon.
     * @param parser Use StringParser, IntParser, or FloatParser depending on the expected data type. (Custom parsers can be created using the Parser<T> interface.)
     * @return Returns a MatrixFile instance with the contents of the specified file.
     */
    public static <T>MatrixFile<T> read(String completeFilePath, char delimitingChar, Parser<T> parser) {
        return new MatrixFile<T>(hoodland.opensource.toolbox.MatrixFile.Companion.read(completeFilePath, delimitingChar, parser));
    }

    /**
     * read: Constructs a MatrixFile instance based on the contents of an existing file. (This version uses the default delimiter.)
     * Use String for the type if you're not sure.
     * @param completeFilePath The fully qualified path and file name to write to.
     * @param parser Use StringParser, IntParser, or FloatParser depending on the expected data type. (Custom parsers can be created using the Parser<T> interface.)
     * @return Returns a MatrixFile instance with the contents of the specified file.
     */
    public static <T>MatrixFile<T> read(String completeFilePath, Parser<T> parser) {
        return read(completeFilePath, DEFAULT_DELIMITER, parser);
    }
}
