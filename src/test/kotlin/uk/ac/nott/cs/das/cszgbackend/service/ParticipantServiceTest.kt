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

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import uk.ac.nott.cs.das.cszgbackend.model.participant.Participant
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantAti
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantAtiRepository
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantBioDto
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantRepository
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantTlx
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
                participants.shouldBeRight {
                    it.count().shouldBe(1)
                }
            }

            it("should return an exception if the repo fails") {
                every { participantRepo.findAll() } throws IOException()

                val participants = service.getAllParticipants()
                participants.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }

        describe("#getParticipant") {
            it("should return the participant if the id exists") {
                val id = UUID.randomUUID()
                val p = Participant(id, "abcd")
                every { participantRepo.findById(id) } returns Optional.of(p)

                val result = service.getParticipant(id)
                result.shouldBeRight {
                    it.id.shouldBe(id)
                }
            }

            it("should return an exception if the id doesn't exist") {
                every { participantRepo.findById(any()) } returns Optional.empty()

                val result = service.getParticipant(UUID.randomUUID())
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.NOT_FOUND)
                }
            }

            it("should return an exception if the repo fails") {
                every { participantRepo.findById(any()) } throws IOException()

                val result = service.getParticipant(UUID.randomUUID())
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }

        describe("#createParticipant") {
            it("should return the participant if creation succeeds") {
                val p = Participant(username = "abcd")
                every { participantRepo.save(p) } returns p

                val result = service.createParticipant(p)
                result.shouldBeRight {
                    it.shouldBe(p)
                }
            }

            it("should return an exception if the repo fails") {
                every { participantRepo.save(any()) } throws IOException()
                val p = Participant(username = "abcd")

                val result = service.createParticipant(p)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
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
                result.shouldBeRight {
                    it.ati.shouldNotBeNull().participant.shouldBe(participant)
                }
            }

            it("should return an exception if the participant ID doesn't exist") {
                every { participantRepo.findById(any()) } returns Optional.empty()

                val result = service.setParticipantAti(id, ati)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.NOT_FOUND)
                }
            }

            it("should return an exception if saving the ATI fails") {
                every { participantRepo.findById(id) } returns Optional.of(participant)
                every { atiRepo.save(any()) } throws IOException()

                val result = service.setParticipantAti(id, ati)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }

            it("should return an exception if saving the participant fails") {
                every { participantRepo.findById(id) } returns Optional.of(participant)
                every { atiRepo.save(any()) } answers { firstArg() }
                every { participantRepo.save(any()) } throws IOException()

                val result = service.setParticipantAti(id, ati)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }

        describe("#setParticipantBio") {
            val id = UUID.randomUUID()
            lateinit var participant: Participant
            lateinit var bio: ParticipantBioDto

            beforeEach {
                participant = Participant(id, "abcd")
                bio = ParticipantBioDto(1, 'f', null)
            }

            it("should return the participant with the bio set if the ID exists") {
                every { participantRepo.findById(id) } returns Optional.of(participant)
                every { participantRepo.save(any()) } answers { firstArg() }

                val result = service.setParticipantBio(id, bio)
                result.shouldBeRight {
                    it.id.shouldBe(id)
                    it.age.shouldBe(bio.age)
                    it.gender.shouldBe(bio.gender)
                    it.genderDescription.shouldBeNull()
                }
            }

            it("should return an exception if the ID doesn't exist") {
                every { participantRepo.findById(id) } returns Optional.empty()
                val result = service.setParticipantBio(id, bio)

                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.NOT_FOUND)
                }
            }

            it("should return an exception if the repo fails when finding the participant") {
                every { participantRepo.findById(any()) } throws IOException()

                val result = service.setParticipantBio(id, bio)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }

            it("should return an exception if the repo fails when saving the participant") {
                every { participantRepo.findById(id) } returns Optional.of(participant)
                every { participantRepo.save(any()) } throws IOException()

                val result = service.setParticipantBio(id, bio)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }

        describe("#setParticipantTlx") {
            val id = UUID.randomUUID()
            lateinit var participant: Participant
            lateinit var tlx: ParticipantTlx

            beforeEach {
                participant = Participant(id, "abcd")
                tlx = ParticipantTlx(
                    taskNo = 1,
                    mentalDemand = 1,
                    physicalDemand = 1,
                    temporalDemand = 1,
                    effort = 1,
                    performance = 1,
                    frustration = 1
                )
            }

            it("should return the participant with TLX set if the ID exists") {
                every { participantRepo.findById(id) } returns Optional.of(participant)
                every { tlxRepo.save(any()) } answers { firstArg() }
                every { participantRepo.save(any()) } answers { firstArg() }

                val result = service.setParticipantTlx(id, tlx)
                result.shouldBeRight {
                    it.tlx.shouldContain(tlx)
                }
            }

            it("should return an exception if the ID doesn't exist") {
                every { participantRepo.findById(any()) } returns Optional.empty()

                val result = service.setParticipantTlx(id, tlx)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.NOT_FOUND)
                }
            }

            it("should return an exception if getting the participant fails") {
                every { participantRepo.findById(any()) } throws IOException()

                val result = service.setParticipantTlx(id, tlx)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }

            it("should return an exception if saving the TLX fails") {
                every { participantRepo.findById(id) } returns Optional.of(participant)
                every { tlxRepo.save(any()) } throws IOException()

                val result = service.setParticipantTlx(id, tlx)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }

            it("should return an exception if saving the participant fails") {
                every { participantRepo.findById(id) } returns Optional.of(participant)
                every { tlxRepo.save(any()) } answers { firstArg() }
                every { participantRepo.save(any()) } throws IOException()

                val result = service.setParticipantTlx(id, tlx)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }
    }
})
