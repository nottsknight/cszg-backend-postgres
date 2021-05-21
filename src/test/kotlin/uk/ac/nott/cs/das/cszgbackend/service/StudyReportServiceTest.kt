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
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.study.FragmentRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.ReportRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.SentenceRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.Study
import uk.ac.nott.cs.das.cszgbackend.model.study.StudyRepository
import uk.ac.nott.cs.das.cszgbackend.pdf.ReportPdfProcessor
import java.io.IOException
import java.util.*

class StudyReportServiceTest : DescribeSpec({
    // mocks
    lateinit var studyRepo: StudyRepository
    lateinit var reportRepo: ReportRepository
    lateinit var sentenceRepo: SentenceRepository
    lateinit var fragmentRepo: FragmentRepository
    lateinit var pdfProcessor: ReportPdfProcessor

    // service
    lateinit var service: StudyReportService

    describe("StudyReportService") {
        beforeEach {
            studyRepo = mockk()
            reportRepo = mockk()
            sentenceRepo = mockk()
            fragmentRepo = mockk()
            pdfProcessor = mockk()
            service = StudyReportServiceImpl(studyRepo, reportRepo, sentenceRepo, fragmentRepo, pdfProcessor)
        }

        describe("#getAllStudies") {
            it("should return a Right value with the studies") {
                every { studyRepo.findAll() } returns listOf()
                val studies = service.getAllStudies()
                studies.shouldBeTypeOf<Either.Right<Iterable<Study>>>()
            }
        }

        describe("#getStudy") {
            it("should return the study if the ID exists") {
                val randomId = UUID.randomUUID()
                val study = Study(id = randomId, title = "Test", reports = mutableSetOf())
                every { studyRepo.findById(randomId) } returns Optional.of(study)

                val studyResult = service.getStudy(randomId)
                studyResult.shouldBeTypeOf<Either.Right<Study>>()

                studyResult.value.id.shouldBe(randomId)
                studyResult.value.title.shouldBe("Test")
            }

            it("should return an exception if the ID doesn't exist") {
                every { studyRepo.findById(any()) } returns Optional.empty()

                val studyResult = service.getStudy(UUID.randomUUID())
                studyResult.shouldBeTypeOf<Either.Left<ResponseStatusException>>()
                studyResult.value.status.shouldBe(HttpStatus.NOT_FOUND)
            }

            it("should return an exception if the repo fails to read") {
                every { studyRepo.findById(any()) } throws IOException()

                val studyResult = service.getStudy(UUID.randomUUID())
                studyResult.shouldBeTypeOf<Either.Left<ResponseStatusException>>()
                studyResult.value.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        describe("#addStudy") {
            lateinit var toSave: Study

            beforeEach {
                toSave = Study(title = "Test", reports = mutableSetOf())
            }

            it("should return the study if the save was successful") {
                every { studyRepo.save(any()) } answers { firstArg() }
                val study = service.addStudy(toSave)
                study.shouldBeTypeOf<Either.Right<Study>>()
                study.value.shouldBe(toSave)
            }

            it("should return an exception if the save failed") {
                every { studyRepo.save(any()) } throws IOException()
                val result = service.addStudy(toSave)
                result.shouldBeTypeOf<Either.Left<ResponseStatusException>>()
                result.value.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }
})
