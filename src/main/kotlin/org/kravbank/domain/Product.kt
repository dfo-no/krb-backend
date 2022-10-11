package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import lombok.ToString
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
        cascade = [CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        //optional = false,
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null

    //@JsonBackReference
    //@JoinColumn(name = "project_id", foreignKey = ForeignKey(name = "project_id_fk"))

    // var session: Session? = null

    /*
    OneToMany //(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)//
    var products = mutableListOf<Product>()
     */

//    override fun toString(): String {
//        return "project id: ${id} title: ${title} project: $project"
//    }

}