package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
class Codelist : PanacheEntity() {

    @Column(columnDefinition="TEXT")
    lateinit var title: String

    @Column(columnDefinition="TEXT")
    lateinit var description: String

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @OneToMany(
        mappedBy = ("codelist"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    var codes: MutableList<Code>? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY,
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var project: Project? = null
}