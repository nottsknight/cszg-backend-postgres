package uk.ac.nott.cs.das.cszgbackend.pdf

import edu.stanford.nlp.pipeline.AnnotationPipeline
import edu.stanford.nlp.pipeline.POSTaggerAnnotator
import edu.stanford.nlp.pipeline.TokenizerAnnotator
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PdfTextConfiguration {
    @Bean
    fun getNlpPipeline() = AnnotationPipeline().apply {
        addAnnotator(TokenizerAnnotator(false))
        addAnnotator(WordsToSentencesAnnotator(false))
        addAnnotator(POSTaggerAnnotator(false))
    }
}
