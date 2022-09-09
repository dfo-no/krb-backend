package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonSetter
import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.*

@Entity

class ProductKtl: PanacheEntity() {

    //@Id
    //@GeneratedValue
    //var id: Long? = null;
    lateinit var title: String
    lateinit var description: String
    lateinit var deletedDate: String;










    //osv
}