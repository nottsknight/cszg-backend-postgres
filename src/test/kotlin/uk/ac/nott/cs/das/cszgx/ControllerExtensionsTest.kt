package uk.ac.nott.cs.das.cszgx

import arrow.core.Either
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ControllerExtensionsTest : DescribeSpec({
    describe("Either#returnOrThrow") {
        it("should return a value if called on a Right") {
            val e = Either.Right(1)
            e.returnOrThrow().shouldBe(1)
        }

        it("should throw an exception if called on a Left") {
            val e = Either.Left(RuntimeException())
            shouldThrow<RuntimeException> { e.returnOrThrow() }
        }
    }
})