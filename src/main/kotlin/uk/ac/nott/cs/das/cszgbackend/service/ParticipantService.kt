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
package uk.ac.nott.cs.das.cszgbackend.service

import arrow.core.Either
import arrow.core.computations.either
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.participant.Participant
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantAti
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantAtiRepository
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantBioDto
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantRepository
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTlx
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTlxRepository
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTrust
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTrustRepository
import uk.ac.nott.cs.das.cszgx.findAllFx
import uk.ac.nott.cs.das.cszgx.findByIdFx
import uk.ac.nott.cs.das.cszgx.saveFx
import java.util.*

interface ParticipantService {
    suspend fun getAllParticipants(): Either<ResponseStatusException, Iterable<Participant>>
    suspend fun getParticipant(id: UUID): Either<ResponseStatusException, Participant>
    suspend fun createParticipant(p: Participant): Either<ResponseStatusException, Participant>
    suspend fun setParticipantBio(id: UUID, bio: ParticipantBioDto): Either<ResponseStatusException, Participant>
    suspend fun setParticipantAti(id: UUID, ati: ParticipantAti): Either<ResponseStatusException, Participant>
    suspend fun setParticipantTlx(id: UUID, tlx: ParticipantTlx): Either<ResponseStatusException, Participant>
    suspend fun setParticipantTrust(id: UUID, trust: ParticipantTrust): Either<ResponseStatusException, Participant>
}

@Service
class ParticipantServiceImpl(
    private val participantRepo: ParticipantRepository,
    private val atiRepo: ParticipantAtiRepository,
    private val tlxRepo: ParticipantTlxRepository,
    private val trustRepo: ParticipantTrustRepository
) : ParticipantService {

    override suspend fun getAllParticipants() = participantRepo.findAllFx()

    override suspend fun getParticipant(id: UUID) = participantRepo.findByIdFx(id)

    override suspend fun createParticipant(p: Participant) = participantRepo.saveFx(p)

    override suspend fun setParticipantAti(
        id: UUID,
        ati: ParticipantAti
    ) = either<ResponseStatusException, Participant> {
        val p = participantRepo.findByIdFx(id).bind()
        ati.participant = p
        val savedAti = atiRepo.saveFx(ati).bind()
        p.ati = savedAti
        participantRepo.saveFx(p).bind()
    }

    override suspend fun setParticipantBio(id: UUID, bio: ParticipantBioDto) =
        either<ResponseStatusException, Participant> {
            val p = participantRepo.findByIdFx(id).bind()
            p.age = bio.age
            p.gender = bio.gender
            p.genderDescription = bio.genderDescription
            participantRepo.saveFx(p).bind()
        }

    override suspend fun setParticipantTlx(
        id: UUID,
        tlx: ParticipantTlx
    ) = either<ResponseStatusException, Participant> {
        val p = participantRepo.findByIdFx(id).bind()
        tlx.participant = p
        val savedTlx = tlxRepo.saveFx(tlx).bind()
        p.tlx.add(savedTlx)
        participantRepo.saveFx(p).bind()
    }

    override suspend fun setParticipantTrust(
        id: UUID,
        trust: ParticipantTrust
    ) = either<ResponseStatusException, Participant> {
        val p = participantRepo.findByIdFx(id).bind()
        trust.participant = p
        val savedTrust = trustRepo.saveFx(trust).bind()
        p.trust.add(savedTrust)
        participantRepo.saveFx(p).bind()
    }
}
