package uk.ac.nott.cs.das.cszgbackend.pdf

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull

class PdfTextConfigurationTest : DescribeSpec({
    describe("PdfTextConfiguration") {
        it("#getNlpPipeline should return a pipeline with the appropriate annotators") {
            val pipeline = PdfTextConfiguration().getNlpPipeline()

            Annotation("test").apply { pipeline.annotate(this) }.let {
                it[CoreAnnotations.TokensAnnotation::class.java].shouldNotBeNull().let { tokens ->
                    tokens[0][CoreAnnotations.PartOfSpeechAnnotation::class.java].shouldNotBeNull()
                }
                it[CoreAnnotations.SentencesAnnotation::class.java].shouldNotBeNull()
            }
        }
    }
})
