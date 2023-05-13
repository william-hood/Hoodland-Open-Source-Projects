// Copyright (c) 2020, 2023 William Arthur Hood
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

package hoodland.opensource.toolbox

import java.io.BufferedReader
import java.io.IOException
import java.util.*

/**
 * MatrixFile: Manages a new or existing file with delimited data, such as comma or tab separated values.
 *
 * @param T The data type of the delimited data. Usually this is String.
 * @property delimiter The character to serve as the delimiter. THis defaults to a comma ','. A semicolon ';' is also a good choice.
 * @property spacing Number of spaces after the delimiter before the next cell of data starts. Defaults to 1.
 * @constructor
 *
 * @param columnNames The names of the columns to use. These will be the headers in the first row of the data.
 */
class MatrixFile<T>(var delimiter: Char = DEFAULT_DELIMITER, var spacing: Int = DEFAULT_SPACING, vararg columnNames: String) {
    private var headers: ArrayList<String> = ArrayList()
    val allData: ArrayList<ArrayList<T>> = ArrayList()

    init {
        headers.addAll(columnNames)
    }

    constructor(vararg columnNames: String): this(DEFAULT_DELIMITER, DEFAULT_SPACING, *columnNames)

    private fun stripDelimiters(input: String): String {
        return input.replace(delimiter, ' ')
    }

    /**
     * addDataRow: Adds the supplied ArrayList of data as a new row at the bottom of the file. Size of the list matching the number of columns is not enforced. (Omitting columns on the end, or adding extra ones, is allowed.)
     *
     * @param newDataRow: An ArrayList of this MatrixFile's data type.
     */
    fun addDataRow(newDataRow: ArrayList<T>) {
        allData.add(newDataRow)
    }

    /**
     * addDataRow: Adds the supplied list of data as a new row at the bottom of the file. Size of the list matching the number of columns is not enforced. (Omitting columns on the end, or adding extra ones, is allowed.)
     *
     * @param newData One or more parameters of the MatrixFile's data type. Size of the list matching the number of columns is not enforced. (Omitting columns on the end, or adding extra ones, is allowed.)
     */
    fun addDataRow(vararg newData: T) {
        val result = ArrayList<T>()
        result.addAll(newData)
        addDataRow(result)
    }

    /**
     * removeDataRow: Removes the entire row of data where the named column contains the supplied value.
     * If more than one exists, only the first one from the top is removed.
     *
     * @param columnName Name of the column to match the value.
     * @param value Value to match in the supplied column.
     */
    fun removeDataRow(columnName: String, value: T) {
        val dataRow = getDataRow(columnName, value)
        if (dataRow != null) {
            allData.remove(dataRow)
        }
    }

    val size: Int
    get() {
        if (headers.size < 1) {
            throw ImproperConstructionException("A ${this.javaClass.simpleName} must have at least one header column.")
        }
        return headers.size
    }

    /**
     * getDataRow: Gets the entire row of data where the named column contains the supplied value.
     * If more than one exists, the first one from the top is retrieved.
     *
     * @param columnName Name of the column to match the value.
     * @param value Value to match in the supplied column.
     * @return Returns the first row of data where the column name and supplied value match.
     */
    fun getDataRow(columnName: String, value: T): ArrayList<T>? {
        var selectedColumn: Int? = null
        for (columnCursor in headers.indices) {
            if (headers[columnCursor] == columnName) {
                selectedColumn = columnCursor
                break
            }
        }
        if (selectedColumn == null) return null
        for (thisDataRow in allData) {
            if (thisDataRow[selectedColumn] == value) return thisDataRow
        }
        return null
    }

    /**
     * hasDataRow: Use this to determine if a row of data exists where the named column has the supplied value.
     *
     * @param columnName Name of the column to match the value.
     * @param value Value to match in the supplied column.
     * @return Returns true if at least one row exists where the named column has the supplied value. Returns false if no match is found.
     */
    fun hasDataRow(columnName: String, value: T): Boolean {
        return getDataRow(columnName, value) != null
    }

    private fun <SPECIFIED_TYPE> lineOut(dataRow: ArrayList<SPECIFIED_TYPE>): String {
        val lineOutBuilder = StringBuilder()
        for (cursor in dataRow.indices) {
            if (cursor != 0) {
                lineOutBuilder.append(delimiter)
                lineOutBuilder.append(createStringFromBasisCharacter(' ', spacing))
            }
            lineOutBuilder.append(stripDelimiters(dataRow[cursor].toString().trim()))
        }
        return lineOutBuilder.toString()
    }

    /**
     * write: This writes all of this MatrixFile's data to the supplied file name. It can optionally append to the end of an existing file.
     *
     * @param completeFilePath The fully qualified path and file name to write to.
     * @param append Set this to true if you want the output appended to an existing file.
     */
    fun write(completeFilePath: String, append: Boolean = true) {
        val thisOutputManager = QuantumTextFile(completeFilePath, append)
        thisOutputManager.println(lineOut<String>(headers))
        for (thisData in allData) {
            thisOutputManager.println(lineOut<T>(thisData))
        }
        thisOutputManager.close()
    }

    private fun stringRowFromTextLine(textLine: String): ArrayList<String> {
        val result = ArrayList<String>()
        val theseRows: Array<String> = textLine.split(("" + delimiter).toRegex()).toTypedArray()
        for (thisRow in theseRows) {
            result.add(thisRow.replace("" + delimiter, ""))
        }
        return result
    }

    private fun dataRowFromTextLine(textLine: String, parser: Parser<T>): ArrayList<T> {
        val textRow = stringRowFromTextLine(textLine)
        val thisRow = ArrayList<T>(textRow.size)
        /*
		 * for (int cursor = 0; cursor < textRow.size(); cursor++) {
		 * thisRow.add((T) parser.parseMethod(textRow.get(cursor))); }
		 */
        for (thisTextRow in textRow) {
            thisRow.add(parser.parseMethod(thisTextRow))
            /*
            if (parser != null) {
                thisRow.add(parser.parseMethod(thisTextRow) as T)
            }
            */
        }
        return thisRow
    }

    companion object {
        const val DEFAULT_DELIMITER = ','
        const val DEFAULT_SPACING = 1

        /**
         * read: Constructs a MatrixFile instance based on the contents of an existing file. (This version uses the default delimiter.)
         *
         * @param T The data type for this MatrixFile. Use String if you're not sure.
         * @param completeFilePath The fully qualified path and file name to write to.
         * @param parser Use StringParser, IntParser, or FloatParser depending on the expected data type. (Custom parsers can be created using the Parser<T> interface.)
         * @return Returns a MatrixFile instance with the contents of the specified file.
         */
        fun <T> read(completeFilePath: String, parser: Parser<T>): MatrixFile<T> {
            return read(completeFilePath, DEFAULT_DELIMITER, parser)
        }

        /**
         * read: Constructs a MatrixFile instance based on the contents of an existing file. (This version uses the default delimiter.)
         *
         * @param T The data type for this MatrixFile. Use String if you're not sure.
         * @param completeFilePath The fully qualified path and file name to write to.
         * @param delimitingChar The character the file uses as a delimiter. This is typically a comma or semicolon.
         * @param parser Use StringParser, IntParser, or FloatParser depending on the expected data type. (Custom parsers can be created using the Parser<T> interface.)
         * @return Returns a MatrixFile instance with the contents of the specified file.
         */
        fun <T> read(completeFilePath: String, delimitingChar: Char, parser: Parser<T>): MatrixFile<T> {
            val dataFromFile = MatrixFile<T>()
            dataFromFile.delimiter = delimitingChar
            val fileStream: BufferedReader = openForReading(completeFilePath)

            var currentLine = fileStream.readLine()
            if (currentLine != null) dataFromFile.headers = dataFromFile
                    .stringRowFromTextLine(currentLine)
            while (currentLine != null) {
                currentLine = fileStream.readLine()
                if (currentLine != null) dataFromFile.allData.add(dataFromFile.dataRowFromTextLine(
                        currentLine, parser))
            }
            fileStream.close()
            return dataFromFile
        }
    }
}