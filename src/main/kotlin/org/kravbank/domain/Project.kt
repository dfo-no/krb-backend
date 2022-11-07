package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.time.LocalDateTime
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Project : PanacheEntity() {

    lateinit var title: String

    lateinit var description: String

    @Column(
        unique = true,
        name = "ref"
    )
    var ref: String = UUID.randomUUID().toString()

    var deletedDate: LocalDateTime? = null

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value = "product")
    var products = mutableListOf<Product>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnore
    @JsonBackReference(value = "val-publication")
    var publications = mutableListOf<Publication>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnore
    @JsonBackReference(value = "val-requirement")
    var requirements = mutableListOf<Requirement>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnore
    @JsonBackReference(value = "val-need-project")
    var needs = mutableListOf<Need>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value = "val-codelist")
    //@JsonIgnore
    var codelist = mutableListOf<Codelist>()
}