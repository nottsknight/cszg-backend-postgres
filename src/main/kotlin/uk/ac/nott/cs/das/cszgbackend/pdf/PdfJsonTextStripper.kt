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

    override fun startDocument(document: PDDocument?) = writeString("""{"pages":[""")
    override fun startPage(page: PDPage?) =
        writeString("""{"pageNo":$currentPageNo,"width":${page?.artBox?.width},"height":${page?.artBox?.height},"textObjects":[""")

    override fun endPage(page: PDPage?) = writeString("""]},""")
    override fun endDocument(document: PDDocument?) = writeString("]}")

    override fun writeString(text: String?, textPositions: MutableList<TextPosition>?) {
        super.writeString(text, textPositions)
    }
}
