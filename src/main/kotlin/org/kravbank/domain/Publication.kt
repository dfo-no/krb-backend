package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity;

@Entity
class Publication: PanacheEntity() {

    var comment: String = ""

    var date: LocalDateTime? = null

    var version: Long = 0

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    var deletedDate: LocalDateTime? = null

}
