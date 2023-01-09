package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
@Where(clause = "deletedDate is null")
class Project : SoftDeletable() {

    lateinit var title: String

    lateinit var description: String

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    override var deletedDate: LocalDateTime? = null

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    var products = mutableListOf<Product>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    var publications = mutableListOf<Publication>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var requirements = mutableListOf<Requirement>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )

    var needs = mutableListOf<Need>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    var codelist = mutableListOf<Codelist>()
}