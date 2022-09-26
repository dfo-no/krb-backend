package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.jboss.resteasy.annotations.Query

import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.postgresql.core.NativeQuery
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager
import javax.persistence.NamedNativeQuery
import javax.persistence.PersistenceContext


@ApplicationScoped
class CodelistRepository: PanacheRepository<Codelist> {




    //fun findByTitle(name: String) = find("name", name).firstResult()

    fun findByRef(ref: String) = find("ref", ref).firstResult<Codelist>()

    fun listAllRefs(ref: String) = find("ref", ref).list<Codelist>()

    /*

    fun listAllByProjectId(id: Long): MutableList<Codelist>? {
       //var q = NativeQuery("SELECT * FROM Codelist WHERE id in (SELECT id FROM project_codelist WHERE project_id = ?1)",id)
        return find("FROM Codelist c WHERE id c.ref.id = ?1", id).list<Codelist>()
    }

     */
}
