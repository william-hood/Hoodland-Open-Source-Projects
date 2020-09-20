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
import hoodland.opensource.memoir.showThrowable
import java.io.File

internal object ScanEngine {
    private var encounteredAdminFault = false

    fun run(log: Memoir, workOrder: WorkOrder) {
        try {
            var scannedFileSystem = FileSystemDescription()
            if (workOrder.isScanlessComparison) {
                log.info("Loading baseline file ${workOrder.snapshotSavePath} into memory and treating as the scan for comparison.")
                scannedFileSystem = FileSystemDescription.loadInstance(workOrder.snapshotSavePath)
            } else {
                scannedFileSystem = FileSystemDescription()
                scan(log, workOrder, workOrder.startingDirectory, scannedFileSystem)

                // At this point we have completed the actual scan.  We now have to consider what to do with it.
                if (workOrder.saveRequested) {
                    log.info("Saving scan data to baseline file ${workOrder.snapshotSavePath}")
                    scannedFileSystem.save(workOrder.snapshotSavePath)
                }
            }

            if (workOrder.comparisonRequested) {
                log.info("Comparing scan data to baseline file ${workOrder.snapshotComparisonPath}")
                val originalFileSystem = FileSystemDescription.loadInstance(workOrder.snapshotComparisonPath)

                val comparison = FileSystemComparison(originalFileSystem, scannedFileSystem, logSystem)

                if (workOrder.reportRequested) {
                    log.info("Generating report.", "\uD83D\uDCBE")
                    thisReport.Save(comparison, config!!.reportPath)
                }
            }
        } catch (thisException: Exception) {
            log.showThrowable(thisException)
        }

        if (encounteredAdminFault) log.error("YOU MAY NEED TO RE-RUN WITH ADMINISTRATIVE PRIVILEDGES!")
    }

    private fun scan(log: Memoir, workOrder: WorkOrder, rootDirectory: String, thisFileSystem: FileSystemDescription) {
        try {
            if (workOrder.excludes(rootDirectory)) {
                // This folder is excluded.
                log.info("Excluding folder $rootDirectory", "⛔️")
                return
            }

            if (File(rootDirectory).isDirectory) {
                log.info("Scanning $rootDirectory", "\uD83D\uDCC2")

                // Scan files first.
                val files = File(rootDirectory).list() //Will need to filter out directories//Directory.GetFiles(RootDirectory);
                if (files == null) {
                    encounteredAdminFault = true
                    log.error("Denied access to folder $rootDirectory. You may need to re-run the scan with admin priviledges.")
                } else {
                    // First Pass: Scan the files, save directories for the next pass...
                    files.forEach {
                        val thisItem = "$rootDirectory${File.separator}$it"

                        if (File(thisItem).isFile) {
                            if (workOrder.excludes(thisItem)) {
                                // This file was excluded
                                log.info("Excluding file $thisItem", "\uD83D\uDEAB")
                            } else {
                                log.info(thisItem, "\uD83D\uDD0E")

                                try {
                                    val thisFilesDescription = FileDescription(thisItem)
                                    thisFileSystem.add(thisFilesDescription)
                                } catch (thisException: Throwable) {
                                    log.error("Unable to Scan :  $thisItem")
                                    log.showThrowable(thisException)
                                }
                            }
                        }
                    }

                    // Second Pass: Scan directories next.
                    val directories = File(rootDirectory).listFiles() //Directory.GetDirectories(RootDirectory);
                    directories?.forEach {
                        if (it.isDirectory) {
                            if (! workOrder.excludes(it.toString())) {
                                scan(log, workOrder, it.toString(), thisFileSystem)
                            }
                        }
                    }
                }
            } else {
                // This folder did not exist.
                log.error("Declining non-existent folder $rootDirectory")
            }
        } catch (thisException: Throwable) {
            // Alert that this folder is being ignored.
            log.error("Unable to process folder $rootDirectory")
            log.showThrowable(thisException)
        }

        return
    }
}