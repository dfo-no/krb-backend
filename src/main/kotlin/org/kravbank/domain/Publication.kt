package org.kravbank.domain

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

    //one-to-one?
    lateinit var s3Id: String  // UUID samme som UUID i s3 til projectExport

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-publication")
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null

}
