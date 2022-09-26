package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Publication
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationRepository : PanacheRepository<Publication> {

    fun customUpdate(publication: Publication) = update("", publication)


}