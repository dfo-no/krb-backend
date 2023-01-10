package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import org.hibernate.Hibernate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class DeleteRecord(

    @Column(columnDefinition = "jsonb")
    val data: String?,

    val deletedAt: LocalDateTime?,

    val updatedAt: LocalDateTime?,

    @Column(columnDefinition = "varchar(200)")
    val tableName: String?,

    val objectId: Long?,
    
    ) : PanacheEntityBase() {

    constructor() : this(null, LocalDateTime.now(), null, null, null) {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as DeleteRecord

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}


