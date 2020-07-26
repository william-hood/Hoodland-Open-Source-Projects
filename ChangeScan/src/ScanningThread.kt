package hoodland.changescan.cmd

import hoodland.changescan.core.FilesystemChangeScanner
import hoodland.changescan.core.FilesystemChangeScannerConfiguration
import hoodland.changescan.core.ProgressEventListener


internal class ScanningThread(config: FilesystemChangeScannerConfiguration?, logSystem: LogSystem?, passedWorkingIndicator: Throbber?) : Thread(), ProgressEventListener {
    private var pauseCursor = 0
    private var thisScanner: FilesystemChangeScanner? = null
    private var workingIndicator: Throbber? = null
    fun showFirstFrame() {
        if (!ChangeScan.verbose) System.err.print(workingIndicator.firstFrame("Scanning"))
    }

    override fun ProgressOver() {
        if (!ChangeScan.verbose) System.err.println(Throbber.LASTFRAME)
    }

    override fun run() {
        thisScanner!!.PerformScan()
    }

    override fun ProgressHandler() {
        if (!ChangeScan.verbose) {
            if (pauseCursor > PAUSE_BETWEEN_FRAMES) {
                System.err.print(workingIndicator.nextFrame())
                pauseCursor = 0
            }
            pauseCursor++
        }
    }

    companion object {
        private const val PAUSE_BETWEEN_FRAMES = 33 //100;
    }

    init {
        thisScanner = HtmlReportingChangeScanner(config, logSystem)
        workingIndicator = passedWorkingIndicator
        thisScanner!!.addProgressEventListener(this)
    }
}
