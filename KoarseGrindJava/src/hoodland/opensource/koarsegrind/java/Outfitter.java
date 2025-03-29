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

package hoodland.opensource.koarsegrind.java;

import hoodland.opensource.boolog.java.Boolog;

/**
 * An Outfitter is used to provide a setup and cleanup for a whole category of tests. Leave the categoryPath field
 * as the default (empty string) value to assign it to the top-level category. IMPORTANT: Your outfitter must be a
 * CLASS, not an OBJECT in Kotlin. Also it must use a default constructor with no parameters.
 */
public abstract class Outfitter extends hoodland.opensource.koarsegrind.Outfitter {
    public Outfitter(String categoryPath) {
        super(categoryPath);
    }

    public Outfitter() {
        super(null);
    }

    public hoodland.opensource.boolog.java.Boolog log() {
        return new Boolog(getLog());
    }

    public hoodland.opensource.koarsegrind.Enforcer assertion() {
        return super.getAssert();
    }

    public hoodland.opensource.koarsegrind.Enforcer require() {
        return super.getRequire();
    }

    public hoodland.opensource.koarsegrind.Enforcer consider() {
        return super.getConsider();
    }
}
