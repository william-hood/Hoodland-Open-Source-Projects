// Copyright (c) 2020, 2023, 2025 William Arthur Hood
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
import java.nio.file.Files

private const val clobber = "The file's been clobbered!!! Bwa-ha-haaaaa!!!"
private const val fileContent = """The other day,
upon the stair,
I saw a man,
who wasn't there.
He wasn't there
again today.
Gee, I wish
he'd go away!"""

class TestQuantumTextFileFirstWrite : Test(
    "QuantumTextFile - First Write Test",
    "When a QuantumTextFile is created, the file should not exist before it is written, and should exist after the first write.",
    "Toolbox|QuantumTextFile",
    "TB-QT-01"
) {
    override fun performTest() {
        val fileName = "$artifactsDirectory${File.separatorChar}TBTO01.txt"
        val file = File(fileName)
        assert.shouldBeTrue(
            Files.notExists(file.toPath()),
            "The file ${file.name} should not exist at the beginning of the test"
        )
        log.info("Creating the QuantumTextFile")
        val candidate = QuantumTextFile(fileName, false)
        assert.shouldBeTrue(
            Files.notExists(file.toPath()),
            "The file should STILL not exist after creating the QuantumTextFile"
        )
        log.info("Flushing the newly created QuantumTextFile before anything is written")
        candidate.flush()
        assert.shouldBeTrue(
            Files.notExists(file.toPath()),
            "The file should also STILL not exist if the unused QuantumTextFile is flushed"
        )
        candidate.print(fileContent)
        candidate.flush()
        assert.shouldBeTrue(
            Files.exists(file.toPath()),
            "Now that the TextOutput manager has been written to and flushed, The file ${file.name} should exist"
        )
        log.info("Closing the QuantumTextFile")
        assert.shouldBeTrue(
            Files.exists(file.toPath()),
            "The file should still exist after the QuantumTextFile is closed"
        )

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")
    }
}

class TestQuantumTextFileClosedBeforeWrite : Test(
    "QuantumTextFile - Close Before Write",
    "When a QuantumTextFile is created, then closed before anything is written, no actual file on the disk should be created.",
    "Toolbox|QuantumTextFile",
    "TB-QT-02"
) {
    override fun performTest() {
        val fileName = "$artifactsDirectory${File.separatorChar}TBTO02.txt"
        val file = File(fileName)
        assert.shouldBeTrue(
            Files.notExists(file.toPath()),
            "The file ${file.name} should not exist at the beginning of the test"
        )
        log.info("Creating the QuantumTextFile")
        val candidate = QuantumTextFile(fileName, false)
        log.info("Closing the newly created QuantumTextFile before anything is written")
        candidate.close()
        assert.shouldBeTrue(
            Files.notExists(file.toPath()),
            "The file should STILL not exist if the unused QuantumTextFile is closed"
        )
    }
}

class TestQuantumTextFileAppendFalse : Test(
    "QuantumTextFile - Append Set to False",
    "Setting the 'append' parameter of the constructor to 'false' should clobber the file if it already exists.",
    "Toolbox|QuantumTextFile",
    "TB-QT-03"
) {
    override fun performTest() {
        val fileName = "$artifactsDirectory${File.separatorChar}TBTO03.txt"
        val file = File(fileName)
        log.info("Creating pre-existing file ${file.name}")
        file.writeText(fileContent)

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")


        log.info("Creating the QuantumTextFile with 'append' explicitly set to false")
        val candidate = QuantumTextFile(fileName, false)
        log.info("Clobbering the file with new contents")
        candidate.print(clobber)
        candidate.flush()
        candidate.close()

        val clobberedFileContents = file.readText()
        assert.shouldBeEqual(clobberedFileContents, clobber, "The file should now only have the new contents")
    }
}

class TestQuantumTextFileAppendDefault : Test(
    "QuantumTextFile - Append Left Default",
    "Not supplying the 'append' parameter should behave as if it was et to 'false'.",
    "Toolbox|QuantumTextFile",
    "TB-QT-04"
) {
    override fun performTest() {
        val fileName = "$artifactsDirectory${File.separatorChar}TBTO04.txt"
        val file = File(fileName)
        log.info("Creating pre-existing file ${file.name}")
        file.writeText(fileContent)

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")


        log.info("Creating the QuantumTextFile with 'append' not specified")
        val candidate = QuantumTextFile(fileName)
        log.info("Clobbering the file with new contents")
        candidate.print(clobber)
        candidate.flush()
        candidate.close()

        val clobberedFileContents = file.readText()
        assert.shouldBeEqual(clobberedFileContents, clobber, "The file should now only have the new contents")
    }
}

class TestQuantumTextFileAppendTrue : Test(
    "QuantumTextFile - Append Set to True",
    "Setting the 'append' parameter of the constructor to true should append the file if it already exists.",
    "Toolbox|QuantumTextFile",
    "TB-QT-05"
) {
    override fun performTest() {
        val fileName = "$artifactsDirectory${File.separatorChar}TBTO05.txt"
        val file = File(fileName)
        log.info("Creating pre-existing file ${file.name}")
        file.writeText(fileContent)

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")


        log.info("Creating the QuantumTextFile with 'append' explicitly set to true")
        val candidate = QuantumTextFile(fileName, true)
        log.info("Appending the file with new contents")
        candidate.print(clobber)
        candidate.flush()
        candidate.close()

        val appendedFileContents = file.readText()
        assert.shouldBeEqual(
            appendedFileContents,
            "$fileContent$clobber",
            "The file should now have the old contents immediately followed by the new contents"
        )
    }
}

class TestQuantumTextFileAppendNonexistant : Test(
    "QuantumTextFile - Append to Nonexistent File",
    "When a QuantumTextFile is created with append set to true, the file should be created as normal if it doesn't already exist.",
    "Toolbox|QuantumTextFile",
    "TB-QT-06"
) {
    override fun performTest() {
        val fileName = "$artifactsDirectory${File.separatorChar}TBTO06.txt"
        val file = File(fileName)
        assert.shouldBeTrue(
            Files.notExists(file.toPath()),
            "The file ${file.name} should not exist at the beginning of the test"
        )
        log.info("Creating the QuantumTextFile")
        val candidate = QuantumTextFile(fileName, true)
        candidate.print(fileContent)
        candidate.flush()
        assert.shouldBeTrue(
            Files.exists(file.toPath()),
            "Now that the TextOutput manager has been written to and flushed, The file ${file.name} should exist"
        )
        log.info("Closing the QuantumTextFile")

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")
    }
}