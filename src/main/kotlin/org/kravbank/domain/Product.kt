package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.*

@Entity
class Product: PanacheEntity() {

    //@Id
    //@GeneratedValue
    //var id: Long? = null;
    lateinit var title: String

    lateinit var description: String

    lateinit var deletedDate: String

    /*
    OneToMany //(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)//
    var products = mutableListOf<Product>()
     */

    //public String type;
    //public children
    //public parent
}