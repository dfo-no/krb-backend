package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
class Requirement : PanacheEntity() {

    @Column(columnDefinition="TEXT")
    var title: String = ""

    @Column(columnDefinition="TEXT")
    var description: String = ""

    @OneToMany(
        mappedBy = ("requirement"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    var requirementvariants = mutableListOf<RequirementVariant>()

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.EAGER,
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var project: Project? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var need: Need? = null
}
