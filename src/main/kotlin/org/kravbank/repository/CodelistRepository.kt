package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.jboss.resteasy.annotations.Query
import org.kravbank.domain.Codelist
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodelistRepository: PanacheRepository<Codelist> {

}