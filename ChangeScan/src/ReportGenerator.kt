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

import hoodland.opensource.memoir.Memoir
import java.io.PrintWriter

internal class ReportGenerator(val savePath: String) {
    // TODO: Moved Files must show the original location and the new one.
    // TODO: New icons for the side of each report.
    // TODO: Create icons to represent moved and timestamp changes
    // TODO: Change all timeStamp to timestamp. --'Tis one word.
    var reportFile: Memoir? = null

    fun prepare(targetData: FileSystemComparison) {
        reportFile = Memoir("Change Report",
                null,
                PrintWriter(savePath),
                false,
                true,
                ::logHeader)

        reportFile?.let { report ->
            // New
            if (targetData.newToCandidate.size > 0) {
                targetData.newToCandidate.sort()
                val newToCandidateReport = Memoir("${targetData.newToCandidate.size} New Files", null, null, false)

                for (thisFile in targetData.newToCandidate) {
                    newToCandidateReport.info(thisFile.fullyQualifiedPath, NEW_BULLET.toString())
                }

                if (newToCandidateReport.wasUsed) {
                    report.showMemoir(newToCandidateReport, NEW_HEADER.toString(), "implied_good")
                }
            }


            // Removed
            if (targetData.removedInCandidate.size > 0) {
                targetData.removedInCandidate.sort()
                val missingFromOriginalReport = Memoir("${targetData.removedInCandidate.size} Deleted Files", null, null, false)
                for (thisFile in targetData.removedInCandidate) {
                    missingFromOriginalReport.info(thisFile.fullyQualifiedPath, MISSING_BULLET.toString())
                }

                if (missingFromOriginalReport.wasUsed) {
                    report.showMemoir(missingFromOriginalReport, MISSING_HEADER.toString(), "implied_bad")
                }
            }


            // Moved
            if (targetData.movedInCandidate.size > 0) {
                targetData.movedInCandidate.sort()
                val movedInCandidateReport = Memoir("${targetData.movedInCandidate.size} Moved Files", null, null, false)

                for (thisFile in targetData.movedInCandidate) {
                    movedInCandidateReport.info(thisFile.fullyQualifiedPath, MISSING_BULLET.toString())
                }

                if (movedInCandidateReport.wasUsed) {
                    report.showMemoir(movedInCandidateReport, MISSING_HEADER.toString(), "implied_caution")
                }
            }



            // Content Changes
            if (targetData.contentDifferences.size > 0) {
                val sortedContentChangeData = targetData.contentDifferences.keys.toTypedArray()
                sortedContentChangeData.sort()
                val contentChangesReport = Memoir("${sortedContentChangeData.size} Files With Content Changes", null, null, false)
                sortedContentChangeData.forEach {
                    val change = targetData.contentDifferences[it]
                    change?.let { thisChange ->
                        contentChangesReport.info("${thisChange.fullyQualifiedPath}<br><small>${thisChange.allDifferencesAsString}</small>", CHANGED_BULLET.toString())
                    }
                }

                if (contentChangesReport.wasUsed) {
                    report.showMemoir(contentChangesReport, CHANGED_HEADER.toString(), "implied_caution")
                }
            }



            // Timestamp Changes
            if (targetData.timeStampDifferences.size > 0) {
                val sortedTimestampChangeData = targetData.timeStampDifferences.keys.toTypedArray()
                sortedTimestampChangeData.sort()
                val timestampChangesReport = Memoir("${sortedTimestampChangeData.size} Files With Timestamp Changes", null, null, false)
                sortedTimestampChangeData.forEach {
                    val change = targetData.timeStampDifferences[it]
                    change?.let { thisChange ->
                        timestampChangesReport.info("${thisChange.fullyQualifiedPath}<br><small>${thisChange.allDifferencesAsString}</small>", CHANGED_BULLET.toString())
                    }
                }

                if (timestampChangesReport.wasUsed)  {
                    report.showMemoir(timestampChangesReport, CHANGED_HEADER.toString(), "implied_caution")
                }
            }
        }
    }

    fun conclude(errorLog: Memoir) {
        reportFile?.let { report ->
            if (errorLog.wasUsed) {
                report.showMemoir(errorLog)
            }

            report.conclude()
        }
    }

    private fun logHeader(title: String): String {
        return "<table style=\"margin-left: 0; margin-right: 0\"><tr><td>\r\n\r\n$CHANGESCAN_LOGO\r\n\r\n</td><td><h1>$title</h1>\r\nPowered by ChangeScan</i></small></td></tr></table>\r\n<hr>\r\n\r\n"
    }
}