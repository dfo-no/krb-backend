package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
class RequirementVariant(

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString(),

    var description: String = "",

    var requirementText: String = "",

    var instruction: String = "",

    var useProduct: Boolean = false,

    var useSpecification: Boolean = false,

    var useQualification: Boolean = false,

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "val-requirementVariant")
    @JsonIgnore
    var requirement: Requirement? = null,

    @OneToMany(
        mappedBy = ("requirementvariant"),
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],
        orphanRemoval = true
    )
    @JsonBackReference(value = "val-reqvariant-product")
    var product: MutableList<Product>? = null,

    ) : PanacheEntity() {

    override fun toString(): String {
        return "RequirementVariant(ref='$ref', description='$description', requirementText='$requirementText', instruction='$instruction', useProduct=$useProduct, useSpecification=$useSpecification, useQualification=$useQualification, requirement=$requirement, product=$product)"
    }
}

