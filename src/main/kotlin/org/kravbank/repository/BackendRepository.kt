package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.SoftDeletable
import java.time.LocalDateTime

//private var <T> T.deletedDate: LocalDateTime?
//    get() {}
//    set() {}

open class BackendRepository<T : SoftDeletable> : PanacheRepository<T> {
    fun deletePublication(publication: T): T {
        val deletedDate = LocalDateTime.now()
        //val updates = update("deleteddate = ?1 where id = ?2", deletedDate, publication)
        publication.deletedDate = deletedDate
        persistAndFlush(publication)
        return  publication
    }
}