package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
class RequirementVariant : PanacheEntity() {

    lateinit var description: String

    lateinit var requirementText: String

    lateinit var instruction: String

    var useProduct: Boolean = false

    var useSpecification: Boolean = false

    var useQualification: Boolean = false

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], fetch = FetchType.LAZY
    )
    var requirement: Requirement? = null

    @OneToMany(
        mappedBy = ("requirementvariant"),
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],
        orphanRemoval = true
    )
    var product: MutableList<Product>? = null
}