package weka.core.tokenizers

import edu.stanford.nlp.pipeline.CoreDocument
import edu.stanford.nlp.pipeline.StanfordCoreNLP
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
            .toMutableList()
            .let { ts ->
                var currentNer: String? = null
                val it = ts.iterator()
                while (it.hasNext()) {
                    when (val t = it.next()) {
                        "--PERSON--",
                        "--ORGANIZATION--",
                        "--MONEY--",
                        "--NUMBER--",
                        "--PERCENT--",
                        "--DATE--",
                        "--TIME--",
                        "--DURATION--" -> if (t == currentNer) it.remove() else currentNer = t
                        else -> currentNer = null
                    }
                }
                ts
            }
            .let { ts ->
                tokens = ts
                nextToken = 0
            }
}
