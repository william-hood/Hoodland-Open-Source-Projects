// Copyright (c) 2023 William Arthur Hood
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

package hoodland.opensource.toolbox.java;

import java.io.PrintWriter;

public class QuantumTextFile {
    private hoodland.opensource.toolbox.QuantumTextFile KQuantumTextFile;

    public QuantumTextFile(String filename, Boolean append, PrintWriter outputStream) {
        KQuantumTextFile = new hoodland.opensource.toolbox.QuantumTextFile(filename, append, outputStream);
    }

    public QuantumTextFile() {
        this(Symbols.UNSET_STRING, false, null);
    }

    public QuantumTextFile(String filename) {
        this(filename, false, null);
    }

    public QuantumTextFile(Boolean append) {
        this(Symbols.UNSET_STRING, append, null);
    }

    public QuantumTextFile(PrintWriter outputStream) {
        this(Symbols.UNSET_STRING, false, outputStream);
    }

    public QuantumTextFile(String filename, Boolean append) {
        this(filename, append, null);
    }

    public QuantumTextFile(Boolean append, PrintWriter outputStream) {
        this(Symbols.UNSET_STRING, append, outputStream);
    }

    public QuantumTextFile(String filename, PrintWriter outputStream) {
        this(filename, false, outputStream);
    }

    public void println(String output) {
        KQuantumTextFile.println(output);
    }

    public void print(String output) {
        KQuantumTextFile.print(output);
    }

    public void flush() {
        KQuantumTextFile.flush();
    }

    public void close() {
        KQuantumTextFile.close();
    }
}
