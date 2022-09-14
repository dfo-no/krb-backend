package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import org.kravbank.form.CodelistForm
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity

class Codelist() : PanacheEntity() {

    var title: String = ""

    var description: String = ""

    @OneToMany
    var codes = mutableListOf<Code>()

    @OneToMany
    var configs = mutableListOf<Config>()

    // public String type; //code
    //public parent


}
