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

package rockabilly.toolbox

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.IOException
import java.util.zip.CRC32
import java.util.zip.CheckedInputStream

// From http://www.jguru.com/faq/view.jsp?EID=216274
class ChecksumFactory {
    @Throws(IOException::class)
    fun getCRC32(filePath: String?): Long {
        val file = FileInputStream(filePath)
        val check = CheckedInputStream(file, CRC32())
        val instream = BufferedInputStream(check)
        while (instream.read() != -1) {
            // Read file in completely
        }
        val result = check.checksum.value
        instream.close()
        return result
    }
}
