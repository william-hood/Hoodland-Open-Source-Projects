package hoodland.changescan.core

import java.io.*


class FileSystemDescription : Serializable {
    private val thisFileSystem: HashMap<String, FileDescription> = HashMap<String, FileDescription>()
    val fileDescriptions: Collection<Any>
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
        thisFileStream = null
        //using (FileStream thisFileStream = File.Create(rightedFilePath(filePath))) new BinaryFormatter().Serialize(thisFileStream, this);
    }

    operator fun get(FullyQualifiedPath: String?): FileDescription? {
        return thisFileSystem.get(FullyQualifiedPath)
    }

    fun pop(FullyQualifiedPath: String?): FileDescription? {
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