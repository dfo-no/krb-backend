package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
data class Need(

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString(),

    var title: String = "",

    var description: String = "",

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-need-project")
    @JsonIgnore
    var project: Project? = null,

    @OneToMany(
        mappedBy = ("need"),
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],
        orphanRemoval = true
    )
    @JsonBackReference(value = "val-need-requirement")
    var requirements: MutableList<Requirement>? = null

) : PanacheEntity() {

    override fun toString(): String {
        return "Need(ref='$ref', title='$title', description='$description', project=$project, requirements=$requirements)"
    }
}
