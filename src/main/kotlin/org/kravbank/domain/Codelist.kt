package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
class Codelist : PanacheEntity() {
    var title: String = ""

    var description: String = ""

    @Column(unique = true)
    var ref: String = ""

    @OneToMany(
        mappedBy = ("codelist"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnorepo
    @JsonBackReference(value = "value-codes")
    var codes: MutableList<Code>? = null


    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        //optional = false,
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-codelist")
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null
}