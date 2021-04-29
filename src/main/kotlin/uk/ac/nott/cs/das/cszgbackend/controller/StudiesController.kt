package uk.ac.nott.cs.das.cszgbackend.controller

import org.springframework.web.bind.annotation.*
import uk.ac.nott.cs.das.cszgbackend.model.study.Study
import uk.ac.nott.cs.das.cszgbackend.service.StudyService
import uk.ac.nott.cs.das.cszgx.returnOrThrow
import java.util.*

@RestController
@RequestMapping("/studies")
class StudiesController(private val service: StudyService) {
    @GetMapping
    suspend fun getAllStudies() = service.getAllStudies().returnOrThrow()

    @GetMapping("/{id}")
    suspend fun getStudy(@PathVariable id: UUID) = service.getStudy(id).returnOrThrow()

    @PostMapping
    suspend fun addStudy(@RequestBody study: Study) = service.addStudy(study).returnOrThrow()

    @PostMapping("/{id}/link")
    suspend fun linkReport(@PathVariable studyId: UUID, @RequestParam(name = "report") reportId: UUID) =
        service.associateStudyReport(studyId, reportId).returnOrThrow()
}
