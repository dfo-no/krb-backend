package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity

@Entity
data class PublicationExport(

    var ref: String = UUID.randomUUID().toString(),

    var publicationRef: String = "",

    @Column(columnDefinition = "TEXT")
    var serializedProject: String = "",


    ) : PanacheEntity() {

    override fun toString(): String {
        return "Ref : $ref, SerializedProject : $serializedProject"
    }
}
