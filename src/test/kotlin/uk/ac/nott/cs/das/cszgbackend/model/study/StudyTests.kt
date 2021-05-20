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

class StudyTests {
    @Nested
    @DisplayName("Study#equals")
    inner class Equals {
        inner class Dummy

        private lateinit var study: Study

        @BeforeEach
        fun setUp() {
            study = Study(title = "Test", reports = mutableSetOf())
        }

        @Test
        @DisplayName("Should return `true` if both objects are the same instance")
        fun sameInstance() {
            study.shouldBe(study)
        }

        @Test
        @DisplayName("Should return `false` if other object isn't a Study")
        fun otherType() {
            study.shouldNotBe(Dummy())
        }

        @Test
        @DisplayName("Should return `false` if ids don't match")
        fun mismatchedIds() {
            val other = Study(title = "Test", reports = mutableSetOf())
            study.shouldNotBe(other)
        }

        @Test
        @DisplayName("Should return `true` if ids match")
        fun matchedIds() {
            val other = Study(id = study.id, title = "Test", reports = mutableSetOf())
            study.shouldBe(other)
        }
    }

    @Nested
    @DisplayName("Study#hashCode")
    inner class HashCode {
        private lateinit var study: Study

        @BeforeEach
        fun setUp() {
            study = Study(title = "Test", reports = mutableSetOf())
        }

        @Test
        @DisplayName("Should differ from another hashCode if instances are unequal")
        fun unequal() {
            val other = Study(title = "Test", reports = mutableSetOf())
            study.hashCode().shouldNotBe(other.hashCode())
        }

        @Test
        @DisplayName("Should equal another hashCode if instances are equal")
        fun equal() {
            val other = Study(id = study.id, title = "Test", reports = mutableSetOf())
            study.hashCode().shouldBe(other.hashCode())
        }
    }

    @Nested
    @DisplayName("Study#fromDto")
    inner class FromDto {
        @Test
        @DisplayName("Should correctly copy values from the DTO")
        fun copyValues() {
            val dto = StudyDto(UUID.randomUUID(), "Test")
            val dao = Study.fromDto(dto)
            dao.id.shouldBe(dto.id)
            dao.title.shouldBe(dto.title)
            dao.reports.shouldBeEmpty()
        }

        @Test
        @DisplayName("Should assign a new id if DTO has no id")
        fun generateId() {
            val dto = StudyDto(null, "Test")
            val dao = Study.fromDto(dto)
            dao.id.shouldNotBeNull()
        }
    }

    @Nested
    @DisplayName("StudyDto#fromDao")
    inner class FromDao {
        @Test
        @DisplayName("Should correctly copy values from the DAO")
        fun copyValues() {
            val dao = Study(title = "Test", reports = mutableSetOf())
            val dto = StudyDto.fromDao(dao)
            dto.id.shouldNotBeNull().shouldBe(dao.id)
            dto.title.shouldBe(dao.title)
        }
    }
}