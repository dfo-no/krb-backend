package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.util.*
import javax.persistence.*

@Entity
data class Code(

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString(),

    var title: String = "",

    var description: String = "",

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "value-codes")
    @JsonIgnore
    var codelist: Codelist? = null

) : PanacheEntity() {

    override fun toString(): String {
        return "Code(ref='$ref', title='$title', description='$description', codelist=$codelist)"
    }

}


