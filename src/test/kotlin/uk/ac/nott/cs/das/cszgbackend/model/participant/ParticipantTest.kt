package uk.ac.nott.cs.das.cszgbackend.model.participant

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

private class Dummy

class ParticipantTest : DescribeSpec({
    lateinit var p1: Participant

    beforeEach {
        p1 = Participant(username = "abcd")
    }

    describe("Participant") {
        describe("#equals") {
            it("should return true if both objects are the same instance") {
                p1 shouldBe p1
            }

            it("should return false if other object is null") {
                p1 shouldNotBe null
            }

            it("should return false of other object has different type") {
                p1 shouldNotBe Dummy()
            }

            it("should return false if other object has a different id") {
                val p2 = Participant(username = "abcd")
                p1 shouldNotBe p2
            }

            it("should return true if other object has the same id") {
                val p2 = Participant(id = p1.id, username = "efgh")
                p1 shouldBe p2
            }
        }

        describe("#hashCode") {
            it("should return different values for objects with different ids") {
                val p2 = Participant(username = "abcd")
                p1.hashCode() shouldNotBe p2
            }

            it("should return the same value for two objects with the same id") {
                val p2 = Participant(id = p1.id, username = "efgh")
                p1.hashCode() shouldBe p2.hashCode()
            }
        }
    }

    describe("ParticipantDto") {
        describe("#fromDao") {
            it("should set the values correctly from the DAO") {
                val dto = ParticipantDto.fromDao(p1)
                dto.id shouldBe p1.id
                dto.username shouldBe p1.username
                dto.ati shouldBe p1.ati
                dto.tlx shouldBe p1.tlx
                dto.trust shouldBe p1.trust
            }
        }
    }
})
