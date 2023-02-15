package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class Project : PanacheEntity() {

    lateinit var title: String

    lateinit var description: String

    @Column(
        unique = true,
        name = "ref"
    )
    var ref: String = UUID.randomUUID().toString()

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

    override fun toString(): String {
        return "Project(title='$title', description='$description', ref='$ref', products=$products, publications=$publications, requirements=$requirements, needs=$needs, codelist=$codelist)"
    }
}