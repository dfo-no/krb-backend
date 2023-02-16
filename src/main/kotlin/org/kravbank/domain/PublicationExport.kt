package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class PublicationExport : PanacheEntity() {

    var ref: String = UUID.randomUUID().toString()

    lateinit var publicationRef: String

    @Column(columnDefinition = "TEXT")
    var serializedProject: String = ""

    override fun toString(): String {
        return "Ref : $ref, SerializedProject : $serializedProject"
    }
}
