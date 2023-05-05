package hoodland.opensource.toolbox.java;

/**
 * ZipFileCreator: ...is exactly that. It creates a compressed Zip file containing all files in the directory you name, recursing down any subdirectories it finds.
 *
 */
public class ZipFileCreator {
    public static void make(String fullPathToRoot, String fullPathToOutputFile) {
        hoodland.opensource.toolbox.ZipFileCreator.INSTANCE.make(fullPathToRoot, fullPathToOutputFile);
    }
}
