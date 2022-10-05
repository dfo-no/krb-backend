package org.kravbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
class Requirement: PanacheEntity() {


    lateinit var title: String

    lateinit var description: String

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    var requirementvariants = mutableListOf<RequirementVariant>()

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

}
