package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Requirement
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementRepository: PanacheRepository<Requirement> {

}