package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.hibernate.annotations.Where
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Where(clause = "deletedDate is null")
class Publication : SoftDeletable(), Serializable {

    lateinit var comment: String

    var date: LocalDateTime = LocalDateTime.now()

    var version: Long = 0

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    override var deletedDate: LocalDateTime? = null

    //TODO UUID OR ID ref
    @OneToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "publicationExport_id_fk")
    @JsonBackReference(value = "publication_export_backReference")
    var publicationExport: PublicationExport? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-publication")
    @JsonIgnore //TODO CHECK OUT
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null

}
