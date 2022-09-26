package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity;

@Entity
class Publication: PanacheEntity() {

    var comment: String = ""

    var date: String = ""

    var version: String = ""

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    var deletedDate: String = ""

}
