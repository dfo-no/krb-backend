package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
//@Table(name = "projectktl-test")
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

    @NotNull
    //@Column(unique = true)
     var projectId: String = ""

    @NotNull
    var deletedDate: String = ""

    @OneToMany //(cascade = [(CascadeType.MERGE)], fetch = FetchType.LAZY)
    var products = mutableListOf<Product>()

    @OneToMany
    var publications = mutableListOf<Publication>()

    @OneToMany
    var requirements = mutableListOf<Requirement>()

    @OneToMany
    var needs = mutableListOf<Need>()

    @OneToMany
    var codeList = mutableListOf<Codelist>()

    //private String dependency; //vent med bruk
    //private String tags; //vent med bruk
}