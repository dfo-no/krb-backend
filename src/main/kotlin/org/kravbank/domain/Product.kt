package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Where(clause = "deletedDate is null")
class Product : SoftDeletable() {

    @Column(columnDefinition="TEXT")
    lateinit var title: String

    @Column(columnDefinition="TEXT")
    lateinit var description: String

    override var deletedDate: LocalDateTime? = null

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var project: Project? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY
    )
    var requirementvariant: RequirementVariant? = null
}