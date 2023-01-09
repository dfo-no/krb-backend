package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
class Need : PanacheEntity() {

    @Column(columnDefinition="TEXT")
    lateinit var title: String

    @Column(columnDefinition="TEXT")
    lateinit var description: String

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var project: Project? = null

    @OneToMany(
        mappedBy = ("need"),
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],//, CascadeType.REMOVE],
        orphanRemoval = true,
    )
    var requirements = mutableListOf<Requirement>()

}
