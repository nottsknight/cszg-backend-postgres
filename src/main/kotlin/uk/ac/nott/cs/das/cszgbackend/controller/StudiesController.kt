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

import org.springframework.web.bind.annotation.*
import uk.ac.nott.cs.das.cszgbackend.model.study.ReportDto
import uk.ac.nott.cs.das.cszgbackend.model.study.Study
import uk.ac.nott.cs.das.cszgbackend.model.study.StudyDto
import uk.ac.nott.cs.das.cszgbackend.service.StudyReportService
import uk.ac.nott.cs.das.cszgx.returnOrThrow
import java.util.*

@RestController
@RequestMapping("/studies")
class StudiesController(private val service: StudyReportService) {
    @GetMapping
    suspend fun getAllStudies() = service.getAllStudies().map { it.map { s -> StudyDto.fromDao(s) } }.returnOrThrow()

    @GetMapping("/{id}")
    suspend fun getStudy(@PathVariable id: UUID) = service.getStudy(id).map { StudyDto.fromDao(it) }.returnOrThrow()

    @PostMapping
    suspend fun addStudy(@RequestBody study: StudyDto) =
        Study.fromDto(study).let { service.addStudy(it) }.map { StudyDto.fromDao(it) }.returnOrThrow()

    @PostMapping("/{studyId}/link/{reportId}")
    suspend fun linkReport(@PathVariable studyId: UUID, @PathVariable reportId: UUID) =
        service.associateStudyReport(studyId, reportId)
            .map { (s, r) -> StudyDto.fromDao(s) to ReportDto.fromDao(r) }
            .returnOrThrow()
}
