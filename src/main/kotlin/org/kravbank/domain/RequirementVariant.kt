package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
class RequirementVariant:  PanacheEntity() {

    var description: String = ""

    var requirementText: String = ""

    var instruction: String = ""

    var useProduct: Boolean = false

    var useSpesification: Boolean = false //typo

    var useQualification: Boolean = false

    @OneToMany
    var products = mutableListOf<Product>()

    @OneToMany
    var configs = mutableListOf<Config>()

    //public questions
    // public String type;
}
