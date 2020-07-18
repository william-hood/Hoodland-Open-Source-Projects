// Copyright (c) 2020 William Arthur Hood
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

class DelimitedDataManager<T> {
    private var headers: ArrayList<String> = ArrayList()
    val allData: ArrayList<ArrayList<T>> = ArrayList()
    var delimiter = DEFAULT_DELIMITER
    var spacing = DEFAULT_SPACING

    constructor(delimitingChar: Char, spacesAfterDelimitingChar: Int, vararg columnNames: String) {
        // for (int cursor = 0; cursor < columnNames.length; cursor++)
        for (thisColumn in columnNames) {
            headers.add(thisColumn)
        }
        delimiter = delimitingChar
        spacing = spacesAfterDelimitingChar
    }

    constructor(vararg columnNames: String) : this(DEFAULT_DELIMITER, DEFAULT_SPACING, *columnNames) {}

    fun stripDelimiters(input: String?): String? {
        return input!!.replace(delimiter, ' ')
    }

    fun addDataRow(newDataRow: ArrayList<T>) {
        allData.add(newDataRow)
    }

    fun removeDataRow(columnName: String, value: T) {
        val dataRow = getDataRow(columnName, value)
        if (dataRow != null) {
            allData.remove(dataRow)
        }
    }

    val size: Int
    get() {
        if (headers.size < 1) {
            throw ImproperObjectConstructionException("A ${this.javaClass.simpleName} must have at least one header column.")
        }
        return headers.size
    }

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
            lineOutBuilder.append(dataRow[cursor])
        }
        return lineOutBuilder.toString()
    }

    fun toFile(completeFilePath: String, append: Boolean = true) {
        val thisOutputManager = TextOutputManager(completeFilePath, append)
        thisOutputManager.println(lineOut<String>(headers))
        for (thisData in allData) {
            thisOutputManager.println(lineOut<T>(thisData))
        }
        thisOutputManager.close()
    }

    fun stringRowFromTextLine(textLine: String): ArrayList<String> {
        val result = ArrayList<String>()
        val theseRows: Array<String> = textLine.split(("" + delimiter).toRegex()).toTypedArray()
        for (thisRow in theseRows) {
            result.add(thisRow.replace("" + delimiter, ""))
        }
        return result
    }

    fun dataRowFromTextLine(textLine: String, parser: Parser<T>): ArrayList<T> {
        val textRow = stringRowFromTextLine(textLine)
        val thisRow = ArrayList<T>(textRow.size)
        /*
		 * for (int cursor = 0; cursor < textRow.size(); cursor++) {
		 * thisRow.add((T) parser.parseMethod(textRow.get(cursor))); }
		 */
        for (thisTextRow in textRow) {
            thisRow.add(parser.parseMethod(thisTextRow) as T)
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

        @Throws(IOException::class)
        fun <T> fromFile(completeFilePath: String, parser: Parser<T>): DelimitedDataManager<T> {
            return fromFile(completeFilePath,
                    DEFAULT_DELIMITER, parser)
        }

        @Throws(IOException::class)
        fun <T> fromFile(completeFilePath: String, delimitingChar: Char, parser: Parser<T>): DelimitedDataManager<T> {
            val dataFromFile = DelimitedDataManager<T>()
            dataFromFile.delimiter = delimitingChar
            val fileStream: BufferedReader = openForReading(completeFilePath)
                    ?: throw IOException("Attempt to open $completeFilePath for reading produced a null BufferedReader.")

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