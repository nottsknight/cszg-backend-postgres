package uk.ac.nott.cs.das.cszgbackend.service

import arrow.core.Either
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.participant.Participant
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantAti
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTlx
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTrust
import java.util.*

interface ParticipantService {
    suspend fun getAllParticipants(): Either<ResponseStatusException, Iterable<Participant>>
    suspend fun getParticipant(id: UUID): Either<ResponseStatusException, Participant>
    suspend fun createParticipant(p: Participant): Either<ResponseStatusException, Participant>
    suspend fun setParticipantAti(id: UUID, ati: ParticipantAti): Either<ResponseStatusException, Participant>
    suspend fun setParticipantTlx(id: UUID, tlx: ParticipantTlx): Either<ResponseStatusException, Participant>
    suspend fun setParticipantTrust(id: UUID, trust: ParticipantTrust): Either<ResponseStatusException, Participant>
}

@Service
class ParticipantServiceImpl : ParticipantService {
    override suspend fun getAllParticipants(): Either<ResponseStatusException, Iterable<Participant>> {
        TODO("Not yet implemented")
    }

    override suspend fun getParticipant(id: UUID): Either<ResponseStatusException, Participant> {
        TODO("Not yet implemented")
    }

    override suspend fun createParticipant(p: Participant): Either<ResponseStatusException, Participant> {
        TODO("Not yet implemented")
    }

    override suspend fun setParticipantAti(
        id: UUID,
        ati: ParticipantAti
    ): Either<ResponseStatusException, Participant> {
        TODO("Not yet implemented")
    }

    override suspend fun setParticipantTlx(
        id: UUID,
        tlx: ParticipantTlx
    ): Either<ResponseStatusException, Participant> {
        TODO("Not yet implemented")
    }

    override suspend fun setParticipantTrust(
        id: UUID,
        trust: ParticipantTrust
    ): Either<ResponseStatusException, Participant> {
        TODO("Not yet implemented")
    }

}
