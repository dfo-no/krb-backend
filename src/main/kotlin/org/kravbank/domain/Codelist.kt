package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity

class Codelist() : PanacheEntity() {

    var title: String = ""

    var description: String = ""

    @Column(unique = true)
    var ref : String = UUID.randomUUID().toString()

    @OneToMany
    var codes = mutableListOf<Code>()

    @OneToMany
    var configs = mutableListOf<Config>()

    // public String type; //code
    //public parent


}
