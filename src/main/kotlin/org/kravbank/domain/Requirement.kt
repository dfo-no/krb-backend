package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
data class Requirement(

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString(),

    var title: String = "",

    var description: String = "",

    @OneToMany(
        mappedBy = ("requirement"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @JsonIgnore
    var requirementvariants: MutableList<RequirementVariant>? = null,

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.EAGER,
    )
    @JsonManagedReference(value = "val-requirement")
    @JsonIgnore
    var project: Project? = null,

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-need-requirement")
    @JsonIgnore
    var need: Need? = null

) : PanacheEntity() {

    override fun toString(): String {
        return "Requirement(ref='$ref', title='$title', description='$description', requirementvariants=$requirementvariants, project=$project, need=$need)"
    }
}
