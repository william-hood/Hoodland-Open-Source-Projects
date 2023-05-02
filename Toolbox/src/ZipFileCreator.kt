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

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

// Based on http://examples.javacodegeeks.com/core-java/util/zip/create-zip-file-from-multiple-files-with-zipoutputstream/
/**
 * ZipFileCreator: ...is exactly that. It creates a compressed Zip file containing all files in the directory you name, recursing down any subdirectories it finds.
 *
 */
object ZipFileCreator {
    val filesToAdd = ArrayList<String>()

    fun make(fullPathToRoot: String, fullPathToOutputFile: String) {
        var usedOutputFile = fullPathToOutputFile
        // Enforce .zip extension
        if (!fullPathToOutputFile.uppercase().endsWith(".ZIP")) usedOutputFile = "$fullPathToOutputFile.zip"

        // Get a complete list of all the files.
        filesToAdd.clear()
        recurse(fullPathToRoot)

        // Create the actual ZIP file
        val byteBuffer = ByteArray(1024)

        //forceParentDirectoryExistence(fullPathToOutputFile)
        // Force the parent directory to exist...
        File(usedOutputFile).parentFile.mkdirs()

        val zipOutput: ZipOutputStream = ZipOutputStream(FileOutputStream(usedOutputFile))
        for (thisFilePath in filesToAdd) {
            val reader = FileInputStream(File(thisFilePath))
            val zipPath = thisFilePath.substring(fullPathToRoot.length + 1)

            // Debug - Leave Commented Out
            //System.out.println(zipPath);
            zipOutput.putNextEntry(ZipEntry(zipPath))
            var length: Int
            while (reader.read(byteBuffer).also { length = it } > 0) {
                zipOutput.write(byteBuffer, 0, length)
            }
            zipOutput.closeEntry()
            reader.close()
        }
        zipOutput.close()
    }

    private fun recurse(fullPathToRoot: String) {
        val check = File(fullPathToRoot)
        if (check.isDirectory) {
            val contents = check.list()
            contents?.forEach {
                recurse("$fullPathToRoot${File.separator}$it")
            }
        } else {
            filesToAdd.add(fullPathToRoot)
        }
    }
}
