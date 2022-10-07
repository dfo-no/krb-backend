package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity //(name = "Product")
@Table //(name ="product")
class Product : PanacheEntity() {

    var title: String = ""

    var description: String = ""

    var deletedDate: LocalDateTime? = null

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        //cascade = [CascadeType.ALL],
        //optional = false
        //fetch = FetchType.LAZY
    )
    @JsonIgnore
    //@JoinColumn(name="project_id")
    var project: Project? = null

    //@JsonBackReference
    //@JoinColumn(name = "project_id", foreignKey = ForeignKey(name = "project_id_fk"))

   // var session: Session? = null

    /*
    OneToMany //(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)//
    var products = mutableListOf<Product>()
     */

}