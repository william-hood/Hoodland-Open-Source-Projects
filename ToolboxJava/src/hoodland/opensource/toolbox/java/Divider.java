// Copyright (c) 2023, 2025 William Arthur Hood
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

import hoodland.opensource.toolbox.DividerTypes;
import hoodland.opensource.toolbox.SymbolsKt;

/**
 * Used for creating plaintext dividers of various types and lengths.
 */
public class Divider {
    public static final int DEFAULT_DIVIDER_LENGTH = SymbolsKt.DEFAULT_DIVIDER_LENGTH;
    public static final hoodland.opensource.toolbox.DividerTypes DIVIDER_TYPE_SINGLE = DividerTypes.SINGLE;
    public static final hoodland.opensource.toolbox.DividerTypes DIVIDER_TYPE_DOUBLE = DividerTypes.DOUBLE;

    /**
     * Produces a plaintext horizontal divider of a single or double line to the specified length.
     * @param typeToUse DividerTypes.SINGLE or DividerTypes.DOUBLE
     * @param length Specify how many characters long the divider should be.
     * @return a plaintext string rendition of the divider.
     */
    public static String get(hoodland.opensource.toolbox.DividerTypes typeToUse, int length) {
        return SymbolsKt.divider(typeToUse, length);
    }

    /**
     * Produces a plaintext horizontal divider of a single or double line to the default length of 79.
     * @param typeToUse DividerTypes.SINGLE or DividerTypes.DOUBLE
     * @return a plaintext string rendition of the divider.
     */
    public static String get(hoodland.opensource.toolbox.DividerTypes typeToUse) {
        return get(typeToUse, DEFAULT_DIVIDER_LENGTH);
    }

    /**
     * Produces a plaintext horizontal single-line divider of the specified length.
     * @param length Specify how many characters long the divider should be.
     * @return a plaintext string rendition of the divider.
     */
    public static String get(int length) {
        return get (DIVIDER_TYPE_SINGLE, length);
    }

    /**
     * Produces a plaintext horizontal single-line divider of the default length of 79.
     * @return a plaintext string rendition of the divider.
     */
    public static String get() {
        return get(DIVIDER_TYPE_SINGLE, DEFAULT_DIVIDER_LENGTH);
    }
}
