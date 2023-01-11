package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import org.hibernate.Hibernate
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
data class DeleteRecord(

    @Column(columnDefinition = "jsonb")
    val data: String? = null,


    //TODO
    // Fikse: denne blir ikke satt i PSQL via triggeren
    // alternativt kan man opprette tabellen i psql direkte - med default verdi current_timestamp
    //@Temporal(TemporalType.TIMESTAMP)
    //@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val deletedAt: Date? = Timestamp.valueOf(LocalDateTime.now()),

    val updatedAt: LocalDateTime? = null,

    @Column(columnDefinition = "varchar(200)")
    val tableName: String? = null,

    val objectId: Long? = null

) : PanacheEntityBase() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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



