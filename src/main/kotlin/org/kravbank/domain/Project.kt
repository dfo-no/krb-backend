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
     var projectId: String = ""

    @NotNull
    var deletedDate: String = ""

    //@NotNull
    @OneToMany //(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)//
    val products = mutableListOf<Product>()

    @OneToMany
    val publications = mutableListOf<Publication>()


/*
    @OneToMany
    var needs: List<Need>? = null

    @OneToMany
    var requirements // -> private String requirementVarian; --> private string config -->
            : List<Requirement>? = null

    @OneToMany
    var products: List<Product>? = null

    @OneToMany
    var codeList //CONFIG
            : List<Code>? = null


    //private String dependency; //vent med bruk
    //private String tags; //vent med bruk


 */
    //osv
}