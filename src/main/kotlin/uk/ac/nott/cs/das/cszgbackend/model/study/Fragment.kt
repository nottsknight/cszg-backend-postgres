package uk.ac.nott.cs.das.cszgbackend.model.study

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "fragment")
data class Fragment(
    @Id var id: UUID = UUID.randomUUID(),
    var pageNo: Int,
    var x1: Double,
    var y1: Double,
    var x2: Double,
    var y2: Double,
    @ManyToOne var sentence: Sentence
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Fragment).id
    }

    override fun hashCode() = id.hashCode()
}

interface FragmentRepository : CrudRepository<Fragment, UUID>
