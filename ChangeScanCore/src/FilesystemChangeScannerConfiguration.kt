package hoodland.changescan.core

import hoodland.opensource.toolbox.UNSET_STRING
import java.util.*


/// <summary>
/// Will likely store in serialized Dictionaries.
/// 
/// Initial Scan.  Compare current scan to a saved one.
/// Compare two saved scans. (Generic compare two scans.)
/// </summary>
class FilesystemChangeScannerConfiguration(RootDirectory: String, ScanlessComparison: Boolean, SavePath: String, ComparisonPath: String, ReportingPath: String, vararg RequestedExclusions: FilesystemExclusion) {
    var isScanlessComparison = false
    var startingDirectory: String = UNSET_STRING
    var snapshotSavePath: String = UNSET_STRING
    var snapshotComparisonPath: String = UNSET_STRING
    var reportPath: String = UNSET_STRING
    private val exclusions = ArrayList<FilesystemExclusion>()

    val saveRequested: Boolean
        get() = snapshotSavePath !== UNSET_STRING

    val comparisonRequested: Boolean
        get() = snapshotComparisonPath !== UNSET_STRING

    val reportRequested: Boolean
        get() = reportPath !== UNSET_STRING

    fun Excludes(Candidate: String?): Boolean {
        for (thisExclusion in exclusions) {
            if (thisExclusion.Excludes(Candidate!!)) return true
        }
        return false
    }

    init {
        startingDirectory = RootDirectory
        isScanlessComparison = ScanlessComparison
        snapshotSavePath = SavePath
        snapshotComparisonPath = ComparisonPath
        reportPath = ReportingPath
        for (thisExclusion in RequestedExclusions) {
            exclusions.add(thisExclusion)
        }
    }
}
