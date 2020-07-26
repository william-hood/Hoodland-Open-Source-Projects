package hoodland.changescan.core

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
