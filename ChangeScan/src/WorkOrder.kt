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

import hoodland.opensource.memoir.Memoir
import hoodland.opensource.toolbox.*
import java.io.File
import java.util.*

const val DEFAULT_REPORT_FILE_NAME = "ChangeScan Report.html"

/**
 * WorkOrder represents the exact description of what the user has asked
 * ChangeScan to do, based on the command line arguments.
 */
internal class WorkOrder {
    var isScanlessComparison = false
    var startingDirectory = getUserHomeFolder()
    var snapshotSavePath = UNSET_STRING
    var snapshotComparisonPath = UNSET_STRING
    var reportPath = getCurrentWorkingDirectory() + File.separator + DEFAULT_REPORT_FILE_NAME
    val exclusions = ArrayList<FilesystemExclusion>()

    val saveRequested: Boolean
        get() = snapshotSavePath !== UNSET_STRING

    val comparisonRequested: Boolean
        get() = snapshotComparisonPath !== UNSET_STRING

    val reportRequested: Boolean
        get() {
            if (reportPath == UNSET_STRING) return false
            if (reportPath.trim() == ".html") return false
            if (reportPath.trim() == "") return false
            return true
        }

    /**
     * excludes()
     *
     * @param candidate The file/path in-question
     * @return True if the candidate file/path should be excluded from the scan
     */
    fun excludes(candidate: String): Boolean {
        for (thisExclusion in exclusions) {
            if (thisExclusion.Excludes(candidate)) return true
        }

        return false
    }

    fun describeTo(log: Memoir) {
        if (isScanlessComparison) {
            log.info("Comparing two baseline files. No scan is being performed.", "⚠️")
            log.info("• NEWER: $snapshotComparisonPath")
            log.info("• OLDER: $snapshotSavePath")
        } else {
            log.info("Scanning file system from $startingDirectory", "\uD83D\uDD0D")
            log.info("Saving the scan results as a baseline file: $snapshotSavePath")

            if (comparisonRequested) {
                log.info("Comparing scan result to $snapshotComparisonPath")
            }
        }

        if (reportRequested) {
            log.info("Report will be generated to $reportPath", "\uD83D\uDCC4")
        }
    }
}

private fun String.removeTrailingSeparator(): String {
    return this.trimEnd(File.separatorChar)
}

internal fun interpretArgs(args: Array<String>): WorkOrder {
    var sawUse = false
    var sawSave = false
    var sawCompare = false
    var sawReport = false
    var sawRoot = false
    val result = WorkOrder()
    result.exclusions.add(FilesystemExclusion(Categories.Pattern, "\$Recycle.Bin"))
    if (getOperatingSystemName().contains("Win")) result.startingDirectory = "C:\\"

    // Parse Command Line Arguments
    var index = 0
    while (index < args.size) {
        when (args[index].uppercase()) {
            "USE" -> {
                if (sawUse) {
                    System.out.println("⛔ The 'USE' argument was seen more than once.")
                    showUsage()
                }

                sawUse = true
                result.isScanlessComparison = true
                index++
                result.snapshotSavePath = args[index]
            }
            "SAVE" -> if (result.isScanlessComparison) {
                System.out.println("⛔ Can't save a scanned baseline when comparing one baseline to another.")
                showUsage()
            } else {
                if (sawSave) {
                    System.out.println("⛔ The 'SAVE' argument was seen more than once.")
                    showUsage()
                }

                sawSave = true
                index++
                result.snapshotSavePath = args[index]
            }
            "COMPARE" -> {
                if (sawCompare) {
                    System.out.println("⛔ The 'COMPARE' argument was seen more than once.")
                    showUsage()
                }

                sawCompare = true
                index++
                result.snapshotComparisonPath = args[index]
            }
            "REPORT" -> {
                if (sawReport) {
                    System.out.println("⛔ The 'REPORT' argument was seen more than once.")
                    showUsage()
                }

                sawReport = true
                index++
                result.reportPath = args[index]
                if (!result.reportPath.uppercase().endsWith(".HTML")) result.reportPath += ".html"
            }
            "ROOT" -> if (result.isScanlessComparison) {
                System.out.println("⛔ Can't accept a root directory for scanning when comparing one baseline to another.")
                showUsage()
            } else {
                if (sawRoot) {
                    System.out.println("⛔ The 'ROOT' argument was seen more than once.")
                    showUsage()
                }

                sawRoot = true
                index++
                result.startingDirectory = args[index].removeTrailingSeparator()
            }
            "EXCLUDE" -> {
                index++
                var whichCategory = when (args[index].uppercase()) {
                    "FILE" -> Categories.File
                    "DIRECTORY", "FOLDER" -> Categories.Directory
                    "PATTERN" -> Categories.Pattern
                    else -> Categories.UNSET
                }
                if (whichCategory == Categories.UNSET) {
                    System.out.println("⛔ EXCLUDE must be followed by FILE, PATTERN, DIRECTORY or FOLDER")
                    showUsage()
                }
                index++
                var markedExclude = args[index]

                if (whichCategory == Categories.Directory) {
                    markedExclude = markedExclude.removeTrailingSeparator()
                }

                result.exclusions.add(FilesystemExclusion(whichCategory, markedExclude))
            }
            else -> {
                System.out.println("⛔ Unrecognized command line argument: ${args[index]}")
                showUsage()
            }
        }

        index++
    }

    // Do not allow the end user to request a comparison without generating a report.
    if (result.comparisonRequested) {
        if (! result.reportRequested) {
            System.out.println("⛔ If you request any kind of comparison, you must also save a report.")
            showUsage()
        }
    }

    // Do not allow the end user to perform a baseline scan without saving it.
    if (! result.saveRequested) {
        if (! result.isScanlessComparison) {
            System.out.println("⛔ Can't perform a baseline scan without saving it.")
            showUsage()
        }
    }

    return result
}
