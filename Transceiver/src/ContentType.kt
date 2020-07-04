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

const val CONTENT_TYPE_HEADER_KEY = "Content-Type"
const val TYPE_SUBTYPE_DELIMITER = "/"
private const val BOUNDARY_IDENTIFIER = "boundary="
private const val BOUNDARY_MISSING = "Content Type header does not designate a multipart boundary."

fun ContentTypeHeaderValue(type: String, subtype: String?): String? {
    if (StringIsEmpty(type)) return null // Java version used "" to allow omission of the Content Type header.
    val result = StringBuilder(type)
    if (!StringIsEmpty(subtype)) {
        result.append(TYPE_SUBTYPE_DELIMITER)
        result.append(subtype)
    }
    return result.toString()
}

val HttpHeaders.contentTypeIsSet: Boolean
    get() = this.containsKey(CONTENT_TYPE_HEADER_KEY)

val HttpHeaders.multipartBoundary: String
    get() {
        if (contentType.asKnownContentType === ContentType.multipart) { throw HttpMessageParseException(BOUNDARY_MISSING) }

        var index = contentSubtype.lastIndexOf(BOUNDARY_IDENTIFIER)
        if (index == -1) throw HttpMessageParseException(BOUNDARY_MISSING)
        index += BOUNDARY_IDENTIFIER.length
        return contentSubtype.substring(index)
    }

private fun HttpHeaders.enforceTypeAndSubtypeSet() {
    if (determinedContentType == null) {
        if (this.contentTypeIsSet) {
            val tmp = this[CONTENT_TYPE_HEADER_KEY]!!.first().split(TYPE_SUBTYPE_DELIMITER.toRegex()).toTypedArray()
            try {
                determinedContentType = tmp[0]
                determinedContentSubtype = tmp[1]
            } catch (dontCare: Exception) {
                // Deliberate NO-OP
                // Assuming this to be a missing subtype
            }
        } else {
            throw MissingContentTypeException()
        }
    }
}

val HttpHeaders.contentType: String
    get() {
        enforceTypeAndSubtypeSet()
        return determinedContentType!!
    }

val HttpHeaders.contentSubtype: String
    get() {
        enforceTypeAndSubtypeSet()
        return determinedContentSubtype!!
    }

val HttpHeaders.contentIsMultipart: Boolean
    get() = contentType == ContentType.multipart.toString()

val HttpHeaders.contentIsText: Boolean
    get() {
        if (contentType == ContentType.text.toString()) { return true }
        if (contentIsMultipart) { return false }
        val asObj = contentSubtype.asKnownContentType
        if (asObj == null)  { return false }
        return asObj.isText
    }

val HttpHeaders.contentIsBinary: Boolean
    get() = if (contentIsMultipart) false else !contentIsText

class ContentType(stringValue: String, isText: Boolean = false) {
    val stringValue: String = stringValue
    val isText: Boolean = isText

    init {
        ContentType.dictionary.put(this.stringValue, this)
    }

    override fun toString(): String = stringValue
    
    companion object  {
        internal val dictionary = HashMap<String, ContentType>()

        // Some of the content types and subtypes have dashes in them. Variable/Enum
        // names may not have dashes, thus the String value has to be explict.
        // It was not appropriate to use an enum because each item needs to know
        // whether or not it is text.
        val application = ContentType("application")
        val audio = ContentType("audio")
        val avi = ContentType("avi")
        val base64 = ContentType("base64", true)
        val bmp = ContentType("bmp")
        val book = ContentType("book")
        val chemical = ContentType("chemical")
        val comma_separated_values = ContentType("comma-separated-values", true)
        val crescendo = ContentType("crescendo")
        val css = ContentType("css", true)
        val csv = ContentType("csv", true)
        val ecmascript = ContentType("ecmascript", true)
        val excel = ContentType("excel")
        val form_data = ContentType("form-data")
        val gif = ContentType("gif")
        val html = ContentType("html", true)
        val image = ContentType("image")
        val java = ContentType("java", true)
        val java_byte_code = ContentType("java-byte-code")
        val javascript = ContentType("javascript", true)
        val jpeg = ContentType("jpeg")
        val json = ContentType("json", true)
        val mac_binary = ContentType("mac-binary")
        val macbinary = ContentType("macbinary")
        val midi = ContentType("midi")
        val mime = ContentType("mime")
        val mixed = ContentType("mixed")
        val model = ContentType("model")
        val movie = ContentType("movie")
        val mpeg = ContentType("mpeg")
        val mpeg3 = ContentType("mpeg3")
        val ms_powerpoint = ContentType("ms-powerpoint")
        val mspowerpoint = ContentType("mspowerpoint")
        val msword = ContentType("msword")
        val mswrite = ContentType("mswrite")
        val multipart = ContentType("multipart")
        val octet_stream = ContentType("octet-stream")
        val pascal = ContentType("pascal", true)
        val pdf = ContentType("pdf")
        val plain = ContentType("plain", true)
        val png = ContentType("png")
        val postscript = ContentType("postscript")
        val powerpoint = ContentType("powerpoint")
        val quicktime = ContentType("quicktime")
        val richtext = ContentType("richtext", true)
        val rn_realtext = ContentType("rn-realtext")
        val rtf = ContentType("rtf", true)
        val tab_separated_values = ContentType("tab-separated-values", true)
        val text = ContentType("text", true)
        val tiff = ContentType("tiff")
        val video = ContentType("video")
        val vnd_ms_excel = ContentType("vnd.ms-excel")
        val vnd_ms_project = ContentType("vnd.ms-project")
        val vrml = ContentType("vrml", true)
        val wav = ContentType("wav")
        val wordperfect = ContentType("wordperfect")
        val wordperfect6_0 = ContentType("wordperfect6.0")
        val wordperfect6_1 = ContentType("wordperfect6.1")
        val x_bzip = ContentType("x-bzip")
        val x_bzip2 = ContentType("x-bzip2")
        val x_chat = ContentType("x-chat")
        val x_compress = ContentType("x-compress")
        val x_compressed = ContentType("x-compressed")
        val x_conference = ContentType("x-conference")
        val x_dvi = ContentType("x-dvi")
        val x_excel = ContentType("x-excel")
        val x_fortran = ContentType("x-fortran")
        val x_gzip = ContentType("x-gzip")
        val x_java_class = ContentType("x-java-class")
        val x_javascript = ContentType("x-javascript", true)
        val x_json = ContentType("x-json", true)
        val x_karaoke = ContentType("x-karaoke")
        val x_lisp = ContentType("x-lisp", true)
        val x_mid = ContentType("x-mid")
        val x_midi = ContentType("x-midi")
        val x_motion_jpeg = ContentType("x-motion-jpeg")
        val x_mpeg = ContentType("x-mpeg")
        val x_mpeg_3 = ContentType("x-mpeg-3")
        val x_mpeq2a = ContentType("x-mpeq2a")
        val x_msexcel = ContentType("x-msexcel")
        val x_pn_realaudio = ContentType("x-pn-realaudio")
        val x_pn_realaudio_plugin = ContentType("x-pn-realaudio-plugin")
        val x_quicktime = ContentType("x-quicktime")
        val x_realaudio = ContentType("x-realaudio")
        val x_rtf = ContentType("x-rtf", true)
        val x_script_lisp = ContentType("x-script.lisp", true)
        val x_script_perl = ContentType("x-script.perl", true)
        val x_tiff = ContentType("x-tiff")
        val x_visio = ContentType("x-visio")
        val x_vrml = ContentType("x-vrml", true)
        val x_wav = ContentType("x-wav")
        val x_world = ContentType("x-world")
        val x_www_form_urlencoded = ContentType("x-www-form-urlencoded")
        val x_xbitmap = ContentType("x-xbitmap")
        val x_xbm = ContentType("x-xbm")
        val x_xpixmap = ContentType("x-xpixmap")
        val x_zip = ContentType("x-zip")
        val x_zip_compressed = ContentType("x-zip-compressed")
        val xbm = ContentType("xbm")
        val xml = ContentType("xml")
        val xpm = ContentType("xpm")
        val zip = ContentType("zip")
    }
}

val String.asKnownContentType: ContentType?
        get() = ContentType.dictionary[this]