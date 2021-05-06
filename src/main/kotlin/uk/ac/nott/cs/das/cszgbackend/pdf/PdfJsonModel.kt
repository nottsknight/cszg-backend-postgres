package uk.ac.nott.cs.das.cszgbackend.pdf

import kotlinx.serialization.Serializable

@Serializable
data class PdfJsonDocument(
    var version: String,
    var pages: MutableList<PdfJsonPage>
)

@Serializable
data class PdfJsonPage(
    var pageNo: Int,
    var width: Double,
    var height: Double,
    var textObjects: MutableList<PdfJsonTextObject>
)

@Serializable
data class PdfJsonTextObject(
    var text: String,
    var fontName: String,
    var fontSize: Double,
    var x1: Double,
    var y1: Double,
    var x2: Double,
    var y2: Double
)
