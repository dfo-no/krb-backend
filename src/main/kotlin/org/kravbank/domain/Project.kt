package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
//@Table(name = "projectktl-test")
class ProjectKtl: PanacheEntity() {

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

    @NotNull
    @OneToMany //(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)// mappedBy = "productktl")
    val products = mutableListOf<ProductKtl>() //

    //osv
}