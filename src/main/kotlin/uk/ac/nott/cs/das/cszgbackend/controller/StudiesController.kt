package uk.ac.nott.cs.das.cszgbackend.controller

import arrow.core.Either
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.ac.nott.cs.das.cszgbackend.service.StudyService
import java.util.*

@RestController
@RequestMapping("/studies")
class StudiesController(private val service: StudyService) {
    @GetMapping
    suspend fun getAllStudies() = when (val res = service.getAllStudies()) {
        is Either.Left -> throw res.value
        is Either.Right -> res.value
    }

    @GetMapping("/{id}")
    suspend fun getStudy(@PathVariable id: UUID) = when (val res = service.getStudy(id)) {
        is Either.Left -> throw res.value
        is Either.Right -> res.value
    }
}
