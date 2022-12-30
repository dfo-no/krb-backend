package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import lombok.NoArgsConstructor
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@NoArgsConstructor
class Codelist : PanacheEntity(), Serializable {

    lateinit var title: String

    lateinit var description: String

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @OneToMany(
        mappedBy = ("codelist"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value = "value-codes")
    var codes: MutableList<Code>? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-codelist")
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null

    override fun toString(): String {
        return "Title: $title,  Description: $description, Ref: $ref " +
                "Codes: $codes \n" +
                "Project: $project \n"
    }
}