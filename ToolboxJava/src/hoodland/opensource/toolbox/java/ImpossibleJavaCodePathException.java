package hoodland.opensource.toolbox.java;

public class ImpossibleJavaCodePathException extends Exception {
    public ImpossibleJavaCodePathException() {
        super("A point in the code was reached that should not be possible to get to.");
    }
}
