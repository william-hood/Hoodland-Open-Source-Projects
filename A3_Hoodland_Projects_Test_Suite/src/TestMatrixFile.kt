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

package hoodland.opensource.testsuite

import hoodland.opensource.koarsegrind.Test
import hoodland.opensource.toolbox.*
import java.io.File

// Happy Path: Create a file read it back in.
class TestMatrixFile : Test(
    "MatrixFile - Happy Path",
    "Create a MatrixFile for Strings; Write it out; read it back in; make sure they're the same.",
    "Toolbox",
    "TB-MF-01"
) {
    override fun performTest() {
        val originalFileName = "$artifactsDirectory${File.separatorChar}original.csv"
        val copyFileName = "$artifactsDirectory${File.separatorChar}copy.csv"

        log.info("Creating the original MatrixFile")
        val original = MatrixFile<String>("First Column", "Second Column", "Third Column")
        original.addDataRow("One", "Two", "Three")
        original.addDataRow("A", "B", "C")
        original.addDataRow("Why", "Just", "Me")
        original.addDataRow("So", "You", "See")

        log.info("Writing out to $originalFileName")
        original.write(originalFileName)

        log.info("Reading into a new \"copier\" MatrixFile")
        val copy = MatrixFile.read(originalFileName, StringParser)

        val originalRow1 = original.getDataRow("First Column", "One")
        val copyRow1 = copy.getDataRow("First Column", "One")
        assert.shouldBeEqual(originalRow1, copyRow1, "Original's first row should be the same as the copy's")

        val originalRow2 = original.getDataRow("First Column", "A")
        val copyRow2 = copy.getDataRow("First Column", "A")
        assert.shouldBeEqual(originalRow2, copyRow2, "Original's second row should be the same as the copy's")

        val originalRow3 = original.getDataRow("First Column", "Why")
        val copyRow3 = copy.getDataRow("First Column", "Why")
        assert.shouldBeEqual(originalRow3, copyRow3, "Original's third row should be the same as the copy's")

        val originalRow4 = original.getDataRow("First Column", "So")
        val copyRow4 = copy.getDataRow("First Column", "So")
        assert.shouldBeEqual(originalRow4, copyRow4, "Original's fourth row should be the same as the copy's")

        log.showObject(original, "Original")
        log.showObject(copy, "Copy")

        log.info("Writing a copy file based on the read-in data")
        copy.write(copyFileName)

        assert.shouldBeEqual(
            File(originalFileName).readText(),
            File(copyFileName).readText(),
            "Read in contents of the original file and the copy file should be the same"
        )
        assert.shouldBeEqual(
            File(originalFileName).crc32ChecksumValue,
            File(copyFileName).crc32ChecksumValue,
            "CRC32 checksums of the original file and the copy file should be the same"
        )
    }
}