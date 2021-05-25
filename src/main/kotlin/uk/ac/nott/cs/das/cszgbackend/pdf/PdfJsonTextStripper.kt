package uk.ac.nott.cs.das.cszgbackend.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition
import org.springframework.stereotype.Component

@Component
class PdfJsonTextStripper : PDFTextStripper() {
    init {
        sortByPosition = true
    }

    var firstPage = true
    var firstTextObject = true

    override fun startDocument(doc: PDDocument?) = writeString("""{"version":"${doc?.version}" "pages":[""")
    override fun startPage(page: PDPage?) {
        if (!firstPage) writeString(",")
        firstPage = false
        firstTextObject = true
        writeString("""{"pageNo":$currentPageNo,"width":${page?.artBox?.width},"height":${page?.artBox?.height},"textObjects":[""")
    }

    override fun endPage(page: PDPage?) = writeString("""]}""")
    override fun endDocument(document: PDDocument?) = writeString("]}")

    override fun writeString(text: String, textPositions: MutableList<TextPosition>) {
        val fontName = textPositions[0].font.name
        val fontSize = textPositions[0].fontSizeInPt
        val x1 = textPositions.minOf { it.x }
        val y1 = textPositions.minOf { it.y }
        val x2 = textPositions.maxOf { it.endX }
        val y2 = textPositions.maxOf { it.endY }

        if (!firstTextObject) writeString(",")
        firstTextObject = false
        writeString("""{"text":"$text","fontName":"$fontName","fontSize":$fontSize,"x1":$x1,"y1":$y1,"x2":$x2,"y2":$y2}""")
    }
}
