package uk.ac.nott.cs.das.cszgbackend.controller

import org.springframework.web.bind.annotation.*
import uk.ac.nott.cs.das.cszgbackend.model.study.Report
import uk.ac.nott.cs.das.cszgbackend.model.study.ReportDto
import uk.ac.nott.cs.das.cszgbackend.model.study.StudyDto
import uk.ac.nott.cs.das.cszgbackend.service.StudyReportService
import uk.ac.nott.cs.das.cszgx.returnOrThrow
import java.util.*

@RestController
@RequestMapping("/reports")
class ReportsController(private val service: StudyReportService) {
    @GetMapping
    suspend fun getAllReports() = service.getAllReports().map { it.map { r -> ReportDto.fromDao(r) } }.returnOrThrow()

    @GetMapping("/{id}")
    suspend fun getReport(@PathVariable id: UUID) = service.getReport(id).map { ReportDto.fromDao(it) }.returnOrThrow()

    @PostMapping
    suspend fun addReport(@RequestBody report: ReportDto) =
        Report.fromDto(report).let { service.addReport(it) }.map { ReportDto.fromDao(it) }.returnOrThrow()

    @PostMapping("/{reportId}/link/{studyId}")
    suspend fun linkReportStudy(@PathVariable reportId: UUID, @PathVariable studyId: UUID) =
        service.associateStudyReport(studyId, reportId)
            .map { (s, r) -> StudyDto.fromDao(s) to ReportDto.fromDao(r) }
            .returnOrThrow()
}
