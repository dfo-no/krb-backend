package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
class Need:  PanacheEntity() {

    lateinit var title: String

    lateinit var description: String

    @OneToMany
    var requirements = mutableListOf<Requirement>();


   // public String type;  //need
    //public parent




}
