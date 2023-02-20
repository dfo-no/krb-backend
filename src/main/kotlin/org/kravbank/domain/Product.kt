package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
data class Product(

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString(),

    var title: String = "",

    var description: String = "",

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "product")
    @JsonIgnore
    var project: Project? = null,

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "val-reqvariant-product")
    @JsonIgnore
    var requirementvariant: RequirementVariant? = null

) : PanacheEntity() {

    override fun toString(): String {
        return "Product(title='$title', description='$description', ref='$ref', project=$project, requirementvariant=$requirementvariant)"
    }
}