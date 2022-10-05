package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntity
import lombok.Getter
import lombok.Setter
import java.util.*
import javax.persistence.*

@Entity
class Codelist() : PanacheEntity() {
    var title: String = ""

    var description: String = ""

    @Column(unique = true)
    var ref : String = UUID.randomUUID().toString()

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE], orphanRemoval = true)
    @JsonIgnore
    var codes = mutableListOf<Code>()

    @OneToMany
    @JsonIgnore
    var configs = mutableListOf<Config>()

    /*@ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE])
    @JsonIgnore
    lateinit var project: Project

     */
}