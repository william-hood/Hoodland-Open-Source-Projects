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

import hoodland.opensource.toolbox.crc32ChecksumValue
import java.io.File
import java.io.Serializable
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.ZoneId

internal class FileDescription(fullyQualifiedPathAndFileName: String) : Any(), Serializable, Comparable<FileDescription> {
    val checksum: Long
    val size: Long
    val creationTime: LocalDateTime
    val lastAccessTime: LocalDateTime
    val lastWriteTime: LocalDateTime
    val directory: String
    val fileName: String

    init {
        fileName = fullyQualifiedPathAndFileName.substringAfterLast(File.separator)
        directory = fullyQualifiedPathAndFileName.substringBeforeLast(File.separator)
    }

    val fullyQualifiedPath: String
        get() = "$directory${File.separator}$fileName"

    init  //BasicFileAttributes info)
    {
        var info: BasicFileAttributes = Files.readAttributes(Paths.get(fullyQualifiedPath), BasicFileAttributes::class.java)
        size = info.size()
        creationTime = LocalDateTime.ofInstant(info.creationTime().toInstant(), ZoneId.systemDefault())
        lastAccessTime = LocalDateTime.ofInstant(info.lastAccessTime().toInstant(), ZoneId.systemDefault())
        lastWriteTime = LocalDateTime.ofInstant(info.lastModifiedTime().toInstant(), ZoneId.systemDefault())
        checksum = File(fullyQualifiedPath).crc32ChecksumValue
    }

    override fun compareTo(other: FileDescription): Int {
        return fullyQualifiedPath.compareTo(other.fullyQualifiedPath)
    }

    fun isTheSameFile(theOther: FileDescription): Boolean {
        return (this.fileName == theOther.fileName) && (this.checksum == theOther.checksum)
    }

    companion object {
        private const val serialVersionUID = -5057162263664139079L
    }
}
