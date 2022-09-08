package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class ProjectKtl: PanacheEntity() {

    //@Id
    //@GeneratedValue
    //var id: Long? = null;
    lateinit var title: String
    lateinit var description: String
    //osv
}