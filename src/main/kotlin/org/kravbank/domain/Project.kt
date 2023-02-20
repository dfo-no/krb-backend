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
data class Project(

    @Column(
        unique = true,
        name = "ref"
    )
    var ref: String = UUID.randomUUID().toString(),

    var title: String = "",

    var description: String = "",

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    var products: MutableList<Product>? = null,

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    var publications: MutableList<Publication>? = null,

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    var requirements: MutableList<Requirement>? = null,

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    var needs: MutableList<Need>? = null,

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var codelist: MutableList<Codelist>? = null

) : PanacheEntity() {

    override fun toString(): String {
        return "Project(title='$title', description='$description', ref='$ref', products=$products, publications=$publications, requirements=$requirements, needs=$needs, codelist=$codelist)"
    }
}