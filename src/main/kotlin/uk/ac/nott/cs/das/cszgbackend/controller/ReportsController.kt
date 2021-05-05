package uk.ac.nott.cs.das.cszgbackend.controller

import org.springframework.web.bind.annotation.*
import uk.ac.nott.cs.das.cszgbackend.model.study.Report
import uk.ac.nott.cs.das.cszgbackend.service.StudyReportService
import uk.ac.nott.cs.das.cszgx.returnOrThrow
import java.util.*

@RestController
@RequestMapping("/reports")
class ReportsController(private val service: StudyReportService) {
    @GetMapping
    suspend fun getAllReports() = service.getAllReports().returnOrThrow()

    @GetMapping("/{id}")
    suspend fun getReport(@PathVariable id: UUID) = service.getReport(id).returnOrThrow()

    @PostMapping("/reports")
    suspend fun addReport(@RequestBody report: Report) = service.addReport(report).returnOrThrow()
}
