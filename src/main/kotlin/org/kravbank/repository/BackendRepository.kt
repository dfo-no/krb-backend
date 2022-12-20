package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.SoftDeletable
import java.time.LocalDateTime

open class BackendRepository<T : PanacheEntity> : PanacheRepository<T> {
    override fun delete(entity: T) = when (entity) {
        is SoftDeletable -> {
            val deletedDate = LocalDateTime.now()
            entity.deletedDate = deletedDate
            persistAndFlush(entity)
        }

        else -> {
            super.delete(entity)
        }
    }
}