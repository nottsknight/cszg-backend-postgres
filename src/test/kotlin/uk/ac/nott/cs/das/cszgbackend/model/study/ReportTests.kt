package uk.ac.nott.cs.das.cszgbackend.model.study

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class ReportTests {
    inner class Dummy

    @Nested
    @DisplayName("Report#equals")
    inner class Equals {
        private lateinit var report: Report

        @BeforeEach
        fun setUp() {
            report =
                Report(title = "Test", pdfData = ByteArray(0), studies = mutableSetOf(), sentences = mutableSetOf())
        }

        @Test
        @DisplayName("Should return `true` is both objects are the same instance")
        fun sameInstance() {
            report.shouldBe(report)
        }

        @Test
        @DisplayName("Should return `false` if other object isn't a Report")
        fun differentType() {
            report.shouldNotBe(Dummy())
        }

        @Test
        @DisplayName("Should return `false` if objects have different ids")
        fun differentIds() {
            val other =
                Report(title = "Test", pdfData = ByteArray(0), studies = mutableSetOf(), sentences = mutableSetOf())
            report.shouldNotBe(other)
        }

        @Test
        @DisplayName("Should return `true` if objects have the same id")
        fun sameIds() {
            val other =
                Report(
                    id = report.id,
                    title = "Test",
                    pdfData = ByteArray(0),
                    studies = mutableSetOf(),
                    sentences = mutableSetOf()
                )

            report.shouldBe(other)
        }
    }

    @Nested
    @DisplayName("Report#hashCode")
    inner class HashCode {
        private lateinit var report: Report

        @BeforeEach
        fun setUp() {
            report =
                Report(title = "Test", pdfData = ByteArray(0), studies = mutableSetOf(), sentences = mutableSetOf())
        }

        @Test
        @DisplayName("Should differ from another hashCode if instances are unequal")
        fun unequal() {
            val other =
                Report(title = "Test", pdfData = ByteArray(0), studies = mutableSetOf(), sentences = mutableSetOf())
            report.hashCode().shouldNotBe(other.hashCode())
        }

        @Test
        @DisplayName("Should equal another hashCode if instances are equal")
        fun equal() {
            val other = Report(
                id = report.id,
                title = "Test",
                pdfData = ByteArray(0),
                studies = mutableSetOf(),
                sentences = mutableSetOf()
            )
            report.hashCode().shouldBe(other.hashCode())
        }
    }

    @Nested
    @DisplayName("Report#fromDto")
    inner class FromDto {
        @Test
        @DisplayName("Should correctly copy values from the DTO")
        fun copyValues() {
            val dto =
                ReportDto(id = UUID.randomUUID(), title = "Test", pdfData = "abcdef12", sentences = mutableSetOf())
            val dao = Report.fromDto(dto)

            dao.id.shouldBe(dto.id)
            dao.title.shouldBe(dto.title)
            dao.pdfData.shouldBe(byteArrayOf(105, -73, 29, 121, -3, 118))
            dao.studies.shouldBeEmpty()
            dao.sentences.shouldBe(dto.sentences)
        }

        @Test
        @DisplayName("Should assign a new id if DTO has no id")
        fun assignId() {
            val dto = ReportDto(id = null, title = "Test", pdfData = "abcdef12", sentences = mutableSetOf())
            val dao = Report.fromDto(dto)
            dao.id.shouldNotBeNull()
        }
    }

    @Nested
    @DisplayName("ReportDto#fromDao")
    inner class FromDao {
        @Test
        @DisplayName("Should correctly copy values from DAO")
        fun copyValues() {
            val dao = Report(
                title = "Test",
                pdfData = byteArrayOf(105, -73, 29, 121, -3, 118),
                studies = mutableSetOf(),
                sentences = mutableSetOf()
            )
            val dto = ReportDto.fromDao(dao)

            dto.id.shouldBe(dao.id)
            dto.title.shouldBe(dao.title)
            dto.pdfData.shouldBe("abcdef12")
            dto.sentences.shouldBe(dao.sentences)
        }
    }
}
