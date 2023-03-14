package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class RequirementVariant : PanacheEntity() {

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    lateinit var description: String

    lateinit var requirementText: String

    lateinit var instruction: String

    var useProduct: Boolean = false

    var useSpecification: Boolean = false

    var useQualification: Boolean = false


    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "val-requirementVariant")
    @JsonIgnore
    var requirement: Requirement? = null

    @ManyToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],
    )
    @JsonBackReference(value = "val-reqvariant-product")
    var products: MutableList<Product> = mutableListOf()

}