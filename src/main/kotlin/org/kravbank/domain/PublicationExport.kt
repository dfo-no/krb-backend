package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.sql.Blob
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
class PublicationExport : PanacheEntity() {

    lateinit var ref: String

    @Lob
    lateinit var blobFormat: Blob

    override fun toString(): String {
        return "Ref : $ref, BlobFormat : $blobFormat"
    }

}
