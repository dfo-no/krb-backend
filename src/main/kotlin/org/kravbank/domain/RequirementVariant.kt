package org.kravbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*
import javax.persistence.*

@Entity
class RequirementVariant:  PanacheEntity() {

    var description: String = ""

    var requirementText: String = ""

    var instruction: String = ""

    var useProduct: Boolean = false

    var useSpesification: Boolean = false //typo

    var useQualification: Boolean = false

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    var products = mutableListOf<Product>()

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    var configs = mutableListOf<Config>()

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    /*
    @ManyToOne
    @JsonIgnore
    lateinit var requirement: Requirement
 */

    //public questions
    // public String type;
}
