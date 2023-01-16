package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@NamedNativeQuery(
    name = "selectDeletedRecords",
    query = "SELECT * FROM DeleteRecord",
    resultClass = DeleteRecord::class
)
@Entity
data class DeleteRecord(

    @Column(columnDefinition = "jsonb")
    var data: String? = null,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deletedAt", nullable = false)
    var deletedAt: Date? = Timestamp.valueOf(LocalDateTime.now()),

    var updatedAt: LocalDateTime? = null,

    @Column(columnDefinition = "varchar(200)")
    var tableName: String? = null,

    var objectId: Long? = null

) : PanacheEntityBase() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}