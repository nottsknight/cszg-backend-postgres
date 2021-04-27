package uk.ac.nott.cs.das.cszgbackend.model.study

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.OneToMany

@Entity
data class Report(
    @Id var id: UUID = UUID.randomUUID(),
    var title: String,
    var pdfData: ByteArray,
    @ManyToMany(mappedBy = "reports")
    var studies: MutableSet<Study>,
    @OneToMany(mappedBy = "report")
    var sentences: MutableSet<Sentence>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Report).id
    }

    override fun hashCode() = id.hashCode()
}

interface ReportRepository : CrudRepository<Report, UUID>
