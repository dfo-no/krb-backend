package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Where(clause = "deletedDate is null")
class Product : SoftDeletable() {

    lateinit var title: String

    lateinit var description: String

    override var deletedDate: LocalDateTime? = null

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "product")
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "val-reqvariant-product")
    @JsonIgnore
    @JoinColumn(name = "requirementvariant_id_fk")
    var requirementvariant: RequirementVariant? = null
}