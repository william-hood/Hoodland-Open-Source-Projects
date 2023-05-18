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

/**
 * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
 */
public class QuantumTextFile {
    private hoodland.opensource.toolbox.QuantumTextFile KQuantumTextFile;

    /**
     * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
     *
     * @constructor
     *
     * @param filename The name of the file to write. No file will be created unless at least one call to print() or println() is made.
     * @param append Set this to true to append to an existing file.
     * @param outputStream Normally omit this. If you want to use QuantumTextFile to operate on a file (or any PrintWriter) you already have open, supply it here.
     */
    public QuantumTextFile(String filename, Boolean append, PrintWriter outputStream) {
        KQuantumTextFile = new hoodland.opensource.toolbox.QuantumTextFile(filename, append, outputStream);
    }

    /**
     * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
     *
     * @constructor
     *
     */
    public QuantumTextFile() {
        this(Symbols.UNSET_STRING, false, null);
    }

    /**
     * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
     *
     * @constructor
     *
     * @param filename The name of the file to write. No file will be created unless at least one call to print() or println() is made.
     */
    public QuantumTextFile(String filename) {
        this(filename, false, null);
    }

    /**
     * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
     *
     * @constructor
     *
     * @param append Set this to true to append to an existing file.
     */
    public QuantumTextFile(Boolean append) {
        this(Symbols.UNSET_STRING, append, null);
    }

    /**
     * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
     *
     * @constructor
     *
     * @param outputStream Normally omit this. If you want to use QuantumTextFile to operate on a file (or any PrintWriter) you already have open, supply it here.
     */
    public QuantumTextFile(PrintWriter outputStream) {
        this(Symbols.UNSET_STRING, false, outputStream);
    }

    /**
     * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
     *
     * @constructor
     *
     * @param filename The name of the file to write. No file will be created unless at least one call to print() or println() is made.
     * @param append Set this to true to append to an existing file.
     */
    public QuantumTextFile(String filename, Boolean append) {
        this(filename, append, null);
    }

    /**
     * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
     *
     * @constructor
     *
     * @param append Set this to true to append to an existing file.
     * @param outputStream Normally omit this. If you want to use QuantumTextFile to operate on a file (or any PrintWriter) you already have open, supply it here.
     */
    public QuantumTextFile(Boolean append, PrintWriter outputStream) {
        this(Symbols.UNSET_STRING, append, outputStream);
    }

    /**
     * QuantumTextFile: Prepares a text output file, but no file is actually created until and unless it is written to.
     *
     * @constructor
     *
     * @param filename The name of the file to write. No file will be created unless at least one call to print() or println() is made.
     * @param outputStream Normally omit this. If you want to use QuantumTextFile to operate on a file (or any PrintWriter) you already have open, supply it here.
     */
    public QuantumTextFile(String filename, PrintWriter outputStream) {
        this(filename, false, outputStream);
    }

    /**
     * println: Sends a string to the output stream followed by a carriage-return-line-feed sequence. (Assuming Windows-style CR/LF is deliberate because all platforms handle it well.)
     * Calling this function will create the output file.
     *
     * @param output The string to send to the output stream.
     */
    public void println(String output) {
        KQuantumTextFile.println(output);
    }

    /**
     * print: Sends a string to the output stream. Calling this function will create the output file.
     *
     * @param output The string to send to the output stream.
     */
    public void print(String output) {
        KQuantumTextFile.print(output);
    }

    /**
     * flush: This will flush the output stream if one exists (AKA if the file has been created) but will do nothing otherwise.
     *
     */
    public void flush() {
        KQuantumTextFile.flush();
    }

    /**
     * close: This will flush and then close the output stream if one exists (AKA if the file has been created).
     * It will also request garbage collection from the JVM, regardless of whether or not the file was created.
     *
     */
    public void close() {
        KQuantumTextFile.close();
    }
}
