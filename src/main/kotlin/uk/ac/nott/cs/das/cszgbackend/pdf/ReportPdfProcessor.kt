package uk.ac.nott.cs.das.cszgbackend.pdf

import arrow.core.Either
import arrow.core.computations.either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.study.Report

@Component
class ReportPdfProcessor(private val textStripper: PDFTextStripper) {
    suspend fun processReport(report: Report): Either<ResponseStatusException, Report> = either {
        val docText = loadDocument(report.pdfData).bind().use { extractText(it) }.bind()
        report
    }

    private suspend fun loadDocument(data: ByteArray) = withContext(Dispatchers.IO) {
        try {
            Either.Right(PDDocument.load(data))
        } catch (e: Exception) {
            Either.Left(ResponseStatusException(HttpStatus.BAD_REQUEST, e.localizedMessage, e))
        }
    }

    private suspend fun extractText(doc: PDDocument) = withContext(Dispatchers.IO) {
        try {
            Either.Right(textStripper.getText(doc))
        } catch (e: Exception) {
            Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.localizedMessage, e))
        }
    }
}
