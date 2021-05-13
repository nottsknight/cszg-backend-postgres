package uk.ac.nott.cs.das.cszgbackend.pdf

import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
data class PdfJsonDocument(
    var version: String,
    var pages: MutableList<PdfJsonPage>
) : Iterable<PdfJsonPage> {

    override fun iterator() = pages.iterator()
}

@Serializable
data class PdfJsonPage(
    var pageNo: Int,
    var width: Double,
    var height: Double,
    var textObjects: MutableList<PdfJsonTextObject>
) : Iterable<PdfJsonTextObject> {

    override fun iterator() = textObjects.iterator()
}

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

infix fun PdfJsonTextObject.distanceTo(other: PdfJsonTextObject) = abs(other.x1 - this.x2)