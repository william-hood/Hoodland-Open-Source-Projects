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

package hoodland.opensource.descriptions

enum class StringFieldTargets {
    DEFAULT, NULL, HAPPY_PATH, EXPLICIT, EMPTY_STRING, EXCESSIVELY_LONG, PADDED_WHITESPACE, PRECEDING_WHITESPACE, TRAILING_WHITESPACE, ALL_WHITESPACE, CONTAINS_NEWLINE, CONTAINS_ASIAN_CHARACTERS, CONTAINS_NUMERIC_CHARACTERS, CONTAINS_PERIOD, CONTAINS_BACKTICK, CONTAINS_TILDE, CONTAINS_EXCLAMATION_POINT, CONTAINS_AT_SYMBOL, CONTAINS_HASH_SYMBOL, CONTAINS_DOLLAR_SIGN, CONTAINS_PERCENT_SIGN, CONTAINS_CARET_SYMBOL, CONTAINS_AMPERSAND, CONTAINS_STAR, CONTAINS_OPEN_PARENTHESIS, CONTAINS_CLOSE_PARENTHESIS, CONTAINS_OPEN_BRACE, CONTAINS_CLOSE_BRACE, CONTAINS_OPEN_BRACKET, CONTAINS_CLOSE_BRACKET, CONTAINS_DASH, CONTAINS_UNDERSCORE, CONTAINS_EQUALS_SIGN, CONTAINS_PLUS_SIGN, CONTAINS_PIPE_SYMBOL, CONTAINS_BACKSLASH, CONTAINS_FORWARD_SLASH, CONTAINS_LESS_THAN_SIGN, CONTAINS_GREATER_THAN_SIGN, CONTAINS_COLON, CONTAINS_SEMICOLON, CONTAINS_COMMA, CONTAINS_QUESTION_MARK, CONTAINS_BELL_CHARACTER, CONTAINS_TAB_CHARACTER, CONTAINS_CARRIAGE_RETURN_CHARACTER, CONTAINS_LINE_FEED_CHARACTER, CONTAINS_FORM_FEED_CHARACTER, CONTAINS_SPACE, CONTAINS_SINGLE_QUOTE, ENCLOSED_SINGLE_QUOTES, CONTAINS_DOUBLE_QUOTE, ENCLOSED_DOUBLE_QUOTES, ENCLOSED_PARENTHESES, ENCLOSED_BRACKETS, ENCLOSED_BRACES, ENCLOSED_POINTY_BRACKETS, CONTAINS_SQL, ONLY_SQL, CONTAINS_HTML, ONLY_HTML, CONTAINS_XML, ONLY_XML, CONTAINS_JAVASCRIPT, ONLY_JAVASCRIPT;

    override fun toString(): String {
        when (this) {
            ALL_WHITESPACE -> return "All Whitespace"
            CONTAINS_AMPERSAND -> return "Contains Ampersand"
            CONTAINS_ASIAN_CHARACTERS -> return "Contains Asian Characters"
            CONTAINS_AT_SYMBOL -> return "Contains At Symbol"
            CONTAINS_BACKSLASH -> return "Contains Backslash Character"
            CONTAINS_BACKTICK -> return "Contains Backtick Character"
            CONTAINS_BELL_CHARACTER -> return "Contains Bell Character"
            CONTAINS_CARET_SYMBOL -> return "Contains Caret Symbol"
            CONTAINS_CLOSE_BRACE -> return "Contains Closing Brace"
            CONTAINS_CLOSE_BRACKET -> return "Contains Closing Bracket"
            CONTAINS_CLOSE_PARENTHESIS -> return "Contains Closing Parenthesis"
            CONTAINS_COLON -> return "Contains Colon"
            CONTAINS_COMMA -> return "Contains Comma"
            CONTAINS_DASH -> return "Contains Dash"
            CONTAINS_DOLLAR_SIGN -> return "Contains Dollar Sign"
            CONTAINS_EQUALS_SIGN -> return "Contains Equals Sign"
            CONTAINS_EXCLAMATION_POINT -> return "Contains Exclamation Point"
            CONTAINS_FORWARD_SLASH -> return "Contains Forward Slash Character"
            CONTAINS_GREATER_THAN_SIGN -> return "Contains Greater Than Sign"
            CONTAINS_HASH_SYMBOL -> return "Contains Hash Symbol"
            CONTAINS_HTML -> return "Contains Inserted HTML"
            CONTAINS_JAVASCRIPT -> return "Contains Inserted JavaScript"
            CONTAINS_LESS_THAN_SIGN -> return "Contains Less Than Sign"
            CONTAINS_CARRIAGE_RETURN_CHARACTER -> return "Contains Carriage Return Character"
            CONTAINS_LINE_FEED_CHARACTER -> return "Contains Line Feed Character"
            CONTAINS_FORM_FEED_CHARACTER -> return "Contains Form Feed Character"
            CONTAINS_SPACE -> return "Contains Inserted Space"
            CONTAINS_NEWLINE -> return "Contains Newline Character"
            CONTAINS_NUMERIC_CHARACTERS -> return "Contains Numeric Characters"
            CONTAINS_OPEN_BRACE -> return "Contains Opening Brace"
            CONTAINS_OPEN_BRACKET -> return "Contains Opening Bracket"
            CONTAINS_OPEN_PARENTHESIS -> return "Contains Opening Parenthesis"
            CONTAINS_PERCENT_SIGN -> return "Contains Percent Sign"
            CONTAINS_PERIOD -> return "Contains Period"
            CONTAINS_PIPE_SYMBOL -> return "Contains Pipe Symbol"
            CONTAINS_PLUS_SIGN -> return "Contains Plus Sign"
            CONTAINS_QUESTION_MARK -> return "Contains Question Mark"
            CONTAINS_SEMICOLON -> return "Contains Semicolon"
            CONTAINS_SINGLE_QUOTE -> return "Contains Single Quote"
            CONTAINS_DOUBLE_QUOTE -> return "Contains Double Quote"
            CONTAINS_SQL -> return "Contains Inserted SQL"
            CONTAINS_STAR -> return "Contains Star Character"
            CONTAINS_TAB_CHARACTER -> return "Contains Tab Character"
            CONTAINS_TILDE -> return "Contains Tilde"
            CONTAINS_UNDERSCORE -> return "Contains Underscore"
            CONTAINS_XML -> return "Contains Inserted XML"
            EMPTY_STRING -> return "Empty String"
            ENCLOSED_BRACES -> return "Enclosed in Braces"
            ENCLOSED_BRACKETS -> return "Enclosed in Brackets"
            ENCLOSED_DOUBLE_QUOTES -> return "Enclosed in Double Quotes"
            ENCLOSED_PARENTHESES -> return "Enclosed in Parentheses"
            ENCLOSED_POINTY_BRACKETS -> return "Enclosed in Pointy Brackets"
            ENCLOSED_SINGLE_QUOTES -> return "Enclosed in Single Quotes"
            EXCESSIVELY_LONG -> return "Excessively Long"
            EXPLICIT -> return "Explicit Value"
            HAPPY_PATH -> return "Happy Path"
            NULL -> return "Explicit Null"
            ONLY_HTML -> return "Purely HTML"
            ONLY_JAVASCRIPT -> return "Purely JavaScript"
            ONLY_SQL -> return "Purely SQL"
            ONLY_XML -> return "Purely XML"
            PADDED_WHITESPACE -> return "Padded Whitespace Both Sides"
            PRECEDING_WHITESPACE -> return "Has Preceding Whitespace"
            TRAILING_WHITESPACE -> return "Has Trailing Whitespace"
        }
        return "Left Default"
    }

    val isHappyOrExplicit: Boolean
        get() {
            if (this == HAPPY_PATH) return true
            return if (this == EXPLICIT) true else false
        }

    val isEmpty: Boolean
        get() {
            if (this == ALL_WHITESPACE) return true
            if (this == EMPTY_STRING) return true
            if (this == NULL) return true
            return if (this == DEFAULT) true else false
        }

    val isWhitespaceCase: Boolean
        get() {
            if (this == ALL_WHITESPACE) return true
            if (this == PADDED_WHITESPACE) return true
            if (this == PRECEDING_WHITESPACE) return true
            if (this == TRAILING_WHITESPACE) return true
            return if (this == CONTAINS_SPACE) true else false
        }

    val isScriptAttack: Boolean
        get() {
            if (this == CONTAINS_SQL) return true
            if (this == ONLY_SQL) return true
            if (this == CONTAINS_HTML) return true
            if (this == ONLY_HTML) return true
            if (this == CONTAINS_XML) return true
            if (this == ONLY_XML) return true
            if (this == CONTAINS_JAVASCRIPT) return true
            return if (this == ONLY_JAVASCRIPT) true else false
        }

    val isNonstandardCharacterCase: Boolean
        get() {
            when (this) {
                CONTAINS_AMPERSAND, CONTAINS_ASIAN_CHARACTERS, CONTAINS_AT_SYMBOL, CONTAINS_BACKSLASH, CONTAINS_BACKTICK, CONTAINS_BELL_CHARACTER, CONTAINS_CARET_SYMBOL, CONTAINS_CLOSE_BRACE, CONTAINS_CLOSE_BRACKET, CONTAINS_CLOSE_PARENTHESIS, CONTAINS_COLON, CONTAINS_COMMA, CONTAINS_DASH, CONTAINS_DOLLAR_SIGN, CONTAINS_EQUALS_SIGN, CONTAINS_EXCLAMATION_POINT, CONTAINS_FORWARD_SLASH, CONTAINS_GREATER_THAN_SIGN, CONTAINS_HASH_SYMBOL, CONTAINS_LESS_THAN_SIGN, CONTAINS_CARRIAGE_RETURN_CHARACTER, CONTAINS_LINE_FEED_CHARACTER, CONTAINS_FORM_FEED_CHARACTER, CONTAINS_SPACE, CONTAINS_NEWLINE, CONTAINS_NUMERIC_CHARACTERS, CONTAINS_OPEN_BRACE, CONTAINS_OPEN_BRACKET, CONTAINS_OPEN_PARENTHESIS, CONTAINS_PERCENT_SIGN, CONTAINS_PERIOD, CONTAINS_PIPE_SYMBOL, CONTAINS_PLUS_SIGN, CONTAINS_QUESTION_MARK, CONTAINS_SEMICOLON, CONTAINS_SINGLE_QUOTE, CONTAINS_DOUBLE_QUOTE, CONTAINS_STAR, CONTAINS_TAB_CHARACTER, CONTAINS_TILDE, CONTAINS_UNDERSCORE, ENCLOSED_BRACES, ENCLOSED_BRACKETS, ENCLOSED_DOUBLE_QUOTES, ENCLOSED_PARENTHESES, ENCLOSED_POINTY_BRACKETS, ENCLOSED_SINGLE_QUOTES -> return true
            }
            return false
        }
}