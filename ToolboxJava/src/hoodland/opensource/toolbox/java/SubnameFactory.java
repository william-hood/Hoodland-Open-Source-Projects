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

/**
 * SubnameFactory is used in cases where a lot of records (or fields, test cases, etc.) need to have the same name,
 * with a unique suffix. MyTestA, MyTestB, etc. Use this to generate the suffix when programmatically generating
 * several similar items. This assumes a startingIndex and totalPlaces of zero.
 */
public class SubnameFactory {
    private hoodland.opensource.toolbox.SubnameFactory KSubnameFactory;

    /**
     * SubnameFactory is used in cases where a lot of records (or fields, test cases, etc.) need to have the same name,
     * with a unique suffix. MyTestA, MyTestB, etc. Use this to generate the suffix when programmatically generating
     * several similar items. This assumes a startingIndex and totalPlaces of zero.
     *
     * @constructor
     *
     */
    public SubnameFactory() {
        KSubnameFactory = new hoodland.opensource.toolbox.SubnameFactory();
    }

    /**
     * SubnameFactory is used in cases where a lot of records (or fields, test cases, etc.) need to have the same name,
     * with a unique suffix. MyTestA, MyTestB, etc. Use this to generate the suffix when programmatically generating
     * several similar items. This assumes a totalPlaces of zero.
     *
     * @constructor
     *
     * @param startingIndex Each subname is a Base26 number with a corresponding long integer. Zero is A. Unless you know why you'd want to change it, omit this parameter so it uses the default value of zero.
     */
    public SubnameFactory(int startingIndex) {
        KSubnameFactory = new hoodland.opensource.toolbox.SubnameFactory(startingIndex, 0);
    }

    /**
     * SubnameFactory is used in cases where a lot of records (or fields, test cases, etc.) need to have the same name,
     * with a unique suffix. MyTestA, MyTestB, etc. Use this to generate the suffix when programmatically generating
     * several similar items.
     *
     * @constructor
     *
     * @param startingIndex Each subname is a Base26 number with a corresponding long integer. Zero is A. Unless you know why you'd want to change it, omit this parameter so it uses the default value of zero.
     * @param totalPlaces When rendering the subname as a string, use this to force the subname to take a minimum of <totalPlaces> characters. The default placeholder is '.', but you can change it after construction.
     */
    public SubnameFactory(int startingIndex, int totalPlaces) {
        KSubnameFactory = new hoodland.opensource.toolbox.SubnameFactory(startingIndex, totalPlaces);
    }

    /**
     * getNextIndexAsString: This advances the index and produces the next Long-Integer index value as a string. (This is the numeric value, not the Base26 subname.)
     */
    public String getNextIndexAsString() {
        return KSubnameFactory.getNextIndexAsString();
    }

    /**
     * getCurrentIndexAsString: This produces the current Long-Integer index value as a string without advancing the index. (This is the numeric value, not the Base26 subname.)
     */
    public String getCurrentIndexAsString() {
        return KSubnameFactory.getCurrentIndexAsString();
    }

    /**
     * getNextIndex: This advances the index and produces the next Long-Integer index value. (This is the numeric value, not the Base26 subname.)
     */
    public int getNextIndex() {
        return KSubnameFactory.getNextIndex();
    }

    /**
     * advance: Advances the index but returns nothing. Use this to skip a subname if necessary.
     */
    public void advance() {
        KSubnameFactory.advance();
    }

    /**
     * getNextSubname: This advances the index and produces the next Base26 subname.
     */
    public String getNextSubname() {
        return KSubnameFactory.getNextSubname();
    }

    /**
     * currentSubname: This produces the current Base26 subname without advancing the index.
     */
    public String getCurrentSubname() {
        return KSubnameFactory.getCurrentSubname();
    }

    public int getCurrentIndex() {
        return KSubnameFactory.getCurrentIndex();
    }

    public int getPlaces() {
        return KSubnameFactory.getPlaces();
    }

    public void setPlaces(int totalPlaces) {
        KSubnameFactory.setPlaces(totalPlaces);
    }

    public char getPlaceHolder() {
        return KSubnameFactory.getPlaceholder();
    }

    public void setPlaceHolder(char placeholder) {
        KSubnameFactory.setPlaceholder(placeholder);
    }
}
