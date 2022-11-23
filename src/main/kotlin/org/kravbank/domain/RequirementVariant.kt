package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
class RequirementVariant : PanacheEntity() {

    lateinit var description: String

    lateinit var requirementText: String

    lateinit var instruction: String

    var useProduct: Boolean = false

    var useSpesification: Boolean = false

    var useQualification: Boolean = false

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "val-requirementVariant")
    @JsonIgnore
    @JoinColumn(name = "requirement_id_fk")
    var requirement: Requirement? = null

    @OneToMany(
        mappedBy = ("requirementvariant"),
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],
        orphanRemoval = true
    )
    @JsonBackReference(value = "val-reqvariant-product")
    var product: MutableList<Product>? = null
}