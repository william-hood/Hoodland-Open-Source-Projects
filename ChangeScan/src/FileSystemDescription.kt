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

package hoodland.opensource.changescan

import java.io.*


internal class FileSystemDescription : Serializable {
    private val thisFileSystem = HashMap<String, FileDescription>()

    val fileDescriptions: Collection<FileDescription>
        get() = thisFileSystem.values

    fun add(thisFileDescription: FileDescription) {
        thisFileSystem[thisFileDescription.fullyQualifiedPath] = thisFileDescription
    }

    @Throws(FileNotFoundException::class, IOException::class)
    fun save(filePath: String) {
        //Saves via binary serialization.
        var thisFileStream: ObjectOutputStream? = ObjectOutputStream(FileOutputStream(rightedFilePath(filePath)))
        thisFileStream!!.writeObject(this)
        thisFileStream.close()
        //using (FileStream thisFileStream = File.Create(rightedFilePath(filePath))) new BinaryFormatter().Serialize(thisFileStream, this);
    }

    operator fun get(FullyQualifiedPath: String): FileDescription? {
        return thisFileSystem.get(FullyQualifiedPath)
    }

    fun pop(FullyQualifiedPath: String): FileDescription? {
        val pop = thisFileSystem.get(FullyQualifiedPath)
        thisFileSystem.remove(FullyQualifiedPath)
        return pop
    }

    companion object {
        private const val EXTENSION = ".fsc"

        @Throws(FileNotFoundException::class, IOException::class, ClassNotFoundException::class)
        fun loadInstance(filePath: String): FileSystemDescription {
            //Loads via binary serialization.
            var thisFileStream: ObjectInputStream? = ObjectInputStream(FileInputStream(rightedFilePath(filePath)))
            val result = thisFileStream!!.readObject() as FileSystemDescription
            thisFileStream.close()
            thisFileStream = null
            return result
            //using (FileStream thisFileStream = File.OpenRead(rightedFilePath(filePath))) return (FileSystemDescription)(new BinaryFormatter().Deserialize(thisFileStream));
        }

        private fun rightedFilePath(filePath: String): String {
            var filePath = filePath
            if (!filePath.endsWith(EXTENSION)) filePath += EXTENSION
            return filePath
        }
    }
}