package uk.ac.nott.cs.das.cszgbackend.pdf

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.iterator.shouldHaveNext
import io.kotest.matchers.iterator.shouldNotHaveNext
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class PdfJsonModelTest : DescribeSpec({
    describe("PdfJsonDocument") {
        it("correctly exposes an iterator through the pages") {
            val doc = PdfJsonDocument(
                version = "1.0",
                pages = mutableListOf(
                    PdfJsonPage(1, 1.0, 1.0, mutableListOf()),
                    PdfJsonPage(2, 1.0, 1.0, mutableListOf())
                )
            )

            val it = doc.iterator()
            it.shouldHaveNext()
            it.next().should {
                it.pageNo.shouldBe(1)
                it.width.shouldBe(1.0)
                it.height.shouldBe(1.0)
                it.textObjects.shouldBeEmpty()
            }
            it.shouldHaveNext()
            it.next().should {
                it.pageNo.shouldBe(2)
                it.width.shouldBe(1.0)
                it.height.shouldBe(1.0)
                it.textObjects.shouldBeEmpty()
            }
            it.shouldNotHaveNext()
        }
    }

    describe("PdfJsonPage") {
        it("correctly exposes an iterator through the text objects") {
            val to1 = PdfJsonTextObject("First", "", 1.0, 1.0, 1.0, 1.0, 1.0)
            val to2 = PdfJsonTextObject("Second", "", 1.0, 1.0, 1.0, 1.0, 1.0)
            val page = PdfJsonPage(1, 1.0, 1.0, mutableListOf(to1, to2))

            val it = page.iterator()
            it.shouldHaveNext()
            it.next().should {
                it.text.shouldBe("First")
            }
            it.shouldHaveNext()
            it.next().should {
                it.text.shouldBe("Second")
            }
            it.shouldNotHaveNext()
        }
    }

    describe("PdfJsonTextObject") {
        describe("#compareTo") {
            it("should order objects in the same column by Y position") {
                val to1 = PdfJsonTextObject("", "", 1.0, 1.0, 1.0, 2.0, 2.0)
                val to2 = PdfJsonTextObject("", "", 1.0, 1.0, 5.0, 2.0, 6.0)

                val cmp1 = to1.compareTo(to2)
                cmp1.shouldBeLessThan(0)
                val cmp2 = to2.compareTo(to1)
                cmp2.shouldBeGreaterThan(0)
            }

            it("should order objects in different columns by X position") {
                val to1 = PdfJsonTextObject("", "", 1.0, 7.0, 1.0, 8.0, 2.0)
                val to2 = PdfJsonTextObject("", "", 1.0, 1.0, 5.0, 2.0, 6.0)

                val cmp1 = to1.compareTo(to2)
                cmp1.shouldBeGreaterThan(0)
                val cmp2 = to2.compareTo(to1)
                cmp2.shouldBeLessThan(0)
            }
        }

        describe("#distanceTo") {
            it("should correctly calculate the gap") {
                val to1 = PdfJsonTextObject("", "", 1.0, 1.0, 1.0, 2.0, 2.0)
                val to2 = PdfJsonTextObject("", "", 1.0, 3.0, 5.0, 4.0, 6.0)

                val dist1 = to1 distanceTo to2
                dist1.shouldBe(1.0)
                val dist2 = to2 distanceTo to1
                dist2.shouldBe(1.0)
            }
        }
    }
})
