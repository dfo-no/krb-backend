package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class Codelist : PanacheEntity() {

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    lateinit var title: String

    lateinit var description: String

    @Column(columnDefinition = "TEXT")
    var serialized_codes: String = ""

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-codelist")
    @JsonIgnore
    var project: Project? = null

    override fun toString(): String {
        return "Title: $title,  Description: $description, Ref: $ref " +
                "Codes: $serialized_codes \n" +
                "Project: $project \n"
    }
}

