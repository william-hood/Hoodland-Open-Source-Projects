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


class FileSystemComparison {
    // New To Candidate
    var NewToCandidate = ArrayList<FileDescription>()

    // Removed From Original
    var RemovedInCandidate = ArrayList<FileDescription>()

    // Different
    var FileSystemDifferences = HashMap<String, FileComparison>()

    // Default constructor prohibited.  Must provide something to compare against.
    private constructor() {}
    constructor(Original: FileSystemDescription, Candidate: FileSystemDescription, usedLogSystem: LogSystem) {
        // Given a description of an original and candidate file system, populate the categories of differences between the two.
        for (originalFileDescription in Original.fileDescriptions) {
            try {
                usedLogSystem.debugMessage("Comparing " + originalFileDescription.fullyQualifiedPath)
                val fileComparison = FileComparison(originalFileDescription, Candidate.pop(originalFileDescription.fullyQualifiedPath))
                if (fileComparison.differences != null) FileSystemDifferences[fileComparison.fullyQualifiedPath] = fileComparison
            } catch (nullException: FileDescriptionNullException) {
                if (nullException.message!!.contains("original")) {
                    usedLogSystem.showFailure(nullException)
                } else {
                    usedLogSystem.debugMessage("Counting as 'Removed in Candidate:'  " + originalFileDescription.fullyQualifiedPath)
                    RemovedInCandidate.add(originalFileDescription)
                }
            } catch (needToDetermineTypeAndCareAboutIt: Exception) {
                usedLogSystem.showFailure(needToDetermineTypeAndCareAboutIt)
            }
        }

        // At this point, only new files should remain in the Candidate
        for (newFileDescription in Candidate.fileDescriptions) {
            usedLogSystem.debugMessage("Counting as 'New to Candidate:'  " + newFileDescription.fullyQualifiedPath)
            NewToCandidate.add(newFileDescription)
        }
    }
}
