package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class PublicationExport : PanacheEntity() {

    var ref: String = UUID.randomUUID().toString()

    //hibernate 6 ref
    @Column(columnDefinition = "TEXT")
    lateinit var content: String

    lateinit var publicationRef: String


    override fun toString(): String {
        return "Ref : $ref, BlobFormat : $content"
    }

}
