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

package rockabilly.descriptions

import rockabilly.toolbox.CRLF

// ALL FIELD DESCRIPTIONS NEED A TOSTRING() METHOD AND A CYCLING CLASS.
// TODO: MAKE IT POSSIBLE TO EASILY CREATE LETTERED SUB-CASES THAT ITERATE ALL POSSIBILITIES
//       (Note during Kotlin port: StringFieldSubnameDescription may be exactly this.)

open class StringFieldDescription() : FieldDescription<String>() {
    var target = StringFieldTargets.HAPPY_PATH
    private val HTML_HACK = "<html><head><title>TITLE</title></head><body>Howdy</body></html>"
    private val SQL_HACK = "SELECT 'Hello, World!';"
    private val JAVASCRIPT_HACK = "function displayDate(){document.getElementById(\"demo\").innerHTML=Date();}"
    private val XML_HACK = "<?xml version=\"1.0\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2001/12/soap-envelope\" soap:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\"><soap:Body xmlns:m=\"http://www.example.org/stock\"><m:GetStockPrice><m:StockName>IBM</m:StockName></m:GetStockPrice></soap:Body></soap:Envelope>"

    constructor(BasisValue: String) : this() { this.basisValue = BasisValue }

    override fun hasSpecificHappyValue(): Boolean {
        return target === StringFieldTargets.HAPPY_PATH && basisValue != null
    }

    override val isExplicit: Boolean
        get() = (target === StringFieldTargets.EXPLICIT)

    override val isDefault: Boolean
        get() = (target === StringFieldTargets.DEFAULT)

    private val middleIndex: Int
        get() = Math.round(basisValue!!.length / 2.toFloat())

    private val firstHalf: String?
        get() = basisValue!!.substring(0, middleIndex)

    private val secondHalf: String?
        get() = basisValue!!.substring(middleIndex + 1)

    override val describedValue: String?
        get() {
        return when (target) {
            StringFieldTargets.DEFAULT -> throw NoValueException()
            StringFieldTargets.HAPPY_PATH, StringFieldTargets.EXPLICIT -> {
                if (basisValue == null) throw InappropriateDescriptionException()
                basisValue
            }
            StringFieldTargets.ALL_WHITESPACE -> "             "
            StringFieldTargets.CONTAINS_AMPERSAND -> firstHalf.toString() + "&" + secondHalf
            StringFieldTargets.CONTAINS_ASIAN_CHARACTERS -> firstHalf.toString() + "\u60a8\u597d\u5927\u5bb6" /*"您好大家"*/ + secondHalf
            StringFieldTargets.CONTAINS_AT_SYMBOL -> firstHalf.toString() + "@" + secondHalf
            StringFieldTargets.CONTAINS_BACKSLASH -> firstHalf.toString() + "\\" + secondHalf
            StringFieldTargets.CONTAINS_BACKTICK -> firstHalf.toString() + "`" + secondHalf
            StringFieldTargets.CONTAINS_BELL_CHARACTER -> firstHalf.toString() + "\u2407" + secondHalf
            StringFieldTargets.CONTAINS_CARET_SYMBOL -> firstHalf.toString() + "^" + secondHalf
            StringFieldTargets.CONTAINS_CLOSE_BRACE -> firstHalf.toString() + "}" + secondHalf
            StringFieldTargets.CONTAINS_CLOSE_BRACKET -> firstHalf.toString() + "]" + secondHalf
            StringFieldTargets.CONTAINS_CLOSE_PARENTHESIS -> firstHalf.toString() + ")" + secondHalf
            StringFieldTargets.CONTAINS_COLON -> firstHalf.toString() + ":" + secondHalf
            StringFieldTargets.CONTAINS_COMMA -> firstHalf.toString() + "," + secondHalf
            StringFieldTargets.CONTAINS_DASH -> firstHalf.toString() + "-" + secondHalf
            StringFieldTargets.CONTAINS_DOLLAR_SIGN -> firstHalf.toString() + "$" + secondHalf
            StringFieldTargets.CONTAINS_EQUALS_SIGN -> firstHalf.toString() + "=" + secondHalf
            StringFieldTargets.CONTAINS_EXCLAMATION_POINT -> firstHalf.toString() + "!" + secondHalf
            StringFieldTargets.CONTAINS_FORWARD_SLASH -> firstHalf.toString() + "/" + secondHalf
            StringFieldTargets.CONTAINS_GREATER_THAN_SIGN -> firstHalf.toString() + ">" + secondHalf
            StringFieldTargets.CONTAINS_HASH_SYMBOL -> firstHalf.toString() + "#" + secondHalf
            StringFieldTargets.CONTAINS_HTML -> firstHalf.toString() + " " + HTML_HACK + " " + secondHalf
            StringFieldTargets.CONTAINS_JAVASCRIPT -> firstHalf.toString() + " " + JAVASCRIPT_HACK + " " + secondHalf
            StringFieldTargets.CONTAINS_LESS_THAN_SIGN -> firstHalf.toString() + "<" + secondHalf
            StringFieldTargets.CONTAINS_CARRIAGE_RETURN_CHARACTER -> firstHalf.toString() + "\r" + secondHalf
            StringFieldTargets.CONTAINS_LINE_FEED_CHARACTER -> firstHalf.toString() + "\n" + secondHalf
            StringFieldTargets.CONTAINS_FORM_FEED_CHARACTER -> firstHalf.toString() + "\u000C" + secondHalf
            StringFieldTargets.CONTAINS_SPACE -> firstHalf.toString() + " " + secondHalf
            StringFieldTargets.CONTAINS_NEWLINE -> firstHalf + CRLF + secondHalf
            StringFieldTargets.CONTAINS_NUMERIC_CHARACTERS -> firstHalf.toString() + "1234567890" + secondHalf
            StringFieldTargets.CONTAINS_OPEN_BRACE -> firstHalf.toString() + "{" + secondHalf
            StringFieldTargets.CONTAINS_OPEN_BRACKET -> firstHalf.toString() + "[" + secondHalf
            StringFieldTargets.CONTAINS_OPEN_PARENTHESIS -> firstHalf.toString() + "(" + secondHalf
            StringFieldTargets.CONTAINS_PERCENT_SIGN -> firstHalf.toString() + "%" + secondHalf
            StringFieldTargets.CONTAINS_PERIOD -> firstHalf.toString() + "." + secondHalf
            StringFieldTargets.CONTAINS_PIPE_SYMBOL -> firstHalf.toString() + "|" + secondHalf
            StringFieldTargets.CONTAINS_PLUS_SIGN -> firstHalf.toString() + "+" + secondHalf
            StringFieldTargets.CONTAINS_QUESTION_MARK -> firstHalf.toString() + "?" + secondHalf
            StringFieldTargets.CONTAINS_SEMICOLON -> firstHalf.toString() + ";" + secondHalf
            StringFieldTargets.CONTAINS_SINGLE_QUOTE -> firstHalf.toString() + "'" + secondHalf
            StringFieldTargets.CONTAINS_DOUBLE_QUOTE -> firstHalf.toString() + "\"" + secondHalf
            StringFieldTargets.CONTAINS_SQL -> firstHalf.toString() + " " + SQL_HACK + " " + secondHalf
            StringFieldTargets.CONTAINS_STAR -> firstHalf.toString() + "*" + secondHalf
            StringFieldTargets.CONTAINS_TAB_CHARACTER -> firstHalf.toString() + "\t" + secondHalf
            StringFieldTargets.CONTAINS_TILDE -> firstHalf.toString() + "~" + secondHalf
            StringFieldTargets.CONTAINS_UNDERSCORE -> firstHalf.toString() + "_" + secondHalf
            StringFieldTargets.CONTAINS_XML -> firstHalf.toString() + " " + XML_HACK + " " + secondHalf
            StringFieldTargets.EMPTY_STRING -> ""
            StringFieldTargets.ENCLOSED_BRACES -> "{$basisValue}"
            StringFieldTargets.ENCLOSED_BRACKETS -> "[$basisValue]"
            StringFieldTargets.ENCLOSED_DOUBLE_QUOTES -> "\"" + basisValue + "\""
            StringFieldTargets.ENCLOSED_PARENTHESES -> "($basisValue)"
            StringFieldTargets.ENCLOSED_POINTY_BRACKETS -> "<$basisValue>"
            StringFieldTargets.ENCLOSED_SINGLE_QUOTES -> "'$basisValue'"
            StringFieldTargets.EXCESSIVELY_LONG -> "fdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjffdagshjafguyagyuwefuyabvubmcxvvFHWGYDISFIDFI4637285462850qzmglpbotrekwisirutjf"
            StringFieldTargets.NULL -> null
            StringFieldTargets.ONLY_HTML -> HTML_HACK
            StringFieldTargets.ONLY_JAVASCRIPT -> JAVASCRIPT_HACK
            StringFieldTargets.ONLY_SQL -> SQL_HACK
            StringFieldTargets.ONLY_XML -> XML_HACK
            StringFieldTargets.PADDED_WHITESPACE -> "     $basisValue      "
            StringFieldTargets.PRECEDING_WHITESPACE -> "     $basisValue"
            StringFieldTargets.TRAILING_WHITESPACE -> "$basisValue      "
        }
        return null
    }

    override fun toString(): String {
        return if (target === StringFieldTargets.EXPLICIT) "$target ($basisValue)" else target.toString()
    }

    override fun setExplicitValue(value: String) {
        basisValue = value
        target = StringFieldTargets.EXPLICIT
    }

}