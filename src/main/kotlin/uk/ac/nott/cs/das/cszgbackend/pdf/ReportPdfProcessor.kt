package uk.ac.nott.cs.das.cszgbackend.pdf

import arrow.core.Either
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.study.Report

@Component
class ReportPdfProcessor {
    fun processReport(report: Report): Either<ResponseStatusException, Report> = TODO("Not yet implemented")
}
