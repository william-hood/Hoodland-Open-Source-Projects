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

package rockabilly.toolbox

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


// Based on http://examples.javacodegeeks.com/core-java/util/zip/create-zip-file-from-multiple-files-with-zipoutputstream/
class ZipFileCreator {
    private var filesToAdd: ArrayList<String>? = null

    @Throws(IOException::class)
    fun make(fullPathToRoot: String, fullPathToOutputFile: String) {
        // Enforce .zip extension
        var fullPathToOutputFile = fullPathToOutputFile
        if (!fullPathToOutputFile.toUpperCase().endsWith(".ZIP")) fullPathToOutputFile = "$fullPathToOutputFile.zip"

        // Get a complete list of all the files.
        filesToAdd = ArrayList()
        recurse(fullPathToRoot)

        // Create the actual ZIP file
        val byteBuffer = ByteArray(1024)

        //forceParentDirectoryExistence(fullPathToOutputFile)
        // Force the parent directory to exist...
        File(fullPathToOutputFile).parentFile.mkdirs()

        var zipOutput: ZipOutputStream? = ZipOutputStream(FileOutputStream(fullPathToOutputFile))
        for (thisFilePath in filesToAdd!!) {
            var reader: FileInputStream? = FileInputStream(File(thisFilePath))
            var zipPath: String? = thisFilePath.substring(fullPathToRoot.length + 1)

            // Debug - Leave Commented Out
            //System.out.println(zipPath);
            zipOutput!!.putNextEntry(ZipEntry(zipPath))
            var length: Int
            while (reader!!.read(byteBuffer).also { length = it } > 0) {
                zipOutput.write(byteBuffer, 0, length)
            }
            zipOutput.closeEntry()
            reader.close()
            zipPath = null
            reader = null
        }
        zipOutput!!.close()
        zipOutput = null
    }

    private fun recurse(fullPathToRoot: String) {
        val check = File(fullPathToRoot)
        if (check.isDirectory) {
            val contents = check.list()
            for (thisFile in contents) {
                recurse(fullPathToRoot + File.separator + thisFile)
            }
        } else {
            filesToAdd!!.add(fullPathToRoot)
        }
    }
}
