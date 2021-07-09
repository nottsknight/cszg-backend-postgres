package uk.ac.nott.cs.das.cszgbackend.classification

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import org.springframework.stereotype.Component
import uk.ac.nott.cs.das.cszgbackend.model.study.Sentence
import weka.classifiers.meta.PicoSentenceClassifier
import weka.core.Attribute
import weka.core.DenseInstance
import weka.core.Instances

@Component
class PdfSentencesClassifier {
    suspend fun classifySentences(sentences: Sequence<Sentence>) {
        val datasets = generateDatasets(sentences)
        val classifiers = CLASS_NAMES.mapIndexed { i, c -> PicoSentenceClassifier(c, datasets[i]) }

        for (i in (0..10)) {
            datasets[i].asFlow().collect { inst -> classifiers[i].classifyInstance(inst) }
        }
    }

    private fun generateDatasets(sentences: Sequence<Sentence>): Array<Instances> {
        val numSentences = sentences.count()

        // create datasets
        val datasets =
            Array(11) { i ->
                Instances(
                    "class-$i",
                    arrayListOf(ATTR_TEXT, ATTRIBUTES[i + 1]),
                    numSentences
                ).apply { setClassIndex(1) }
            }

        // add sentences
        sentences
            .map { s -> Array(11) { DenseInstance(2).apply { setValue(ATTR_TEXT, s.content) } } }
            .forEach { insts ->
                for (i in (0..10)) {
                    insts[i].setDataset(datasets[i])
                    datasets[i].add(insts[i])
                }
            }

        return datasets
    }

    companion object {
        private val CLASS_NAMES = arrayOf(
            "allocation",
            "blinding",
            "duration",
            "funding",
            "diagnosis",
            "number",
            "age",
            "sex",
            "history",
            "intervention",
            "outcome"
        )

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
