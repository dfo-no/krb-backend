package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;

@Entity
class Publication: PanacheEntity() {

    var comment: String = ""

    var date: String =""

    var version: Long = 1 //long

    var bankId: String = ""

    var deletedDate: String = ""

}
