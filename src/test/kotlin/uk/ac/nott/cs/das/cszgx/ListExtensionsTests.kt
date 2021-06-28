package uk.ac.nott.cs.das.cszgx

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.sequences.shouldBeEmpty
import io.kotest.matchers.sequences.shouldContain
import io.kotest.matchers.sequences.shouldHaveSize
import io.kotest.matchers.shouldBe

class ListExtensionsTests : DescribeSpec({
    describe("List#pairs") {
        it("should return an empty sequence from an empty list") {
            val xs = listOf<Int>()
            xs.pairs().shouldBeEmpty()
        }

        it("should return an empty sequence from a singleton list") {
            val xs = listOf(1)
            xs.pairs().shouldBeEmpty()
        }

        it("should return all the pairs from a non-singleton list") {
            val xs = listOf(1, 2, 3)
            xs.pairs().let {
                it.shouldHaveSize(2)
                it.shouldContain(1 to 2)
                it.shouldContain(2 to 3)
            }
        }
    }

    describe("List#mode") {
        it("should return null from an empty list") {
            val xs = listOf<Int>()
            xs.mode().shouldBeNull()
        }

        it("should return the modal value from a list with a unique mode") {
            val xs = listOf(1,2,2,3,4)
            xs.mode().shouldBe(2)
        }

        it("should return the first encountered modal value from a list with multiple modes") {
            val xs = listOf(1,2,2,3,3,4)
            xs.mode().shouldBe(2)
        }
    }
})
