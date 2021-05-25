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
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.participant.Participant
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantAti
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantAtiRepository
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantRepository
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTlxRepository
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTrustRepository
import java.io.IOException
import java.util.*

class ParticipantServiceTest : DescribeSpec({
    describe("ParticipantService") {
        lateinit var participantRepo: ParticipantRepository
        lateinit var atiRepo: ParticipantAtiRepository
        lateinit var tlxRepo: ParticipantTlxRepository
        lateinit var trustRepo: ParticipantTrustRepository
        lateinit var service: ParticipantService

        beforeEach {
            participantRepo = mockk()
            atiRepo = mockk()
            tlxRepo = mockk()
            trustRepo = mockk()
            service = ParticipantServiceImpl(participantRepo, atiRepo, tlxRepo, trustRepo)
        }

        describe("#getAllParticipants") {
            it("should return an iterable of Participants") {
                every { participantRepo.findAll() } returns listOf(Participant(username = "abcd"))
                val participants = service.getAllParticipants()
                participants.shouldBeTypeOf<Either.Right<Iterable<Participant>>>()
                participants.value.count().shouldBe(1)
            }

            it("should return an exception if the repo fails") {
                every { participantRepo.findAll() } throws IOException()
                val participants = service.getAllParticipants()
                participants.shouldBeTypeOf<Either.Left<ResponseStatusException>>()
                participants.value.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        describe("#getParticipant") {
            it("should return the participant if the id exists") {
                val id = UUID.randomUUID()
                val p = Participant(id, "abcd")
                every { participantRepo.findById(id) } returns Optional.of(p)

                val result = service.getParticipant(id)
                result.shouldBeTypeOf<Either.Right<Participant>>()
                result.value.id.shouldBe(id)
            }

            it("should return an exception if the id doesn't exist") {
                every { participantRepo.findById(any()) } returns Optional.empty()
                val result = service.getParticipant(UUID.randomUUID())
                result.shouldBeTypeOf<Either.Left<ResponseStatusException>>()
                result.value.status.shouldBe(HttpStatus.NOT_FOUND)
            }

            it("should return an exception if the repo fails") {
                every { participantRepo.findById(any()) } throws IOException()
                val result = service.getParticipant(UUID.randomUUID())
                result.shouldBeTypeOf<Either.Left<ResponseStatusException>>()
                result.value.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        describe("#createParticipant") {
            it("should return the participant if creation succeeds") {
                val p = Participant(username = "abcd")
                every { participantRepo.save(p) } returns p
                val result = service.createParticipant(p)
                result.shouldBeTypeOf<Either.Right<Participant>>()
                result.value.shouldBe(p)
            }

            it("should return an exception if the repo fails") {
                every { participantRepo.save(any()) } throws IOException()
                val p = Participant(username = "abcd")
                val result = service.createParticipant(p)
                result.shouldBeTypeOf<Either.Left<ResponseStatusException>>()
                result.value.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        describe("#setParticipantAti") {
            val id = UUID.randomUUID()
            lateinit var participant: Participant
            lateinit var ati: ParticipantAti

            beforeEach {
                participant = Participant(id, "abcd")
                ati = ParticipantAti(
                    response1 = 1,
                    response2 = 1,
                    response3 = 1,
                    response4 = 1,
                    response5 = 1,
                    response6 = 1,
                    response7 = 1,
                    response8 = 1,
                    response9 = 1
                )
            }

            it("should return the participant with the ATI set if all succeeds") {
                every { participantRepo.findById(id) } returns Optional.of(participant)
                every { participantRepo.save(any()) } answers { firstArg() }
                every { atiRepo.save(any()) } answers { firstArg() }

                val result = service.setParticipantAti(id, ati)
                result.shouldBeTypeOf<Either.Right<Participant>>()
                result.value.let { res ->
                    res.ati.shouldNotBeNull()
                    res.ati!!.participant.shouldBe(participant)
                }
            }

            it("should return an exception if the participant ID doesn't exist") {
                every { participantRepo.findById(any()) } returns Optional.empty()
                val result = service.setParticipantAti(id, ati)
                result.shouldBeTypeOf<Either.Left<ResponseStatusException>>()
                result.value.status.shouldBe(HttpStatus.NOT_FOUND)
            }
        }
    }
})
