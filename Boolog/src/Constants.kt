// Copyright (c) 2020, 2023, 2025 William Arthur Hood
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

package hoodland.opensource.boolog

const val EMOJI_SETUP = "🛠"
const val EMOJI_CLEANUP = "🧹"
const val EMOJI_PASSING_TEST = "✅"
const val EMOJI_SUBJECTIVE_TEST = "🤔"
const val EMOJI_INCONCLUSIVE_TEST = "🛑"
const val EMOJI_FAILING_TEST = "❌"
const val EMOJI_DEBUG = "🐞"
const val EMOJI_ERROR = "😱"
const val EMOJI_BOOLOG = "📝"
const val EMOJI_TEXT_BOOLOG_CONCLUDE = "⤴️"
const val EMOJI_TEXT_BLANK_LINE = ""
const val EMOJI_OBJECT = "🔲"
const val EMOJI_CAUSED_BY = "→"
const val EMOJI_OUTGOING = "↗️"
const val EMOJI_INCOMING = "↩️"
const val UNKNOWN = "(unknown)"
const val THEME_NONE = ""
const val THEME_DEFAULT = THEME_LIGHT

internal const val NAMELESS = "(name not given)"
internal const val ALREADY_CONCLUDED_MESSAGE = "An attempt was made to write to a boolog that was already concluded.\r\n<li>Once a Boolog has been concluded it can no longer be written to.\r\n<li>Passing a Boolog to the ShowBoolog() method will automatically conclude it."
internal const val MAX_OBJECT_FIELDS_TO_DISPLAY = 10
internal const val MAX_SHOW_OBJECT_RECURSION = 10
internal const val MAX_HEADERS_TO_DISPLAY = 10
internal const val MAX_BODY_LENGTH_TO_DISPLAY = 500