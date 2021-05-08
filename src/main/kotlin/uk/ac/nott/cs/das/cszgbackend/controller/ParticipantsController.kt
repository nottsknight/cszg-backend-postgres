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
