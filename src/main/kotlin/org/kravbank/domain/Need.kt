package org.kravbank.domain;

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*
import javax.persistence.*

@Entity
class Need : PanacheEntity() {

    var title: String = ""

    var description: String = ""

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-need-project")
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null

    @OneToMany(
        mappedBy = ("need"),
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],//, CascadeType.REMOVE],
        orphanRemoval = true,
    )

    //@JsonIgnore
    @JsonBackReference(value = "val-need-requirement")
    var requirements = mutableListOf<Requirement>()

    override fun toString(): String {
        return "need project to string: ${project?.toString()} \n" +
                "need requirements to string : ${requirements.size}"
    }
}
