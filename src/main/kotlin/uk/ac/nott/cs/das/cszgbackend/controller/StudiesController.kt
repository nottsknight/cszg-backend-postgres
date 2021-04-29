package uk.ac.nott.cs.das.cszgbackend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
}
