package hoodland.opensource.toolbox.java;

public class InlineImage {
    private hoodland.opensource.toolbox.InlineImage KInlineImage;

    public InlineImage(String base64ImageData, String imageType, String style) {
        KInlineImage = new hoodland.opensource.toolbox.InlineImage(base64ImageData, imageType, style);
    }

    public InlineImage(String base64ImageData, String imageType) {
        this(base64ImageData, imageType, null);
    }

    @Override
    public String toString() {
        return KInlineImage.toString();
    }
}
