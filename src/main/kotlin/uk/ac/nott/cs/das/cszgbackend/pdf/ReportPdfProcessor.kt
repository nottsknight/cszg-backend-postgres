package uk.ac.nott.cs.das.cszgbackend.pdf

import arrow.core.Either
import arrow.core.computations.either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.study.Report

@Component
class ReportPdfProcessor(private val textStripper: PdfJsonTextStripper) {
    suspend fun processReport(report: Report): Either<ResponseStatusException, Report> = either {
        val docText = loadDocumentText(report.pdfData).bind()
        val docModel = Json.decodeFromString<PdfJsonDocument>(docText)
        for (p in docModel) {
            val lines = groupLines(p.textObjects).flatMap { splitLine(it) }
        }
        report
    }

    private suspend fun loadDocumentText(data: ByteArray) = withContext(Dispatchers.IO) {
        try {
            PDDocument.load(data).use { textStripper.getText(it) }.let { Either.Right(it) }
        } catch (e: Exception) {
            Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.localizedMessage, e))
        }
    }

    private fun groupLines(objects: List<PdfJsonTextObject>) = objects.groupBy { it.y1 }.values.toList()

    private fun splitLine(line: List<PdfJsonTextObject>): List<List<PdfJsonTextObject>> {
        val splitPoints = mutableListOf<Int>()
        for (i in 1..line.lastIndex) {
            if (line[i].fontName != line[i - 1].fontName || line[i].fontSize != line[i - 1].fontSize) splitPoints.add(i)
        }
        splitPoints.add(line.lastIndex + 1)

        var i = 0
        val splits = mutableListOf<List<PdfJsonTextObject>>()
        for (j in splitPoints) {
            splits.add(line.subList(i, j))
            i = j
        }
        return splits
    }
}
