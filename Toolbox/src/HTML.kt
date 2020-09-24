// Copyright (c) 2020 William Arthur Hood
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

package hoodland.opensource.toolbox

/**
 * InlineImage: This is one of the remaining vestiges of deprecated code that programmatically represented HTML.
 * It represents an image that has been inserted directly into an HTML file as Base64 encoded data. A deprecated
 * alternative used to exist that put the image in the CSS style section, but was deprecated because some
 * browsers did not work with it.
 */
public class InlineImage(
        val base64ImageData: String,
        val imageType: String,
        val style: String? = null)
{
    public override fun toString(): String {
        val result = StringBuilder("<img src=\"")
        result.append("data:img/")
        result.append(imageType)
        result.append(";base64,")
        result.append(base64ImageData)
        result.append('"')

        style?.let {
            result.append(" style=\"$it\"")
        }

        result.append('>')
        return result.toString()
    }
}