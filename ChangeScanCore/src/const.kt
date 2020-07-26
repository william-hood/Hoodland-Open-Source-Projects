package hoodland.changescan.core

class FileDescriptionNullException(description: String) : Exception("The file description given as $description is null.") { }
class FileMismatchException() : Exception("The files given as original and candidate do not have the same path.") { }

enum class Categories {
    UNSET, Directory, File, Pattern
}

enum class DifferenceTypes {
    CHECKSUM_DIFFERS, CANDIDATE_LARGER, CANDIDATE_SMALLER, ATTRIBUTES_DIFFER, CREATIONTIME_CANDIDATE_MORE_RECENT, CREATIONTIME_ORIGINAL_MORE_RECENT, LASTACCESS_CANDIDATE_MORE_RECENT, LASTACCESS_ORIGINAL_MORE_RECENT, LASTWRITE_CANDIDATE_MORE_RECENT, LASTWRITE_ORIGINAL_MORE_RECENT
}

interface ProgressEventListener {
    fun ProgressHandler()
    fun ProgressOver()
}

interface ReportGenerator {
    fun Save(comparison: FileSystemComparison?, path: String?)
}