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
class Requirement : PanacheEntity(), Serializable {

    var title: String = ""

    var description: String = ""

    @OneToMany(
        mappedBy = ("requirement"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value = "val-requirementVariant")
    var requirementvariants = mutableListOf<RequirementVariant>()

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.EAGER,
    )
    @JsonManagedReference(value = "val-requirement")
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-need-requirement")
    @JsonIgnore
    @JoinColumn(name = "need_id_fk")
    var need: Need? = null
}
