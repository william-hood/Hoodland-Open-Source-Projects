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
import hoodland.opensource.memoir.showThrowable
import hoodland.opensource.toolbox.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

// TODO: File should not exist until first write.
// TODO: If File closed before write it does not exist.
// TODO: File will be appended to if append set to true.
// TODO: File will NOT be appended to if append set to false.

class TestTextOutputManagerFirstWrite:TextOutputManagerTest(
        "TextOutputManager - First Write Test",
        "When a TextOutputManager is created, the file should not exist before it is written, and should exist after the first write.",
        "TB-TO-01",
        "Toolbox", "TextOutputManager", "All"
) {

    override fun performTest() {
        val fileName = "$tmpFolder${File.separatorChar}TBTO01.txt"
        val file = File(fileName)
        assert.shouldBeTrue(Files.notExists(file.toPath()), "The file $fileName should not exist at the beginning of the test.")
        log.info("Creating the TextOutputManager")
        val candidate = TextOutputManager(fileName, false)
        assert.shouldBeTrue(Files.notExists(file.toPath()), "The file $fileName should STILL not exist after creating the TextOutputManager.")
        log.info("Flushing the newly created TextOutputManager before anything is written")
        candidate.flush()
        assert.shouldBeTrue(Files.notExists(file.toPath()), "The file $fileName should also STILL not exist if the unused TextOutputManager is flushed.")
        candidate.println("OK, the file can be written now.")
        candidate.flush()
        assert.shouldBeTrue(Files.exists(file.toPath()), "Now that the TextOutput manager has been written to and flushed, The file $fileName should exist.")
        log.info("Closing the TextOutputManager")
        assert.shouldBeTrue(Files.exists(file.toPath()), "The file $fileName should still exist after the TextOutputManager is closed.")

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