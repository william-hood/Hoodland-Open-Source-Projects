package hoodland.changescan.core

import java.io.File
import java.util.*

// TODO: Redo this with Memoir as the logger
// public delegate void ScanProgress();

// public delegate void ScanProgress();
abstract class FilesystemChangeScanner {
    // Used for progress indicators to show that activity is taking place.
    private val progressSubscribers = ArrayList<ProgressEventListener>()
    private var encounteredAdminFault = false
    abstract val reportGenerators: ArrayList<Any>
    var config: FilesystemChangeScannerConfiguration? = null
    var logSystem: LogSystem? = null

    private constructor() {
        // Do not allow instantiation without configuration.
    }

    constructor(newConfig: FilesystemChangeScannerConfiguration?, newLogSystem: LogSystem?) {
        config = newConfig
        logSystem = newLogSystem
    }

    fun addProgressEventListener(newListener: ProgressEventListener) {
        progressSubscribers.add(newListener)
    }

    fun advanceScanProgress() {
        for (thisSubscriber in progressSubscribers) {
            thisSubscriber.ProgressHandler()
        }
    }

    fun concludeScanProgress() {
        for (thisSubscriber in progressSubscribers) {
            thisSubscriber.ProgressOver()
        }
    }

    fun PerformScan() {
        try {
            var scannedFileSystem: FileSystemDescription? = null
            if (config!!.isScanlessComparison) {
                logSystem.criticalMessage("Assuming scan data from " + config!!.snapshotSavePath)
                scannedFileSystem = FileSystemDescription.loadInstance(config!!.snapshotSavePath)
                logSystem.skipLine()
            } else {
                scannedFileSystem = FileSystemDescription()
                Scan(config!!.startingDirectory, scannedFileSystem)
                //scannedFileSystem.
                concludeScanProgress()
                logSystem.skipLine()

                // At this point we have completed the actual scan.  We now have to consider what to do with it.
                if (config!!.saveRequested) {
                    logSystem.criticalMessage("Saving scan data to " + config!!.snapshotSavePath)
                    scannedFileSystem.save(config!!.snapshotSavePath)
                }
            }
            if (config!!.comparisonRequested) {
                logSystem.criticalMessage("Comparing scan data to " + config!!.snapshotComparisonPath)
                val originalFileSystem = FileSystemDescription.loadInstance(config!!.snapshotComparisonPath)
                val comparison = FileSystemComparison(originalFileSystem, scannedFileSystem, logSystem)
                if (config!!.reportRequested) {
                    for (thisReport in reportGenerators) {
                        logSystem.criticalMessage("Reporting differences to " + config!!.reportPath)
                        thisReport.Save(comparison, config!!.reportPath)
                    }
                }
            }
        } catch (thisException: Exception) {
            logSystem.showFailure(thisException)
        }
        if (encounteredAdminFault) logSystem.criticalMessage(Symbols.NewLine + FX.exclaimIcon("YOU MAY NEED TO RE-RUN WITH ADMINISTRATIVE PRIVILEDGES!"))
    }

    private fun Scan(RootDirectory: String, thisFileSystem: FileSystemDescription) {
        try {
            if (config!!.Excludes(RootDirectory)) {
                // This folder is excluded.
                logSystem.log(FX.infoIcon("Excluding folder $RootDirectory"))
                return
            }
            if (File(RootDirectory).isDirectory) {
                logSystem.log(FX.addBullet(FxChr.ARROW_TIP, 1, RootDirectory))

                // Scan files first.
                val files = File(RootDirectory).list() //Will need to filter out directories//Directory.GetFiles(RootDirectory);
                if (files == null) {
                    encounteredAdminFault = true
                    logSystem.criticalMessage(Symbols.NewLine + FX.wordBox("!", 2, "YOU MAY NEED TO RE-RUN WITH ADMINISTRATIVE PRIVILEDGES!", "Denied access to folder $RootDirectory"))
                } else {
                    // First pass: prepend the directory
                    // Necessary unless there's a way to make File.list() prepend the path on each one.
                    for (cursor in files.indices) {
                        files[cursor] = RootDirectory + File.separator + files[cursor]
                    }
                    for (thisItem in files) {
                        if (File(thisItem).isFile) {
                            if (config!!.Excludes(thisItem)) {
                                // This file was excluded
                                logSystem.log(FX.infoIcon("Excluding file $thisItem"))
                            } else {
                                logSystem.log(FX.addBullet(3, thisItem))
                                try {
                                    val thisFilesDescription = FileDescription(thisItem)
                                    thisFileSystem.add(thisFilesDescription)
                                } catch (placeHolder: Exception) {
                                    logSystem.log(Symbols.NewLine + FX.exclaimIcon("Unable to Scan :  $thisItem"))
                                }
                                /*
								catch (Exception needToDetermineTypeAndCareAboutIt)
								{
									logSystem.log(Symbols.NewLine + BoxFactory.getExclaimIcon("Declining long or illegal path:  " + thisFile));
								}
								catch (Exception needToDetermineTypeAndCareAboutIt)
								{
									logSystem.log(Symbols.NewLine + BoxFactory.getExclaimIcon("Declining alleged duplicate:  " + thisFile));
								}
								*/
                            }
                            advanceScanProgress()
                        }
                    }

                    // Scan directories next.
                    val directories = File(RootDirectory).listFiles() //Directory.GetDirectories(RootDirectory);
                    for (thisItem in directories) {
                        if (thisItem.isDirectory) {
                            if (config!!.Excludes(thisItem.toString())) {
                            } else {
                                Scan(thisItem.toString(), thisFileSystem)
                                advanceScanProgress()
                            }
                        }
                    }
                }
            } else {
                // This folder did not exist.
                logSystem.log(Symbols.NewLine + FX.exclaimIcon("Declining non-existent folder $RootDirectory"))
            }
        } catch (needToDetermineTypeAndCareAboutIt: Exception) {
            // Alert that this folder is being ignored.
            logSystem.log(Symbols.NewLine + FX.exclaimIcon("Exception trying to process folder $RootDirectory"))
            logSystem.showFailure(needToDetermineTypeAndCareAboutIt)
        }
        return
    }
}