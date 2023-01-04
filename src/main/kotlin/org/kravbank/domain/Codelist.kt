package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
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
    @JsonBackReference(value = "value-codes")
    var codes: MutableList<Code>? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-codelist")
    @JsonIgnore
    var project: Project? = null
}