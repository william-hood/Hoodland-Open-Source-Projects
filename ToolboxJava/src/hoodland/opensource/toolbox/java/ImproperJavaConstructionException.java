package hoodland.opensource.toolbox.java;

public class ImproperJavaConstructionException extends Exception {
    public ImproperJavaConstructionException(String message) {
        super("An object or class was created in a way that precludes actual use: " + message);
    }
}
