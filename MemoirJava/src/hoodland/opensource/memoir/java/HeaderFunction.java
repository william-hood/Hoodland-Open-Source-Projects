package hoodland.opensource.memoir.java;

public interface HeaderFunction {
    /**
     * Implement this to return HTML to implement an alternative HTML file header. The parameter is typically the title.
     * @param param1 - Typically the title that might go in big letters at the top of the page.
     * @return Returns HTML representing the title at the top of the file.
     */
    String displayHeader(String param1);

}
