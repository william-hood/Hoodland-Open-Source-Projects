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

import hoodland.opensource.toolbox.COPYRIGHT
import hoodland.opensource.toolbox.justify

fun showUsage() {
    System.out.println("""Change Scan -  $COPYRIGHT 2020, 2023 William Arthur Hood
📜  MIT Licensed: java -jar ChangeScan.jar LICENSE
‼️👉    Examples: java -jar ChangeScan.jar EXAMPLES
Change Scan is a tool for seeing what changed on a file system as the
result of a software install, uninstall, or any other process that
might cause files to be created, changed, or deleted. YOU MAY NEED TO
RUN THIS PROGRAM WITH ADMINISTRAIVE PRIVILEDGES.

1. Create a baseline...
   java -jar ChangeScan.jar ROOT <root folder> SAVE <baseline filename>

2. Install, Uninstall, or otherwise cause changes...

3. Scan again, comparing against the baseline...
   java -jar ChangeScan.jar ROOT <root folder> COMPARE <baseline filename> REPORT <report filename>

* Instead of ROOT and SAVE, the switch USE provides a comparison
  between two already existing baselines.
* Report is HTML format.
* Exclude items using EXCLUDE <DIRECTORY|FILE|PATTERN> <excluded item>
* Use verbose output with VERBOSE""")

    System.exit(0)
}

const val LICENSE_PARAGRAPH_1 = "Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:"
const val LICENSE_PARAGRAPH_2 = "The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software."
const val LICENSE_PARAGRAPH_3 = "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE."
fun showLicense() {
    System.out.println("Change Scan -  $COPYRIGHT 2020, 2023 William Arthur Hood")
    System.out.println()
    System.out.println("MIT LICENSE")
    System.out.println()
    System.out.println(LICENSE_PARAGRAPH_1.justify())
    System.out.println()
    System.out.println(LICENSE_PARAGRAPH_2.justify())
    System.out.println()
    System.out.println(LICENSE_PARAGRAPH_3.justify())
    System.out.println()
    System.out.println("https://github.com/william-hood/Hoodland-Open-Source-Projects")
    System.exit(0)
}

fun showExamples() {
    System.out.println("""Create baseline or scan a 2nd time (use a different filename)
java -jar ChangeScan.jar ROOT <root folder> SAVE <baseline filename>

As above, but exclude directory
java -jar ChangeScan.jar ROOT <root> SAVE <filename> EXCLUDE DIRECTORY <fully qualified folder name>

As above, but exclude file
java -jar ChangeScan.jar ROOT <root> SAVE <filename> EXCLUDE FILE <fully qualified filename>

As above, but exclude everything with a substring
java -jar ChangeScan.jar ROOT <root> SAVE <filename> EXCLUDE PATTERN <substring>

As above, but with multiple exclusions (no limit)
java -jar ChangeScan.jar ROOT <root> SAVE <filename> EXCLUDE PATTERN <string> EXCLUDE FILE <filename> EXCLUDE DIRECTORY <name>

Scan and compare against the baseline (won't save this scan; you may add EXCLUDE as above)
java -jar ChangeScan.jar ROOT <root folder> COMPARE <baseline filename> REPORT <report filename>

DO NOT scan, just compare two baseline files
java -jar ChangeScan.jar USE <post change baseline>.fsc COMPARE <pre change baseline>.fsc

Note that 'EXCLUDE' is the only argument that may appear multiple times.
""")
    System.out.println()
    System.out.println("https://github.com/william-hood/Hoodland-Open-Source-Projects")
    System.exit(0)
}