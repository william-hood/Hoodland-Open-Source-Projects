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
import hoodland.opensource.toolbox.COPYRIGHT
import hoodland.opensource.toolbox.stdout

fun main(args: Array<String>) {
    if (args.size < 1) {
        //System.out.println("No arguments given.")
        showUsage()
    }

    if (args[0].toUpperCase() == "LICENSE") showLicense()

    val workOrder = interpretArgs(args)
    val errorLog = Memoir("Errors Encountered During Scanning")
    val activityLog = Memoir("ChangeScan $COPYRIGHT 2020 William Hood", stdout)
    val report = ReportGenerator(workOrder.reportPath)

    try {
        workOrder.describeTo(activityLog)
        ScanEngine.run(activityLog, errorLog, workOrder, report)
        activityLog.info("Program completed.")
    } catch (thisException: Throwable) {
        errorLog.showThrowable(thisException)
        activityLog.showThrowable(thisException)
    }

    report.conclude(errorLog)
}
