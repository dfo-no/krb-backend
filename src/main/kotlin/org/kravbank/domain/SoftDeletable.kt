package org.kravbank.domain

import io.quarkus.hibernate.orm.panache.PanacheEntity
import org.hibernate.annotations.Where
import java.time.LocalDateTime

@Where(clause = "deletedDate is null")
sealed class SoftDeletable : PanacheEntity() {
    abstract var deletedDate: LocalDateTime?
}