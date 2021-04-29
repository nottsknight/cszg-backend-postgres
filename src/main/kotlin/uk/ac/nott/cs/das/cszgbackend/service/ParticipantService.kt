package uk.ac.nott.cs.das.cszgbackend.service

import arrow.core.Either
import arrow.core.computations.either
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.participant.*
import uk.ac.nott.cs.das.cszgbackend.modelx.findAllFx
import uk.ac.nott.cs.das.cszgbackend.modelx.findByIdFx
import uk.ac.nott.cs.das.cszgbackend.modelx.saveFx
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
