package uk.ac.nott.cs.das.cszgbackend.pdf

import arrow.core.Either
import arrow.core.computations.either
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.AnnotationPipeline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.study.Report
import uk.ac.nott.cs.das.cszgbackend.model.study.Sentence
import uk.ac.nott.cs.das.cszgx.mode
import uk.ac.nott.cs.das.cszgx.pairs

@Component
class ReportPdfProcessor(
    private val textStripper: PdfJsonTextStripper,
    private val nlpPipeline: AnnotationPipeline
) {

    suspend fun processReport(report: Report) = either<ResponseStatusException, Report> {
        val docText = loadDocumentText(report.pdfData).bind()
        val docModel = parseDocumentJson(docText).bind()

        for ((_, _, _, textObjects) in docModel) {
            val lines = groupLines(textObjects).flatMap { splitOnFont(it) }.flatMap { splitOnSpacing(it) }
            val mainFont = lines.map { it[0] }.map { "${it.fontName}/${it.fontSize}" }.mode()
            val mainFontLines = lines.filter { "${it[0].fontName}/${it[0].fontSize}" == mainFont }
            val mainText = mainFontLines.map { it.map { obj -> obj.text } }.map { it.reduce { acc, s -> acc + s } }

            mainText
                .map { Annotation(it) }
                .apply { nlpPipeline.annotate(this) }
                .flatMap { it.get(CoreAnnotations.SentencesAnnotation::class.java) }
                .map {
                    Sentence(
                        content = it.get(CoreAnnotations.OriginalTextAnnotation::class.java),
                        report = report,
                        precisionLabels = IntArray(0),
                        recallLabels = IntArray(0),
                        fragments = mutableSetOf()
                    )
                }
                .forEach { s ->
                    report.sentences.add(s)
                }
        }
        report
    }

    private suspend fun loadDocumentText(data: ByteArray) = withContext(Dispatchers.IO) {
        Either.catch { PDDocument.load(data).use { textStripper.getText(it) } }
            .mapLeft { e -> ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.localizedMessage, e) }
    }

    private fun parseDocumentJson(json: String) =
        Either.catch { Json.decodeFromString<PdfJsonDocument>(json) }
            .mapLeft { e -> ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.localizedMessage, e) }

    private fun groupLines(objects: List<PdfJsonTextObject>) = objects.groupBy { it.y1 }.values.toList()

    private fun splitOnFont(line: List<PdfJsonTextObject>) =
        (0..line.lastIndex + 1)
            .filter { it == 0 || it == line.lastIndex + 1 || line[it].fontName != line[it - 1].fontName || line[it].fontSize != line[it - 1].fontSize }
            .pairs()
            .map { (i, j) -> line.subList(i, j) }
            .toList()

    private fun splitOnSpacing(line: List<PdfJsonTextObject>) =
        line.pairs().map { (o1, o2) -> o1 distanceTo o2 }.sum().let { distanceSum ->
            val meanSpacing = distanceSum / line.size - 1
            (0..line.lastIndex + 1)
                .filter { i -> i == 0 || i == line.lastIndex + 1 || line[i] distanceTo line[i - 1] >= meanSpacing * SPACING_THRESHOLD }
                .pairs()
                .map { (i, j) -> line.subList(i, j) }
                .toList()
        }

    companion object {
        const val SPACING_THRESHOLD = 0.05
    }
}
