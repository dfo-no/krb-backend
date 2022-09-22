package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProjectRepository : PanacheRepository<Project> { //project, long

    //fun findByTitle(name: String) = find("name", name).firstResult()

    fun findByRef(ref: String) = find("ref", ref).firstResult<Project>()

    fun listAllByProjectRef(ref: String) = find("ref", ref).list<Project>()



}