package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import lombok.NoArgsConstructor
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@NoArgsConstructor
class Need : PanacheEntity(), Serializable {

    lateinit var title: String

    lateinit var description: String

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

}
