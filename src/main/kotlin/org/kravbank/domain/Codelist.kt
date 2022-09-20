package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
class Codelist() : PanacheEntity() {

    var title: String = ""

    var description: String = ""

    @Column(unique = true)
    var ref : String = UUID.randomUUID().toString()

    @OneToMany
    @JsonIgnore
    var codes = mutableListOf<Code>()

    @OneToMany
    @JsonIgnore
    var configs = mutableListOf<Config>()

    // public String type; //code
    //public parent


}
