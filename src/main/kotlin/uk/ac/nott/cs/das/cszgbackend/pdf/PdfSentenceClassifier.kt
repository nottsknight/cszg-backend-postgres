package uk.ac.nott.cs.das.cszgbackend.pdf

import org.springframework.stereotype.Component
import uk.ac.nott.cs.das.cszgbackend.model.study.Sentence
import weka.classifiers.Classifier
import weka.core.Attribute
import weka.core.DenseInstance
import weka.core.Instances

@Component
class PdfSentenceClassifier {
    private lateinit var classifier: Classifier

    suspend fun classifySentences(sentences: Iterable<Sentence>) {
        val instances = Instances("sentence-data", ATTRIBUTES, sentences.count())
        sentences
            .map { s ->
                val inst = DenseInstance(12)
                inst.setValue(ATTR_TEXT, s.content)
                inst
            }.forEach { inst ->
                inst.setDataset(instances)
                instances.add(inst)
            }
    }

    companion object {
        private val ATTR_TEXT = Attribute("text", true)
        private val ATTR_ALLOCATION = Attribute("allocation")
        private val ATTR_BLINDING = Attribute("blinding")
        private val ATTR_DURATION = Attribute("duration")
        private val ATTR_FUNDING = Attribute("funding")
        private val ATTR_DIAGNOSIS = Attribute("diagnosis")
        private val ATTR_NUMBER = Attribute("number")
        private val ATTR_AGE = Attribute("age")
        private val ATTR_SEX = Attribute("sex")
        private val ATTR_HISTORY = Attribute("history")
        private val ATTR_INTERVENTION = Attribute("intervention")
        private val ATTR_OUTCOME = Attribute("outcome")

        private val ATTRIBUTES = arrayListOf(
            ATTR_TEXT,
            ATTR_ALLOCATION,
            ATTR_BLINDING,
            ATTR_DURATION,
            ATTR_FUNDING,
            ATTR_DIAGNOSIS,
            ATTR_NUMBER,
            ATTR_AGE,
            ATTR_SEX,
            ATTR_HISTORY,
            ATTR_INTERVENTION,
            ATTR_OUTCOME
        )
    }
}
