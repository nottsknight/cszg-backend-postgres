/**
 * This file is part of the CSzG backend.
 *
 * The CSzG backend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The CSzG backend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see https://www.gnu.org/licenses/.
 */
package uk.ac.nott.cs.das.cszgbackend.controller

import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    fun getAllReports() = runBlocking {
        service.getAllReports().map { it.map { r -> ReportDto.fromDao(r) } }.returnOrThrow()
    }

    @GetMapping("/{id}")
    fun getReport(@PathVariable id: UUID) = runBlocking {
        service.getReport(id).map { ReportDto.fromDao(it) }.returnOrThrow()
    }

    @PostMapping
    fun addReport(@RequestBody report: ReportDto) = runBlocking {
        Report.fromDto(report).let { service.addReport(it) }.map { ReportDto.fromDao(it) }.returnOrThrow()
    }

    @PostMapping("/{reportId}/link/{studyId}")
    fun linkReportStudy(@PathVariable reportId: UUID, @PathVariable studyId: UUID) = runBlocking {
        service.associateStudyReport(studyId, reportId)
            .map { (s, r) -> StudyDto.fromDao(s) to ReportDto.fromDao(r) }
            .returnOrThrow()
    }
}
