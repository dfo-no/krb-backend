package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.ProjectKtl
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProjectRepository : PanacheRepository<ProjectKtl> { //project, long

    //fun findByTitle(name: String) = find("name", name).firstResult()

}