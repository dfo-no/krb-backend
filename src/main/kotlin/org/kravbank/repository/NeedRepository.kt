package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Need
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NeedRepository: PanacheRepository<Need> {

}