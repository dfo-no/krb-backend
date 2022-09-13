package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Codelist: PanacheEntity() {
    lateinit var title: String

    lateinit var description: String

    @OneToMany
    var codes = mutableListOf<Code>()

    @OneToMany
    var configs = mutableListOf<Config>()

    // public String type; //code
    //public parent
}
