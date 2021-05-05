package uk.ac.nott.cs.das.cszgbackend.controller

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.participant.Participant
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantRepository
import java.security.Principal

@RestController
class LoginController(private val participantRepo: ParticipantRepository) {
    @GetMapping("/login")
    fun login(@AuthenticationPrincipal principal: Principal): Participant =
        participantRepo.findByUsername(principal.name).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
}
