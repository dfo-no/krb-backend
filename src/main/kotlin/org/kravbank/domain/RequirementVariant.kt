package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

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


   // @OneToMany
   // public List <Config> configs;


    //public questions
    // public String type;


    //CONFIG


}
