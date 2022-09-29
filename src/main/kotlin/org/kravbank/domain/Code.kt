package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity;

@Entity
class Code: PanacheEntity() {
     var title: String = ""

     var description: String = ""

     @Column(unique = true)
     var ref: String = UUID.randomUUID().toString()

     // public String type; //code

     //public parent

}
