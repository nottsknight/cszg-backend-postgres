package uk.ac.nott.cs.das.cszgbackend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.ac.nott.cs.das.cszgbackend.model.participant.Participant
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantBioDto
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTlx
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTrust
import uk.ac.nott.cs.das.cszgbackend.service.ParticipantService
import uk.ac.nott.cs.das.cszgx.returnOrThrow
import java.util.*

@RestController
@RequestMapping("/participants")
class ParticipantsController(private val service: ParticipantService) {
    @GetMapping
    suspend fun getAllParticipants() = service.getAllParticipants().returnOrThrow()

    @GetMapping("/{id}")
    suspend fun getParticipant(@PathVariable id: UUID) = service.getParticipant(id).returnOrThrow()

    @PostMapping
    suspend fun addParticipant(@RequestBody participant: Participant) =
        service.createParticipant(participant).returnOrThrow()

    @PostMapping("/{id}/bio")
    suspend fun setBio(@PathVariable id: UUID, @RequestBody bio: ParticipantBioDto) =
        service.setParticipantBio(id, bio).returnOrThrow()

    @PostMapping("/{id}/tlx")
    suspend fun setTlx(@PathVariable id: UUID, @RequestBody tlx: ParticipantTlx) =
        service.setParticipantTlx(id, tlx).returnOrThrow()

    @PostMapping("/{id}/trust")
    suspend fun setTrust(@PathVariable id: UUID, @RequestBody trust: ParticipantTrust) =
        service.setParticipantTrust(id, trust).returnOrThrow()
}
