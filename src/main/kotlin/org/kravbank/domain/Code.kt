package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;

@Entity
class Code: PanacheEntity() {
     var title: String = ""
     var description: String = ""

   // public String type; //code

    //public parent

}
