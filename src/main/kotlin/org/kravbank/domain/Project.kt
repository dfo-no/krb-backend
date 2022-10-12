package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import lombok.ToString
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.*
import javax.validation.constraints.*

@Entity//(name = "Project")
@Table //(name = "project")
class Project : PanacheEntity() {

    @NotBlank
    @NotEmpty(message = "Title may not be empty")
    @Size(min = 2, max = 32, message = "Title must be between 2 and 32 characters long")
    lateinit var title: String

    @NotBlank
    @NotEmpty(message = "Description may not be empty")
    @Size(min = 4, max = 140, message = "Description must be between 4 and 140 characters long")
    lateinit var description: String

    @Min(1)
    var version: Long? = null  // //if changed +1 via update form

    var publishedDate: LocalDateTime = LocalDateTime.now() // evnt last updated attribute

    @Column(
        unique = true,
        name = "ref")
    var ref: String = UUID.randomUUID().toString()

    var deletedDate: LocalDateTime? = null //inactive vs. active projs --> if inactive show deleted date?

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        )
    @JsonBackReference(value="product")
    var products = mutableListOf<Product>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnore
    @JsonBackReference(value="val-publication")
    var publications = mutableListOf<Publication>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnore
    @JsonBackReference(value="val-requirement")
    var requirements = mutableListOf<Requirement>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnore
   @JsonBackReference(value="need")
    var needs = mutableListOf<Need>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value="val-codelist")
    //@JsonIgnore
    var codelist = mutableListOf<Codelist>()


    //private String dependency; //vent med bruk
    //private String tags; //vent med bruk

//    override fun toString(): String {
//        return  "project id: $id title: $title"
//    }
}