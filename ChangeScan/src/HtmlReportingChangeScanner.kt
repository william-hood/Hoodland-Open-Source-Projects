package hoodland.changescan.cmd

import hoodland.changescan.core.ReportGenerator
import hoodland.changescan.core.FilesystemChangeScannerConfiguration
import java.util.ArrayList;

public class HtmlReportingChangeScanner extends FilesystemChangeScanner
{

    public HtmlReportingChangeScanner(
            FilesystemChangeScannerConfiguration newConfig,
            LogSystem newLogSystem)
    {
        super(newConfig, newLogSystem);
        reportGenerators.add(new HtmlReportGenerator());
    }

    private ArrayList<ReportGenerator> reportGenerators = new  ArrayList<ReportGenerator>();

    @Override
    public ArrayList<ReportGenerator> getReportGenerators()
    {
        return reportGenerators;
    }

}
