package uk.ac.nott.cs.das.cszgbackend.classification

import weka.core.Capabilities
import weka.core.Instances
import weka.filters.SimpleBatchFilter

class StanfordStringToWordVector : SimpleBatchFilter() {
    override fun globalInfo() = "Vectorizes string attributes using a Stanford CoreNLP pipeline"

    override fun getCapabilities(): Capabilities = super.getCapabilities().apply {
        enable(Capabilities.Capability.STRING_ATTRIBUTES)
        enableAllClasses()
        enable(Capabilities.Capability.NO_CLASS)
    }

    override fun determineOutputFormat(inputFormat: Instances?): Instances {
        TODO("Not yet implemented")
    }

    override fun process(instances: Instances?): Instances {
        TODO("Not yet implemented")
    }
}
