package org.kravbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*
import javax.persistence.*

@Entity
class Need:  PanacheEntity() {

    lateinit var title: String

    lateinit var description: String

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    var requirements = mutableListOf<Requirement>();

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        //optional = false,
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value="need")
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null
}
