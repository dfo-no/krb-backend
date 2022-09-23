package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity
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

    @NotNull
    @NotBlank
    @NotEmpty
    var description: String = ""

    @NotEmpty
    @NotNull
    @NotBlank
    @GeneratedValue
    var version: String = ""

    var publishedDate: String  = ""//date

    @Column(unique = true)
     var ref: String = UUID.randomUUID().toString()

    @NotNull
    var deletedDate: String = "" // logic here

    @OneToMany //(cascade = [(CascadeType.MERGE)], fetch = FetchType.LAZY)
    @JsonIgnore
    var products = mutableListOf<Product>()

    @OneToMany
    @JsonIgnore
    var publications = mutableListOf<Publication>()

    @OneToMany
    @JsonIgnore
    var requirements = mutableListOf<Requirement>()

    @OneToMany
    @JsonIgnore
    var needs = mutableListOf<Need>()

    /***
     * TODO
     * Fix delete
     */
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    //@OnDelete(action = OnDeleteAction.CASCADE)
    var codelist = mutableListOf<Codelist>()

    //private String dependency; //vent med bruk
    //private String tags; //vent med bruk
}