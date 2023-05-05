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
