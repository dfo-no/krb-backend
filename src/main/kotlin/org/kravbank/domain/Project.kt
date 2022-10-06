package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "Project")
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

    @Column(unique = true, name = "ref")
    var ref: String = UUID.randomUUID().toString()

    var deletedDate: LocalDateTime? = null //inactive vs. active projs --> if inactive show deleted date?

    @OneToMany(
        //mappedBy = ("parentProject"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
   // @JsonIgnore
    var products = mutableListOf<Product>()


    @OneToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    @JsonIgnore
    var publications = mutableListOf<Publication>()

    @OneToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    @JsonIgnore
    var requirements = mutableListOf<Requirement>()

    @OneToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    @JsonIgnore
    var needs = mutableListOf<Need>()

    @OneToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    @JsonIgnore
    var codelist = mutableListOf<Codelist>()


    //private String dependency; //vent med bruk
    //private String tags; //vent med bruk
}