package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
class PublicationExport : PanacheEntity() {

    lateinit var ref: String

    @Lob
    lateinit var content: ByteArray

}
