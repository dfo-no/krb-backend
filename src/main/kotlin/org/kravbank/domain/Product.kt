package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class Product: PanacheEntity() {

    //@Id
    //@GeneratedValue
    //var id: Long? = null;
    var title: String = ""

    var description: String = ""

    var deletedDate: LocalDateTime? = null

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne
    @JsonIgnore
    lateinit var project: Project


    /*
    OneToMany //(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)//
    var products = mutableListOf<Product>()
     */

}