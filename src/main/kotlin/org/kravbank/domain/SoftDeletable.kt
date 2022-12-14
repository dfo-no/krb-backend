package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import java.time.LocalDateTime

sealed class SoftDeletable: PanacheEntity() {
    abstract var deletedDate: LocalDateTime?
}