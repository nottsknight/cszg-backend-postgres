package uk.ac.nott.cs.das.cszgbackend.model.study

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Fragment(
    @Id var id: UUID,
    var pageNo: Int,
    var x1: Double,
    var y1: Double,
    var x2: Double,
    var y2: Double,
    @ManyToOne var sentence: Sentence
)

interface FragmentRepository : CrudRepository<Fragment, UUID>
