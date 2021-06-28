package uk.ac.nott.cs.das.cszgbackend.pdf

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.pdfbox.pdmodel.PDDocument
import java.util.*

class PdfJsonTextStripperTest : DescribeSpec({
    lateinit var stripper: PdfJsonTextStripper

    beforeEach {
        stripper = PdfJsonTextStripper()
    }

    describe("PdfJsonTextStripper") {
        it("should sort text objects by position") {
            stripper.sortByPosition.shouldBeTrue()
        }

        describe("#getText") {
            lateinit var doc: PDDocument

            beforeEach {
                val data = Base64.getDecoder().decode(PdfTestData.HELLO_WORLD_B64)
                doc = PDDocument.load(data)
            }

            it("should return valid Json with the correct details") {
                val text = stripper.getText(doc)
                val jsonDoc = shouldNotThrowAny { Json.decodeFromString<PdfJsonDocument>(text) }
                jsonDoc.version.shouldBe("1.7")
                jsonDoc.pages.shouldHaveSize(1)

                val page = jsonDoc.pages[0]
                page.width.shouldBe(200.0)
                page.height.shouldBe(200.0)
                page.pageNo.shouldBe(1)
                page.textObjects.shouldHaveSize(1)

                val textObj = page.textObjects[0]
                textObj.text.shouldBe("Hello, world!")
            }

            afterEach {
                doc.close()
            }
        }
    }
})
