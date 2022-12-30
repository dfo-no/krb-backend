package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.NoArgsConstructor
import org.hibernate.annotations.Where
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
@Where(clause = "deletedDate is null")
@NoArgsConstructor
class Project : SoftDeletable(), Serializable {

    var title: String = ""

    var description: String = " "

    @Column(
        unique = true,
        name = "ref"
    )
    var ref: String = UUID.randomUUID().toString()

    override var deletedDate: LocalDateTime? = null

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
    @JsonBackReference(value = "val-publication")
    var publications = mutableListOf<Publication>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value = "val-requirement")
    var requirements = mutableListOf<Requirement>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value = "val-need-project")
    var needs = mutableListOf<Need>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value = "val-codelist")
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
  