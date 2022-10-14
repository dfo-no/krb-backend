package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity //(name = "Product")
@Table //(name ="product")
class Product : PanacheEntity() {

    var title: String = ""

    var description: String = ""

    var deletedDate: LocalDateTime? = null

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        //optional = false,
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "product")
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        //optional = false,
        fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "val-reqvariant-product")
    @JsonIgnore
    @JoinColumn(name = "requirementvariant_id_fk")
    var requirementvariant: RequirementVariant? = null
}