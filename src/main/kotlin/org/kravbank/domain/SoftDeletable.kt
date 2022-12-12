package org.kravbank.domain

import java.time.LocalDateTime

interface SoftDeletable {
    var deletedDate: LocalDateTime?
}