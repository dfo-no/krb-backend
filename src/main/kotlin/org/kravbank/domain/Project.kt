package org.kravbank.domain

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

    @Column(
        unique = true,
    )
    var ref: String = UUID.randomUUID().toString()

    lateinit var title: String

    lateinit var description: String

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
        return "Title: $title,  Description: $description, Ref: $ref, Deleted date: $deletedDate \n" +
                "Codelist: $codelist \n" +
                "Needs: $needs \n" +
                "Requirements: $requirements \n" +
                "Publications $publications \n" +
                "Products $products"
    }

}
  