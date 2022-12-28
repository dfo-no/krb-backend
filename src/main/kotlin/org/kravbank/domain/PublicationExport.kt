package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.sql.Blob
import javax.persistence.*

@Entity
class PublicationExport : PanacheEntity() {

    lateinit var ref: String

    @Lob
    lateinit var blobFormat: Blob

    @OneToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "publication_id_fk")
    @JsonBackReference(value = "publication_export_backReference")
    var publication: Publication? = null

    override fun toString(): String {
        return "Ref : $ref, BlobFormat : $blobFormat"
    }

}
