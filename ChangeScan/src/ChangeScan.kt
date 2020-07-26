package hoodland.changescan.cmd

import hoodland.changescan.core.Categories
import hoodland.changescan.core.FilesystemChangeScannerConfiguration
import hoodland.changescan.core.FilesystemExclusion
import java.io.File
import java.io.PrintWriter
import java.util.*

// TODO: Memoir-ize
object ChangeScan {
    private const val LOG_FILE_NAME = "ChangeScan.log"
    private val workingIndicator: Throbber = Throbber(
            ThrobberStyles.Rider)
    private val osversion: String = Foundation.getOperatingSystemName()
    private var scanningThread: ScanningThread? = null
    private var startingFolder: String = Foundation.getUserHomeFolder()
    private var savePath: String = Values.DefaultString
    private var comparePath: String = Values.DefaultString
    private var reportPath: String = Values.DefaultString
    private var logPath: String = Foundation.getCurrentWorkingDirectory()
            .toString() + File.separator + LOG_FILE_NAME
    private val exclusions = ArrayList<FilesystemExclusion>()
    private var config: FilesystemChangeScannerConfiguration? = null
    private val logSystem: LogSystem = LogSystem()
    private var scanlessComparison = false
    var verbose = false
    fun showUsage() {
        // TO-DO: Show usage here
        logSystem.criticalMessage("1. Create a baseline...")
        logSystem
                .criticalMessage("java -jar ChangeScan.jar ROOT <root folder> SAVE <baseline filename>")
        logSystem.skipLine(Level.Critical)
        logSystem
                .criticalMessage("2. Install, Uninstall, or otherwise cause changes...")
        logSystem.skipLine(Level.Critical)
        logSystem
                .criticalMessage("3. Scan again, comparing against the baseline...")
        logSystem
                .criticalMessage("java -jar ChangeScan.jar ROOT <root folder> COMPARE <baseline filename> REPORT <report filename>")
        logSystem.skipLine(Level.Critical)
        logSystem.skipLine(Level.Critical)
        logSystem.criticalMessage("* Instead of ROOT and SAVE, the switch USE provides a comparison" + Symbols.NewLine.toString() + "  between two already existing baselines.")
        logSystem.criticalMessage("* Report is HTML format.")
        logSystem
                .criticalMessage("* Exclude items using EXCLUDE <DIRECTORY|FILE|PATTERN> <excluded item>")
        logSystem
                .criticalMessage("* Use verbose output with VERBOSE")
        logSystem
                .criticalMessage("* Add more log streams with LOG <log filename>")
        logSystem.skipLine(Level.Critical)
        System.exit(0)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        logSystem.SHOW_TIME_STAMPS = false
        if (args.size < 1) showUsage()
        try {
            val arrayExample = arrayOfNulls<FilesystemExclusion>(1)
            exclusions.add(FilesystemExclusion(Categories.Pattern,
                    "\$Recycle.Bin"))
            if (osversion.contains("Win")) startingFolder = "C:\\"

            // Parse Command Line Arguments
            var index = 0
            while (index < args.size) {
                when (args[index].toUpperCase()) {
                    "USE" -> {
                        scanlessComparison = true
                        index++
                        savePath = args[index]
                    }
                    "SAVE" -> if (scanlessComparison) {
                        showUsage()
                    } else {
                        index++
                        savePath = args[index]
                    }
                    "COMPARE" -> {
                        index++
                        comparePath = args[index]
                    }
                    "REPORT" -> {
                        index++
                        reportPath = args[index]
                        if (!reportPath.toUpperCase().endsWith(".HTML")) reportPath += ".html"
                    }
                    "VERBOSE" -> verbose = true
                    "LOG" -> {
                        index++
                        if (File(logPath).isDirectory) {
                            if (!logPath.endsWith(File.separator)) {
                                logPath += File.separator
                            }
                            logPath += args[index]
                        }
                        logSystem.addOutput(TextOutputManager(logPath),
                                LogSystem.Level.Standard, LogSystem.Mode.Minimum)
                    }
                    "ROOT" -> if (scanlessComparison) {
                        showUsage()
                    } else {
                        index++
                        startingFolder = args[index]
                    }
                    "EXCLUDE" -> {
                        index++
                        var whichCategory = Categories.Pattern
                        whichCategory = when (args[index]) {
                            "FILE" -> Categories.Pattern
                            "DIRECTORY", "FOLDER" -> Categories.Directory
                            else -> Categories.Pattern
                        }
                        index++
                        val markedExclude = args[index]
                        exclusions.add(FilesystemExclusion(whichCategory,
                                markedExclude))
                    }
                    else -> showUsage()
                }
                index++
            }
            logSystem.addOutput(TextOutputManager(LOG_FILE_NAME), LogSystem.Level.Standard, LogSystem.Mode.Minimum)
            if (verbose) {
                logSystem.addOutput(TextOutputManager(PrintWriter(System.err)), LogSystem.Level.Warning, LogSystem.Mode.Minimum)
            } else {
                logSystem.addOutput(TextOutputManager(PrintWriter(System.err)), LogSystem.Level.Critical, LogSystem.Mode.Minimum)
            }
            config = FilesystemChangeScannerConfiguration(startingFolder, scanlessComparison,
                    savePath, comparePath, reportPath,
                    *exclusions.toArray(arrayExample))
            // thisScanner = new FilesystemChangeScanner(config, logSystem);
            // thisScanner.Advance += ProgressHandler;
            if (scanlessComparison) {
                logSystem.criticalMessage("Beginning Baseline Comparison...")
                logSystem.criticalMessage("Assuming scan results from "
                        + config!!.snapshotSavePath)
            } else {
                logSystem.criticalMessage("Beginning file system scan...")
                logSystem.criticalMessage("Scanning file system from folder "
                        + config!!.startingDirectory)
                if (config!!.saveRequested) logSystem.criticalMessage("Saving baseline to "
                        + config!!.snapshotSavePath)
            }
            if (config!!.comparisonRequested) logSystem.criticalMessage("Comparing scan results to "
                    + config!!.snapshotComparisonPath)
            if (config!!.reportRequested) logSystem.criticalMessage("Report will be generated to "
                    + config!!.reportPath)
            for (thisExclusion in exclusions) {
                logSystem.criticalMessage("Excluding \""
                        + thisExclusion.toString() + "\"")
            }
            logSystem.skipLine()
            scanningThread = ScanningThread(config, logSystem, workingIndicator)
            scanningThread!!.showFirstFrame()
            scanningThread!!.run()

            // Deliberately block until the scan finishes.
            while (scanningThread!!.isAlive);
            // scanningThread.showLastFrame();
            scanningThread = null
            logSystem.criticalMessage("Program completed.")
        } catch (thisFailure: Throwable) {
            logSystem.showFailure(thisFailure)
            showUsage()
        }
    }
}
