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
    fun getAllStudies() = runBlocking {
        service.getAllStudies().map { it.map { s -> StudyDto.fromDao(s) } }.returnOrThrow()
    }

    @GetMapping("/{id}")
    fun getStudy(@PathVariable id: UUID) = runBlocking {
        service.getStudy(id).map { StudyDto.fromDao(it) }.returnOrThrow()
    }

    @PostMapping
    fun addStudy(@RequestBody study: StudyDto) = runBlocking {
        Study.fromDto(study).let { service.addStudy(it) }.map { StudyDto.fromDao(it) }.returnOrThrow()
    }

    @PostMapping("/{studyId}/link/{reportId}")
    fun linkReport(@PathVariable studyId: UUID, @PathVariable reportId: UUID) = runBlocking {
        service.associateStudyReport(studyId, reportId)
            .map { (s, r) -> StudyDto.fromDao(s) to ReportDto.fromDao(r) }
            .returnOrThrow()
    }
}
