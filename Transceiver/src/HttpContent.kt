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

package rockabilly.transceiver

import rockabilly.toolbox.StringIsEmpty
import rockabilly.toolbox.UnsetString


class HttpContent {
    var type: String = UnsetString
    var subtype: String = UnsetString

    constructor() {}
    constructor(contentType: String, contentSubtype: String) {
        type = contentType
        subtype = contentSubtype
    }

    val isSet: Boolean
        get() = type !== UnsetString

    @get:Throws(HttpMessageParseException::class)
    val multipartBoundary: String
        get() {
            if (type != multipart) throw HttpMessageParseException(BOUNDARY_MISSING)
            var index = subtype.lastIndexOf(BOUNDARY_IDENTIFIER)
            if (index == -1) throw HttpMessageParseException(BOUNDARY_MISSING)
            index += BOUNDARY_IDENTIFIER.length
            return subtype.substring(index)
        }

    override fun toString(): String {
        // Allow omission of Content-Type header by blanking out the type.
        if (StringIsEmpty(type)) return ""
        val result = StringBuilder(type)
        if (!StringIsEmpty(subtype)) {
            result.append(TYPE_SUBTYPE_DELIMITER)
            result.append(subtype)
        }
        return result.toString()
    }

    fun toHttpHeader(): HttpHeader {
        return HttpHeader(HEADER_KEY, toString())
    }

    val isMultipart: Boolean
        get() = type == multipart

            /*
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
		    */
    val isText: Boolean
        get() {
            if (isMultipart) return false
            if (type == text) return true
            if (subtype == text) return true
            if (subtype == json) return true
            if (subtype == html) return true
            if (subtype == java) return true
            if (subtype == css) return true
            if (subtype == base64) return true
            if (subtype == pascal) return true
            if (subtype == x_lisp) return true
            if (subtype == x_script_lisp) return true
            if (subtype == richtext) return true
            if (subtype == rtf) return true
            if (subtype == x_rtf) return true
            if (subtype == x_json) return true
            if (subtype == tab_separated_values) return true
            if (subtype == csv) return true
            if (subtype == comma_separated_values) return true
            if (subtype == x_script_perl) return true
            if (subtype == javascript) return true
            if (subtype == x_javascript) return true
            if (subtype == ecmascript) return true
            if (subtype == plain) return true
            if (subtype == vrml) return true
            return if (subtype == x_vrml) true else false
            /*
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            if (subtype == xxxxxxxxxx) return true
            */
        }

    val isBinary: Boolean
        get() = if (isMultipart) false else !isText

    companion object {
        const val HEADER_KEY = "Content-Type"
        const val TYPE_SUBTYPE_DELIMITER = "/"
        private const val BOUNDARY_IDENTIFIER = "boundary="
        private const val BOUNDARY_MISSING = "Content Type header does not designate a multipart boundary."
        fun fromString(candidate: String): HttpContent {
            val result = HttpContent()
            val tmp = candidate!!.split(TYPE_SUBTYPE_DELIMITER.toRegex()).toTypedArray()
            try {
                result.type = tmp[0]
                result.subtype = tmp[1]
            } catch (dontCare: Exception) {
                // Deliberate NO-OP
                // Assuming this to be a missing subtype
            }
            return result
        }

        fun fromHttpHeader(candidate: HttpHeader): HttpContent? {
            return if (candidate.key != HEADER_KEY) null else fromString(candidate.value)
        }

        const val application = "application"
        const val image = "image"
        const val video = "video"
        const val audio = "audio"
        const val text = "text"
        const val model = "model"
        const val chemical = "chemical"
        const val x_conference = "x-conference"
        const val multipart = "multipart"
        const val x_www_form_urlencoded = "x-www-form-urlencoded"
        const val form_data = "form-data"
        const val mixed = "mixed"
        const val x_world = "x-world"
        const val html = "html"
        const val postscript = "postscript"
        const val mime = "mime"
        const val octet_stream = "octet-stream"
        const val avi = "avi"
        const val macbinary = "macbinary"
        const val mac_binary = "mac-binary"
        const val bmp = "bmp"
        const val book = "book"
        const val plain = "plain"
        const val java = "java"
        const val java_byte_code = "java-byte-code"
        const val x_java_class = "x-java-class"
        const val css = "css"
        const val msword = "msword"
        const val x_dvi = "x-dvi"
        const val x_fortran = "x-fortran"
        const val gif = "gif"
        const val jpeg = "jpeg"
        const val javascript = "javascript"
        const val x_javascript = "x-javascript"
        const val ecmascript = "ecmascript"
        const val midi = "midi"
        const val x_karaoke = "x-karaoke"
        const val x_lisp = "x-lisp"
        const val x_script_lisp = "x-script.lisp"
        const val mpeg = "mpeg"
        const val x_mid = "x-mid"
        const val x_midi = "x-midi"
        const val crescendo = "crescendo"
        const val x_motion_jpeg = "x-motion-jpeg"
        const val base64 = "base64"
        const val quicktime = "quicktime"
        const val x_mpeg = "x-mpeg"
        const val x_mpeq2a = "x-mpeq2a"
        const val mpeg3 = "mpeg3"
        const val x_mpeg_3 = "x-mpeg-3"
        const val vnd_ms_project = "vnd.ms-project"
        const val pascal = "pascal"
        const val pdf = "pdf"
        const val x_script_perl = "x-script.perl"
        const val png = "png"
        const val mspowerpoint = "mspowerpoint"
        const val ms_powerpoint = "ms-powerpoint"
        const val powerpoint = "powerpoint"
        const val x_quicktime = "x-quicktime"
        const val x_pn_realaudio = "x-pn-realaudio"
        const val x_pn_realaudio_plugin = "x-pn-realaudio-plugin"
        const val x_realaudio = "x-realaudio"
        const val richtext = "richtext"
        const val rn_realtext = "rn-realtext"
        const val rtf = "rtf"
        const val x_rtf = "x-rtf"
        const val tiff = "tiff"
        const val x_tiff = "x-tiff"
        const val tab_separated_values = "tab-separated-values"
        const val comma_separated_values = "comma-separated-values"
        const val csv = "csv"
        const val x_visio = "x-visio"
        const val wordperfect6_0 = "wordperfect6.0"
        const val wordperfect6_1 = "wordperfect6.1"
        const val wav = "wav"
        const val x_wav = "x-wav"
        const val wordperfect = "wordperfect"
        const val mswrite = "mswrite"
        const val vrml = "vrml"
        const val x_vrml = "x-vrml"
        const val xbm = "xbm"
        const val x_xbm = "x-xbm"
        const val x_xbitmap = "x-xbitmap"
        const val excel = "excel"
        const val x_excel = "x-excel"
        const val x_msexcel = "x-msexcel"
        const val vnd_ms_excel = "vnd.ms-excel"
        const val xml = "xml"
        const val movie = "movie"
        const val x_xpixmap = "x-xpixmap"
        const val xpm = "xpm"
        const val x_compress = "x-compress"
        const val x_compressed = "x-compressed"
        const val x_zip_compressed = "x-zip-compressed"
        const val x_zip = "x-zip"
        const val zip = "zip"
        const val json = "json"
        const val x_json = "x-json"
        const val x_bzip = "x-bzip"
        const val x_bzip2 = "x-bzip2"
        const val x_chat = "x-chat"
        const val x_gzip = "x-gzip"
    /*
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	    const val xxxxxxx = "xxxxxxx"
	*/
    }
}