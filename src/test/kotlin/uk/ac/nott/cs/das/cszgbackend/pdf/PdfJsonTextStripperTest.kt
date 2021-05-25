package uk.ac.nott.cs.das.cszgbackend.pdf

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
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

            afterEach {
                doc.close()
            }
        }
    }
})
