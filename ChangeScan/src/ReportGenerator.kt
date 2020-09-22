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
    var wasPrepared = false
    val report = Memoir("Change Report",
            null,
            PrintWriter(savePath),
            false,
            true,
            ::logHeader)

    fun prepare(targetData: FileSystemComparison) {
        // New to candidate
        targetData.newToCandidate.sort()
        val newToCandidateReport = Memoir("New Files", null, null, false)
        for (thisFile in targetData.newToCandidate) {
            newToCandidateReport.info(thisFile.fullyQualifiedPath, NEW_BULLET.toString())
        }
        report.showMemoir(newToCandidateReport, NEW_BULLET.toString(), "implied_good")

        // Removed from original
        targetData.removedInCandidate.sort()
        val missingFromOriginalReport = Memoir("Missing Files", null, null, false)
        for (thisFile in targetData.removedInCandidate) {
            missingFromOriginalReport.info(thisFile.fullyQualifiedPath, MISSING_BULLET.toString())
        }
        report.showMemoir(missingFromOriginalReport, MISSING_BULLET.toString(), "implied_bad")

        // Changes
        val sortedChangeData = targetData.fileSystemDifferences.keys.toTypedArray()
        sortedChangeData.sort()
        val changesReport = Memoir("Changes", null, null, false)
        sortedChangeData.forEach {
            val change = targetData.fileSystemDifferences[it]
            change?.let { thisChange ->
                changesReport.info("${thisChange.fullyQualifiedPath}<br><small>${thisChange.allDifferencesAsString}</small>")
            }
        }
        report.showMemoir(changesReport, MISSING_BULLET.toString(), "implied_caution")

        wasPrepared = true
    }

    fun conclude(log: Memoir) {
        report.showMemoir(log)
        report.conclude()
    }

    private fun logHeader(title: String): String {
        return "<table style=\"margin-left: 0; margin-right: 0\"><tr><td>\r\n\r\n$CHANGESCAN_LOGO\r\n\r\n</td><td><h1>$title</h1>\r\nPowered by ChangeScan</i></small></td></tr></table>\r\n<hr>\r\n\r\n"
    }
}