package hoodland.changescan.core

import java.util.*


class FileComparison {
    var OriginalFile: FileDescription? = null
    var CandidateFile: FileDescription? = null

    private constructor() {
        // Default constructor prohibited.  Must provide original and candidate.
    }

    constructor(GivenOriginal: FileDescription?, GivenCandidate: FileDescription?) { // One of these is null :-S
        if (GivenOriginal == null) {
            throw FileDescriptionNullException("original")
        } else if (GivenCandidate == null) {
            throw FileDescriptionNullException("candidate")
        } else if (GivenOriginal.fullyQualifiedPath != GivenCandidate.fullyQualifiedPath) throw FileMismatchException()
        OriginalFile = GivenOriginal
        CandidateFile = GivenCandidate
    }

    val fullyQualifiedPath: String
        get() = OriginalFile!!.fullyQualifiedPath//if (OriginalFile.getAttributes().CompareTo(CandidateFile.getAttributes()) != 0) differences.add(DifferenceTypes.ATTRIBUTES_DIFFER);

    // Returns a list of the differences between the to files, determined on-the-fly.  Returns null if there is no difference at all.
    val differences: ArrayList<DifferenceTypes>?
        get() {
            val differences = ArrayList<DifferenceTypes>()
            if (CandidateFile!!.checksum !== OriginalFile!!.checksum) differences.add(DifferenceTypes.CHECKSUM_DIFFERS)
            //if (OriginalFile.getAttributes().CompareTo(CandidateFile.getAttributes()) != 0) differences.add(DifferenceTypes.ATTRIBUTES_DIFFER);
            if (CandidateFile!!.size!! > OriginalFile!!.size!!) differences.add(DifferenceTypes.CANDIDATE_LARGER)
            if (CandidateFile!!.size!! < OriginalFile!!.size!!) differences.add(DifferenceTypes.CANDIDATE_SMALLER)
            if (CandidateFile!!.creationTime.after(OriginalFile!!.creationTime)) differences.add(DifferenceTypes.CREATIONTIME_CANDIDATE_MORE_RECENT)
            if (CandidateFile!!.creationTime.before(OriginalFile!!.creationTime)) differences.add(DifferenceTypes.CREATIONTIME_ORIGINAL_MORE_RECENT)
            if (CandidateFile!!.lastAccessTime.after(OriginalFile!!.lastAccessTime)) differences.add(DifferenceTypes.LASTACCESS_CANDIDATE_MORE_RECENT)
            if (CandidateFile!!.lastAccessTime.before(OriginalFile!!.lastAccessTime)) differences.add(DifferenceTypes.LASTACCESS_ORIGINAL_MORE_RECENT)
            if (CandidateFile!!.lastWriteTime.after(OriginalFile!!.lastWriteTime)) differences.add(DifferenceTypes.LASTWRITE_CANDIDATE_MORE_RECENT)
            if (CandidateFile!!.lastWriteTime.before(OriginalFile!!.lastWriteTime)) differences.add(DifferenceTypes.LASTWRITE_ORIGINAL_MORE_RECENT)
            return if (differences.size == 0) null else differences
        }

    val allDifferencesAsString: String
        get() {
            val reportedDifferences = StringBuilder()
            for (thisDifference in differences!!) {
                if (reportedDifferences.length > 0) reportedDifferences.append(", ")
                reportedDifferences.append(getDifferenceAsString(thisDifference))
            }
            return reportedDifferences.toString()
        }

    companion object {
        fun getDifferenceAsString(thisDifference: DifferenceTypes?): String {
            return when (thisDifference) {
                DifferenceTypes.ATTRIBUTES_DIFFER -> "Attributes Differ"
                DifferenceTypes.CANDIDATE_LARGER -> "Increased in size"
                DifferenceTypes.CANDIDATE_SMALLER -> "Decreased in size"
                DifferenceTypes.CHECKSUM_DIFFERS -> "Checksum Differs"
                DifferenceTypes.CREATIONTIME_CANDIDATE_MORE_RECENT -> "Latest file created more recently"
                DifferenceTypes.CREATIONTIME_ORIGINAL_MORE_RECENT -> "Original file created more recently"
                DifferenceTypes.LASTACCESS_CANDIDATE_MORE_RECENT -> "Latest file accessed more recently"
                DifferenceTypes.LASTACCESS_ORIGINAL_MORE_RECENT -> "Original file accessed more recently"
                DifferenceTypes.LASTWRITE_CANDIDATE_MORE_RECENT -> "Latest file written to more recently"
                DifferenceTypes.LASTWRITE_ORIGINAL_MORE_RECENT -> "Original file written to more recently"
                else -> ""
            }
        }
    }
}
