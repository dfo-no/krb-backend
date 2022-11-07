package org.kravbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*
import javax.persistence.*

@Entity
class Code : PanacheEntity() {

    var title: String = ""

    var description: String = ""

    @Column(unique = true)
    var ref: String = UUID.randomUUID().toString()

    @ManyToOne(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
        fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "value-codes")
    @JsonIgnore
    @JoinColumn(name = "codelist_id_fk")
    var codelist: Codelist? = null
}
