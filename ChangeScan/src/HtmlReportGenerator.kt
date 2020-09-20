package hoodland.opensource.changescan

import apple.laf.JRSUIConstants
import hoodland.changescan.core.ReportGenerator
import java.util.*

// TODO: Complete overhaul. Use some of the new tricks I've learned since 2015.
// TODO: Overhauled version is in ReportGenerator.kt. Delete this file when that's ready.
class HtmlReportGenerator {
    //private static final int LINE_ITEM_SIZE = 75;
    internal class HtmlReport private constructor() : WebInterface() {
        init {
            REPORT_TIME = Date().toString()
            setTitle("$REPORT_TITLE : $REPORT_TIME")
            controlsInOrder.add(RawCodeSegment("<center>"))
            controlsInOrder.add(ReportBanner())
            controlsInOrder.add(RawCodeSegment("</center>"))
            controlsInOrder.add(Divider())
        }
    }

    internal class ReportBanner : ConjoinedControls(Icon_ChangeScanLogo(), ConjoinedControls(Label("CHANGE REPORT", TITLE_SIZE), Label(REPORT_TIME, HEADER_SIZE), ConjoinedControls.Orientation.AlphaAbove), JRSUIConstants.Orientation.AlphaLeft)

    override fun Save(targetData: FileSystemComparison?, savePath: String?) {
        var thisTextWriter: TextOutputManager? = TextOutputManager(savePath)
        thisTextWriter.println(getHTML(targetData))
        thisTextWriter.close()
        thisTextWriter = null
    }

    companion object {
        private const val REPORT_TITLE = "CHANGE REPORT"
        private var REPORT_TIME: String = Values.DefaultString
        private const val TITLE_SIZE = 250
        private const val HEADER_SIZE = 150
        private fun getHTML(targetData: FileSystemComparison?): String {
            val report = HtmlReport()

            // New to Candidate
            report.controlsInOrder.add(CaptionedControl(Icon_NewHeader(), "New Files", HEADER_SIZE, JRSUIConstants.Orientation.LeftOfCaption))
            var NewToCandidateArrayList: ArrayList<String>? = ArrayList()
            for (thisFile in targetData!!.NewToCandidate) {
                NewToCandidateArrayList!!.add(thisFile.fullyQualifiedPath)
            }
            var NewToCandidate: Array<String>? = NewToCandidateArrayList!!.toTypedArray()
            Arrays.sort(NewToCandidate)
            for (thisFile in NewToCandidate!!) {
                //report.controlsInOrder.add(new CaptionedControl(new Icon_NewBullet(), thisFile, LINE_ITEM_SIZE, Orientation.LeftOfCaption));
                report.controlsInOrder.add(RawCodeSegment(Icon_NewBullet().toString() + " " + thisFile))
            }
            report.controlsInOrder.add(LineBreak())
            report.controlsInOrder.add(LineBreak())
            report.controlsInOrder.add(LineBreak())
            NewToCandidateArrayList = null
            NewToCandidate = null

            // Removed from Original
            report.controlsInOrder.add(CaptionedControl(Icon_MissingHeader(), "Missing Files", HEADER_SIZE, JRSUIConstants.Orientation.LeftOfCaption))
            var RemovedFromOriginalArrayList: ArrayList<String>? = ArrayList()
            for (thisFile in targetData.RemovedInCandidate) {
                RemovedFromOriginalArrayList!!.add(thisFile.fullyQualifiedPath)
            }
            var RemovedFromOriginal: Array<String>? = RemovedFromOriginalArrayList!!.toTypedArray()
            Arrays.sort(RemovedFromOriginal)
            for (thisFile in RemovedFromOriginal!!) {
                //report.controlsInOrder.add(new CaptionedControl(new Icon_MissingBullet(), thisFile, LINE_ITEM_SIZE, Orientation.LeftOfCaption));
                report.controlsInOrder.add(RawCodeSegment(Icon_MissingBullet().toString() + " " + thisFile))
            }
            report.controlsInOrder.add(LineBreak())
            report.controlsInOrder.add(LineBreak())
            report.controlsInOrder.add(LineBreak())
            RemovedFromOriginalArrayList = null
            RemovedFromOriginal = null

            // Changes
            report.controlsInOrder.add(CaptionedControl(Icon_ChangedHeader(), "Changes", HEADER_SIZE, JRSUIConstants.Orientation.LeftOfCaption))
            var ChangesArrayList: ArrayList<String?>? = ArrayList()
            for (thisDifference in targetData.FileSystemDifferences.values) {
                var tmp: StringBuilder? = StringBuilder()
                tmp!!.append(thisDifference.fullyQualifiedPath)
                tmp.append("<BR>")
                tmp.append("<SMALL>")
                tmp.append(thisDifference.allDifferencesAsString)
                tmp.append("</SMALL>")
                ChangesArrayList!!.add(tmp.toString())
                tmp = null
                thisDifference = null
            }
            var Changes: Array<String?>? = ChangesArrayList!!.toTypedArray()
            Arrays.sort(Changes)
            for (thisDifference in Changes!!) {
                report.controlsInOrder.add(CaptionedControl(Icon_ChangedBullet(), thisDifference, JRSUIConstants.Orientation.LeftOfCaption))
            }
            report.controlsInOrder.add(LineBreak())
            report.controlsInOrder.add(LineBreak())
            report.controlsInOrder.add(LineBreak())
            ChangesArrayList = null
            Changes = null
            return report.toString()
        }
    }
}
