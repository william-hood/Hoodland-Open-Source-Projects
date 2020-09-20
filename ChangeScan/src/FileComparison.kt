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

import java.util.*


class FileComparison(
        val originalFile: FileDescription,
        val candidateFile: FileDescription) {

    val fullyQualifiedPath: String
        get() = originalFile.fullyQualifiedPath//if (OriginalFile.getAttributes().CompareTo(CandidateFile.getAttributes()) != 0) differences.add(DifferenceTypes.ATTRIBUTES_DIFFER);

    // Returns a list of the differences between the two files, determined on-the-fly.  Returns null if there is no difference at all.
    val differences: ArrayList<DifferenceTypes>?
        get() {
            val differences = ArrayList<DifferenceTypes>()
            if (candidateFile.checksum !== originalFile.checksum) differences.add(DifferenceTypes.CHECKSUM_DIFFERS)
            //if (OriginalFile.getAttributes().CompareTo(CandidateFile.getAttributes()) != 0) differences.add(DifferenceTypes.ATTRIBUTES_DIFFER);


            // TODO: Is it really possible for the attributes below to be null?
            if (candidateFile.size!! > originalFile.size!!) differences.add(DifferenceTypes.CANDIDATE_LARGER)
            if (candidateFile.size!! < originalFile.size!!) differences.add(DifferenceTypes.CANDIDATE_SMALLER)
            if (candidateFile.creationTime.isAfter(originalFile.creationTime)) differences.add(DifferenceTypes.CREATIONTIME_CANDIDATE_MORE_RECENT)
            if (candidateFile.creationTime.isBefore(originalFile.creationTime)) differences.add(DifferenceTypes.CREATIONTIME_ORIGINAL_MORE_RECENT)
            if (candidateFile.lastAccessTime.isAfter(originalFile.lastAccessTime)) differences.add(DifferenceTypes.LASTACCESS_CANDIDATE_MORE_RECENT)
            if (candidateFile.lastAccessTime.isBefore(originalFile.lastAccessTime)) differences.add(DifferenceTypes.LASTACCESS_ORIGINAL_MORE_RECENT)
            if (candidateFile.lastWriteTime.isAfter(originalFile.lastWriteTime)) differences.add(DifferenceTypes.LASTWRITE_CANDIDATE_MORE_RECENT)
            if (candidateFile.lastWriteTime.isBefore(originalFile.lastWriteTime)) differences.add(DifferenceTypes.LASTWRITE_ORIGINAL_MORE_RECENT)
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
