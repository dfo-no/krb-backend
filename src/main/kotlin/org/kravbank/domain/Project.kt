package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
//@Table(name = "Project")
class Project: PanacheEntity() {


    //@Id
    //@GeneratedValue
    //var id: Long? = null;

    @NotNull
    var title: String = ""


    var description: String = ""


    var version: Long  = 0 // //if changed +1 via update form

    var publishedDate: LocalDateTime  =  LocalDateTime.now() // evnt last updated attribute

    @Column(unique = true)
     var ref: String = UUID.randomUUID().toString()

    //@NotNull
     var deletedDate: LocalDateTime? = LocalDateTime.now() //inactive vs. active projs --> if inactive show deleted date?

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    var products = mutableListOf<Product>()

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    var publications = mutableListOf<Publication>()

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    var requirements = mutableListOf<Requirement>()

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    var needs = mutableListOf<Need>()

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    var codelist = mutableListOf<Codelist>()

    //private String dependency; //vent med bruk
    //private String tags; //vent med bruk
}