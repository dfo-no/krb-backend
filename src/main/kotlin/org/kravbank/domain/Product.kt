package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class Product : PanacheEntity() {

    //@Id
    //@GeneratedValue
   // @Column(name = "product_id")
    //var id: Long? = null;

    var title: String = ""

    var description: String = ""

    var deletedDate: LocalDateTime? = null

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.ALL],
        //optional = false
        //fetch = FetchType.LAZY
    )
    //@JsonIgnore
    //@JoinColumn(name = "project_id", foreignKey = ForeignKey(name = "project_id_fk"))
    var project: Project? = null



   // var session: Session? = null


    /*
    OneToMany //(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)//
    var products = mutableListOf<Product>()
     */

}