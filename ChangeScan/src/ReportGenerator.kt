// Copyright (c) 2020, 2023, 2025 William Arthur Hood
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

import hoodland.opensource.boolog.Boolog
import hoodland.opensource.boolog.THEME_CLASSIC
import java.io.PrintWriter

internal class ReportGenerator(val savePath: String) {
    // TODO: Test on Windows
    var reportFile: Boolog? = null

    fun prepare(targetData: FileSystemComparison) {
        reportFile = Boolog("Change Report",
                null,
                PrintWriter(savePath),
                false,
                true,
                THEME_CLASSIC,
                ::logHeader)

        reportFile?.let { report ->
            // New
            if (targetData.newToCandidate.size > 0) {
                targetData.newToCandidate.sort()
                val newToCandidateReport = Boolog("${targetData.newToCandidate.size} New", null, null, false)

                for (thisFile in targetData.newToCandidate) {
                    newToCandidateReport.info(thisFile.fullyQualifiedPath, "\uD83C\uDD95")
                }

                if (newToCandidateReport.wasUsed) {
                    report.showBoolog(newToCandidateReport, NEW_FILES_HEADER_ICON, "passing_test_result")
                }
            }


            // Removed
            if (targetData.removedInCandidate.size > 0) {
                targetData.removedInCandidate.sort()
                val missingFromOriginalReport = Boolog("${targetData.removedInCandidate.size} Missing", null, null, false)
                for (thisFile in targetData.removedInCandidate) {
                    missingFromOriginalReport.info(thisFile.fullyQualifiedPath, "\uD83D\uDEAB")
                }

                if (missingFromOriginalReport.wasUsed) {
                    report.showBoolog(missingFromOriginalReport, MISSING_FILES_HEADER_ICON, "failing_test_result")
                }
            }


            // Moved
            if (targetData.movedInCandidate.size > 0) {
                targetData.movedInCandidate.sort()
                val movedInCandidateReport = Boolog("${targetData.movedInCandidate.size} Moved", null, null, false)

                for (thisFile in targetData.movedInCandidate) {
                    movedInCandidateReport.info("${thisFile.fullyQualifiedPath}<br><small>&nbsp;&nbsp;&nbsp;• Previous location ${thisFile.formerDirectory}</small>", "\uD83D\uDCC2")
                }

                if (movedInCandidateReport.wasUsed) {
                    report.showBoolog(movedInCandidateReport, MOVED_FILES_HEADER_ICON, "plate")
                }
            }



            // Content Changes
            if (targetData.contentDifferences.size > 0) {
                val sortedContentChangeData = targetData.contentDifferences.keys.toTypedArray()
                sortedContentChangeData.sort()
                val contentChangesReport = Boolog("${sortedContentChangeData.size} Content Changes", null, null, false)
                sortedContentChangeData.forEach {
                    val change = targetData.contentDifferences[it]
                    change?.let { thisChange ->
                        contentChangesReport.info("${thisChange.fullyQualifiedPath}<br><small>${thisChange.allDifferencesAsString}</small>", "⚠️")
                    }
                }

                if (contentChangesReport.wasUsed) {
                    report.showBoolog(contentChangesReport, CONTENT_CHANGED_HEADER_ICON, "inconclusive_test_result")
                }
            }



            // Timestamp Changes
            if (targetData.timestampDifferences.size > 0) {
                val sortedTimestampChangeData = targetData.timestampDifferences.keys.toTypedArray()
                sortedTimestampChangeData.sort()
                val timestampChangesReport = Boolog("${sortedTimestampChangeData.size} Timestamp Changes", null, null, false)
                sortedTimestampChangeData.forEach {
                    val change = targetData.timestampDifferences[it]
                    change?.let { thisChange ->
                        timestampChangesReport.info("${thisChange.fullyQualifiedPath}<br><small>${thisChange.allDifferencesAsString}</small>", "\uD83D\uDD51")
                    }
                }

                if (timestampChangesReport.wasUsed)  {
                    report.showBoolog(timestampChangesReport, TIMESTAMP_CHANGED_HEADER_ICON, "implied_caution")
                }
            }
        }
    }

    fun conclude(errorLog: Boolog) {
        reportFile?.let { report ->
            if (errorLog.wasUsed) {
                report.showBoolog(errorLog, ERROR_LOG_HEADER_ICON, "highlighted")
            }

            report.conclude()
        }
    }

    private fun logHeader(title: String): String {
        return "<table style=\"margin-left: 0; margin-right: 0\"><tr><td>\r\n\r\n$CHANGESCAN_LOGO_ICON\r\n\r\n</td><td><h1>$title</h1>\r\nPowered by ChangeScan</i></small></td></tr></table>\r\n<hr>\r\n\r\n"
    }
}