package uk.ac.nott.cs.das.cszgbackend.classification

import edu.stanford.nlp.pipeline.CoreDocument
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import weka.core.tokenizers.Tokenizer
import java.util.*

class StanfordTokenizer : Tokenizer() {
    private val nlpPipeline: StanfordCoreNLP
    private var tokens: List<String>? = null
    private var nextToken = 0

    init {
        val props = Properties()
        javaClass.getResourceAsStream("corenlp.properties").use { props.load(it) }
        nlpPipeline = StanfordCoreNLP(props)
    }

    override fun hasMoreElements() = tokens != null && nextToken < tokens!!.size

    override fun nextElement() = tokens?.get(nextToken++) ?: throw NoSuchElementException()

    override fun getRevision() = "1.0.0"

    override fun globalInfo() = "A tokenizer that uses a Stanford CoreNLP pipeline to tokenize text"

    override fun tokenize(s: String) =
        CoreDocument(s)
            .apply { nlpPipeline.annotate(this) }
            .tokens()
            .asSequence()
            .mapNotNull { t -> if (t[IsStopwordAnnotation::class.java]) null else t }
            .map { t ->
                when (t.ner()) {
                    "PERSON",
                    "ORGANIZATION",
                    "MONEY",
                    "NUMBER",
                    "PERCENT",
                    "DATE",
                    "TIME",
                    "DURATION" -> "--${t.ner()}--"
                    else -> t.lemma()
                }
            }
            .toList()
            .let { ts ->
                tokens = ts
                nextToken = 0
            }
}
