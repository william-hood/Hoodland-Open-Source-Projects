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

package hoodland.opensource.changescan

import java.util.*


internal class FileComparison(
        val originalFile: FileDescription,
        val candidateFile: FileDescription) {

    val fullyQualifiedPath: String
        get() = originalFile.fullyQualifiedPath

    // Returns a list of the differences between the two files, determined on-the-fly.  The list is empty if there's no difference at all.
    val differences: ArrayList<DifferenceTypes>
        get() {
            val differences = ArrayList<DifferenceTypes>()
            if (candidateFile.checksum != originalFile.checksum) differences.add(DifferenceTypes.CHECKSUM_DIFFERS)
            if (candidateFile.size > originalFile.size) differences.add(DifferenceTypes.CANDIDATE_LARGER)
            if (candidateFile.size < originalFile.size) differences.add(DifferenceTypes.CANDIDATE_SMALLER)
            if (candidateFile.creationTime.isAfter(originalFile.creationTime)) differences.add(DifferenceTypes.CREATIONTIME_CANDIDATE_MORE_RECENT)
            if (candidateFile.creationTime.isBefore(originalFile.creationTime)) differences.add(DifferenceTypes.CREATIONTIME_ORIGINAL_MORE_RECENT)
            if (candidateFile.lastAccessTime.isAfter(originalFile.lastAccessTime)) differences.add(DifferenceTypes.LASTACCESS_CANDIDATE_MORE_RECENT)
            if (candidateFile.lastAccessTime.isBefore(originalFile.lastAccessTime)) differences.add(DifferenceTypes.LASTACCESS_ORIGINAL_MORE_RECENT)
            if (candidateFile.lastWriteTime.isAfter(originalFile.lastWriteTime)) differences.add(DifferenceTypes.LASTWRITE_CANDIDATE_MORE_RECENT)
            if (candidateFile.lastWriteTime.isBefore(originalFile.lastWriteTime)) differences.add(DifferenceTypes.LASTWRITE_ORIGINAL_MORE_RECENT)
            return differences
        }

    val allDifferencesAsString: String
        get() {
            val reportedDifferences = StringBuilder()
            for (thisDifference in differences) {
                if (reportedDifferences.length > 0) reportedDifferences.append(", ")
                reportedDifferences.append(thisDifference.toString())
            }

            if (reportedDifferences.length < 1) return "(no difference)"
            return reportedDifferences.toString()
        }

    val contentWasChanged: Boolean
        get() = differences.contains(DifferenceTypes.CHECKSUM_DIFFERS) || differences.contains(DifferenceTypes.CANDIDATE_LARGER) || differences.contains(DifferenceTypes.CANDIDATE_SMALLER)
    }