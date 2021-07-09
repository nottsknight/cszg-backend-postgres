package weka.classifiers.meta

import weka.classifiers.Classifier
import weka.core.Instances
import weka.core.SerializationHelper
import weka.core.stopwords.Rainbow
import weka.core.tokenizers.StanfordTokenizer
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector
import java.io.File

class PicoSentenceClassifier(baseFile: String, inputFormat: Instances) : FilteredClassifier() {
    init {
        this.classifier = SerializationHelper.read("$baseFile.model") as Classifier
        this.filter = FixedDictionaryStringToWordVector().apply {
            tokenizer = StanfordTokenizer()
            stopwordsHandler = Rainbow()
            dictionaryFile = File("$baseFile.dict")
            setInputFormat(inputFormat)
        }
    }
}
