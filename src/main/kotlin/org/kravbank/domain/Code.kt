package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;

@Entity
class Code: PanacheEntity() {
    lateinit var title: String

    lateinit var description: String

   // public String type; //code

    //public parent

}
