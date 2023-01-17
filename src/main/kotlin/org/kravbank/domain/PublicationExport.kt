package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class PublicationExport : PanacheEntity() {

    var ref: String = UUID.randomUUID().toString()

    lateinit var publicationRef: String

    //hibernate 6 ref
    @Column(columnDefinition = "TEXT")
    var serializedProject: String = ""

    override fun toString(): String {
        return "Ref : $ref, SerializedProject : $serializedProject"
    }

}
