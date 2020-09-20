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

import hoodland.opensource.toolbox.UNSET_STRING
import hoodland.opensource.toolbox.crc32ChecksumValue
import java.io.File
import java.io.Serializable
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.ZoneId

class FileDescription(fullPath: String) : Any(), Serializable, Comparable<FileDescription> {
    var fullyQualifiedPath: String = UNSET_STRING
    var checksum: Long? = null
    var size: Long? = null

    /* From the C# Version:  The FileAttributes enumeration...
	ReadOnly			The file is read-only.
	Hidden				The file is hidden, and thus is not included in an ordinary directory listing.
	System				The file is a system file. That is, the file is part of the operating system or is used exclusively by the operating system.
	Directory			The file is a directory.
	Archive				The file is a candidate for backup or removal.
	Device				Reserved for future use.
	Normal				The file is a standard file that has no special attributes. This attribute is valid only if it is used alone.
	Temporary			The file is temporary. A temporary file contains data that is needed while an application is executing but is not needed after the application is finished. File systems try to keep all the data in memory for quicker access rather than flushing the data back to mass storage. A temporary file should be deleted by the application as soon as it is no longer needed.
	SparseFile			The file is a sparse file. Sparse files are typically large files whose data consists of mostly zeros.
	ReparsePoint		The file contains a reparse point, which is a block of user-defined data associated with a file or a directory.
	Compressed			The file is compressed.
	Offline				The file is offline. The data of the file is not immediately available.
	NotContentIndexed	The file will not be indexed by the operating system's content indexing service.
	Encrypted			The file or directory is encrypted. For a file, this means that all data in the file is encrypted. For a directory, this means that encryption is the default for newly created files and directories.
	IntegrityStream		The file or directory includes data integrity support. When this value is applied to a file, all data streams in the file have integrity support. When this value is applied to a directory, all new files and subdirectories within that directory, by default, include integrity support.
	NoScrubData			The file or directory is excluded from the data integrity scan. When this value is applied to a directory, by default, all new files and subdirectories within that directory are excluded from data integrity.
	*/
    /*
	public FileAttributes getAttributes()
	{
		return attributes;
	}
	*/
    //FileAttributes attributes; // Intentionally uninitialized
    var creationTime: LocalDateTime? = null
    var lastAccessTime: LocalDateTime? = null
    var lastWriteTime: LocalDateTime? = null

    override fun compareTo(other: FileDescription): Int {
        return fullyQualifiedPath.compareTo(other.fullyQualifiedPath)
    }

    companion object {
        private const val serialVersionUID = -5057162263664139079L
    }

    init  //BasicFileAttributes info)
    {
        fullyQualifiedPath = fullPath
        var info: BasicFileAttributes? = null
        try {
            info = Files.readAttributes(Paths.get(fullyQualifiedPath), BasicFileAttributes::class.java)
        } catch (dontCare: Exception) {
            // Deliberate NO-OP
        }
        try {
            size = info!!.size()
        } catch (dontCare: Exception) {
            // Deliberate NO-OP
        }

        /*
		try
		{
			attributes = info.Attributes;
		}
		catch (Exception dontCare)
		{
			attributes = FileAttributes.Offline;
		}
		*/try {
        creationTime = LocalDateTime.ofInstant(info!!.creationTime().toInstant(), ZoneId.systemDefault())
    } catch (dontCare: Exception) {
        // Deliberate NO-OP
    }
        try {
            lastAccessTime = LocalDateTime.ofInstant(info!!.lastAccessTime().toInstant(), ZoneId.systemDefault())
        } catch (dontCare: Exception) {
            // Deliberate NO-OP
        }
        try {
            lastWriteTime = LocalDateTime.ofInstant(info!!.lastModifiedTime().toInstant(), ZoneId.systemDefault())
        } catch (dontCare: Exception) {
            // Deliberate NO-OP
        }
        try {
            // TODO: Keep this? Are there better options?
            checksum = File(fullyQualifiedPath).crc32ChecksumValue
        } catch (dontCare: Exception) {
            // Deliberate NO-OP
        }
    }
}
