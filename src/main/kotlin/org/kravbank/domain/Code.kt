package org.kravbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*
import javax.persistence.*

@Entity
class Code: PanacheEntity() {
     var title: String = ""

     var description: String = ""

     @Column(unique = true)
     var ref: String = UUID.randomUUID().toString()


     @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE])
     @JsonIgnore
     lateinit var codelist: Codelist

     // public String type; //code
     //public parent

}
