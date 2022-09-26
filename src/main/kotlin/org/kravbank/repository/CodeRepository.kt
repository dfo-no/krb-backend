package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Code
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodeRepository: PanacheRepository<Code> {

}