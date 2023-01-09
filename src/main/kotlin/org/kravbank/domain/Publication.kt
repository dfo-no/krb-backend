package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Where(clause = "deletedDate is null")
class Publication : SoftDeletable() {

    @Column(columnDefinition="TEXT")
    lateinit var comment: String

    var date: LocalDateTime = LocalDateTime.now()

    var version: Long = 0

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    override var deletedDate: LocalDateTime? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var project: Project? = null

}
