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

import hoodland.opensource.koarsegrind.Test
import hoodland.opensource.toolbox.*
import java.io.File
import java.nio.file.Files

// TODO: File will be appended to if append set to true.

private const val clobber = "The file's been clobbered!!! Bwa-ha-haaaaa!!!"
private const val fileContent = """The other day,
upon the stair,
I saw a man,
who wasn't there.
He wasn't there
again today.
Gee, I wish
he'd go away!"""

class TestTextOutputManagerFirstWrite:TextOutputManagerTest(
        "TextOutputManager - First Write Test",
        "When a TextOutputManager is created, the file should not exist before it is written, and should exist after the first write.",
        "TB-TO-01",
        "Toolbox", "TextOutputManager", "All"
) {
    override fun performTest() {
        val fileName = "$tmpFolder${File.separatorChar}TBTO01.txt"
        val file = File(fileName)
        assert.shouldBeTrue(Files.notExists(file.toPath()), "The file ${file.name} should not exist at the beginning of the test")
        log.info("Creating the TextOutputManager")
        val candidate = TextOutputManager(fileName, false)
        assert.shouldBeTrue(Files.notExists(file.toPath()), "The file should STILL not exist after creating the TextOutputManager")
        log.info("Flushing the newly created TextOutputManager before anything is written")
        candidate.flush()
        assert.shouldBeTrue(Files.notExists(file.toPath()), "The file should also STILL not exist if the unused TextOutputManager is flushed")
        candidate.print(fileContent)
        candidate.flush()
        assert.shouldBeTrue(Files.exists(file.toPath()), "Now that the TextOutput manager has been written to and flushed, The file ${file.name} should exist")
        log.info("Closing the TextOutputManager")
        assert.shouldBeTrue(Files.exists(file.toPath()), "The file should still exist after the TextOutputManager is closed")

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")
    }
}

class TestTextOutputManagerClosedBeforeWrite:TextOutputManagerTest(
        "TextOutputManager - Close Before Write",
        "When a TextOutputManager is created, then closed before anything is written, no actual file on the disk should be created.",
        "TB-TO-02",
        "Toolbox", "TextOutputManager", "All"
) {
    override fun performTest() {
        val fileName = "$tmpFolder${File.separatorChar}TBTO02.txt"
        val file = File(fileName)
        assert.shouldBeTrue(Files.notExists(file.toPath()), "The file ${file.name} should not exist at the beginning of the test")
        log.info("Creating the TextOutputManager")
        val candidate = TextOutputManager(fileName, false)
        log.info("Closing the newly created TextOutputManager before anything is written")
        candidate.close()
        assert.shouldBeTrue(Files.notExists(file.toPath()), "The file should STILL not exist if the unused TextOutputManager is closed")
    }
}

class TestTextOutputManagerAppendFalse:TextOutputManagerTest(
        "TextOutputManager - Append Set to False",
        "Setting the 'append' parameter of the constructor to 'false' should clobber the file if it already exists.",
        "TB-TO-03",
        "Toolbox", "TextOutputManager", "All"
) {
    override fun performTest() {
        val fileName = "$tmpFolder${File.separatorChar}TBTO03.txt"
        val file = File(fileName)
        log.info("Creating pre-existing file ${file.name}")
        file.writeText(fileContent)

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")


        log.info("Creating the TextOutputManager with 'append' explicitly set to false")
        val candidate = TextOutputManager(fileName, false)
        log.info("Clobbering the file with new contents")
        candidate.print(clobber)
        candidate.flush()
        candidate.close()

        val clobberedFileContents = file.readText()
        assert.shouldBeEqual(clobberedFileContents, clobber, "The file should now only have the new contents")
    }
}

class TestTextOutputManagerAppendDefault:TextOutputManagerTest(
        "TextOutputManager - Append Left Default",
        "Not supplying the 'append' parameter should behave as if it was et to 'false'.",
        "TB-TO-04",
        "Toolbox", "TextOutputManager", "All"
) {
    override fun performTest() {
        val fileName = "$tmpFolder${File.separatorChar}TBTO04.txt"
        val file = File(fileName)
        log.info("Creating pre-existing file ${file.name}")
        file.writeText(fileContent)

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")


        log.info("Creating the TextOutputManager with 'append' not specified")
        val candidate = TextOutputManager(fileName)
        log.info("Clobbering the file with new contents")
        candidate.print(clobber)
        candidate.flush()
        candidate.close()

        val clobberedFileContents = file.readText()
        assert.shouldBeEqual(clobberedFileContents, clobber, "The file should now only have the new contents")
    }
}

class TestTextOutputManagerAppendTrue:TextOutputManagerTest(
        "TextOutputManager - Append Set to True",
        "Setting the 'append' parameter of the constructor to true should append the file if it already exists.",
        "TB-TO-05",
        "Toolbox", "TextOutputManager", "All"
) {
    override fun performTest() {
        val fileName = "$tmpFolder${File.separatorChar}TBTO05.txt"
        val file = File(fileName)
        log.info("Creating pre-existing file ${file.name}")
        file.writeText(fileContent)

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")


        log.info("Creating the TextOutputManager with 'append' explicitly set to true")
        val candidate = TextOutputManager(fileName, true)
        log.info("Appending the file with new contents")
        candidate.print(clobber)
        candidate.flush()
        candidate.close()

        val appendedFileContents = file.readText()
        assert.shouldBeEqual(appendedFileContents, "$fileContent$clobber", "The file should now have the old contents immediately followed by the new contents")
    }
}

class TestTextOutputManagerAppendNonexistant:TextOutputManagerTest(
        "TextOutputManager - Append to Nonexistent File",
        "When a TextOutputManager is created with append set to true, the file should be created as normal if it doesn't already exist.",
        "TB-TO-06",
        "Toolbox", "TextOutputManager", "All"
) {
    override fun performTest() {
        val fileName = "$tmpFolder${File.separatorChar}TBTO06.txt"
        val file = File(fileName)
        assert.shouldBeTrue(Files.notExists(file.toPath()), "The file ${file.name} should not exist at the beginning of the test")
        log.info("Creating the TextOutputManager")
        val candidate = TextOutputManager(fileName, true)
        candidate.print(fileContent)
        candidate.flush()
        assert.shouldBeTrue(Files.exists(file.toPath()), "Now that the TextOutput manager has been written to and flushed, The file ${file.name} should exist")
        log.info("Closing the TextOutputManager")

        val createdFileContents = file.readText()
        assert.shouldBeEqual(createdFileContents, fileContent, "The newly created file's content should be as expected")
    }
}

abstract class TextOutputManagerTest(name: String, detailedDescription: String, testCaseID: String, vararg categories: String): Test(name, detailedDescription, testCaseID, *categories) {
    protected var tmpFolder = ""
    private var tmpFolderFile: File? = null

    override fun setup() {
        log.info("Creating temporary folder $tmpFolder")
        tmpFolder = artifactsDirectory + File.separatorChar + "(this is a test artifact)"
        tmpFolderFile = File(tmpFolder)
        tmpFolderFile!!.mkdir()
        assert.shouldBeTrue(Files.exists(tmpFolderFile!!.toPath()), "Before setup() finishes, the folder $tmpFolder should be confirmed to exist.")
    }

    /*
    override fun cleanup() {
            log.info("Deleting temporary folder $tmpFolder and all its contents")
            File(tmpFolder).deleteRecursively()
        assert.shouldBeTrue(Files.notExists(tmpFolderFile.toPath()), "Temporary folder $tmpFolder should NOT exist after cleaning up")
    }
     */
}