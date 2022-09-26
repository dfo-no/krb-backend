package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Requirement
import org.kravbank.domain.RequirementVariant
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementVariantRepository: PanacheRepository<RequirementVariant> {

}