package uk.ac.nott.cs.das.cszgbackend.classification

import edu.stanford.nlp.ling.CoreAnnotation
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.Annotator
import java.util.*

class StopwordsAnnotator : Annotator {
    private val stopwords: Set<String>

    init {
        val stopwordsSet = mutableSetOf<String>()
        javaClass.getResourceAsStream("stopwords.txt")?.use { f ->
            Scanner(f).use {
                while (it.hasNextLine()) stopwordsSet.add(it.nextLine())
            }
        }
        stopwords = stopwordsSet
    }

    override fun annotate(annotation: Annotation) =
        annotation[CoreAnnotations.TokensAnnotation::class.java].forEach { t ->
            val text = t[CoreAnnotations.TextAnnotation::class.java]
            t[IsStopwordAnnotation::class.java] = stopwords.contains(text)
        }

    override fun requirementsSatisfied() = mutableSetOf(IsStopwordAnnotation::class.java)

    override fun requires() = mutableSetOf(CoreAnnotations.TokensAnnotation::class.java)
}

class IsStopwordAnnotation(var value: Boolean) : CoreAnnotation<Boolean> {
    override fun getType() = Boolean::class.java
}
