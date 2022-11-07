package org.kravbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class Publication : PanacheEntity() {

    var comment: String = ""

    var date: LocalDateTime = LocalDateTime.now()

    var version: Long = 0

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    var deletedDate: LocalDateTime? = null

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH], //CascadeType.Detach
        fetch = FetchType.LAZY,
    )
    @JsonManagedReference(value = "val-publication")
    @JsonIgnore
    @JoinColumn(name = "project_id_fk")
    var project: Project? = null

}
