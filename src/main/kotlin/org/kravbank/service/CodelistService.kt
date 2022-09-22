package org.kravbank.service

import io.quarkus.hibernate.orm.panache.PanacheQuery
import org.hibernate.jpa.QueryHints
import org.jboss.resteasy.annotations.Query
import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.repository.CodelistRepository
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager


@ApplicationScoped
class CodelistService(val codelistRepository: CodelistRepository) {


    fun listCodelists(): MutableList<Codelist> = codelistRepository.listAll()

    fun listCodelistsByRef(ref: String): MutableList<Codelist> = codelistRepository.listAllRefs(ref)

    //fun listCodelistsByProjectId(id: Long) : MutableList<Codelist>? = codelistRepository.listAllByProjectId(id)

    fun getCodelist(id: Long): Codelist = codelistRepository.findById(id)

    fun getCodelistByRef(ref: String): Codelist? {
        return listCodelists().find { codelist ->
            codelist.ref == ref
        }
    }

    fun getCodelistByRefCustomRepo (ref: String): Codelist? = codelistRepository.findByRef(ref)


    fun createCodelist(codelist: Codelist) = codelistRepository.persistAndFlush(codelist)

    fun exists(id: Long?): Boolean = codelistRepository.count("id", id) == 1L

    fun refExists(ref: String): Boolean = codelistRepository.count("ref", ref) == 1L


    /***
     * TODO
     * Fix
     * Constraint error.
     * Child må slettes fra parent
     *
     *
     */
    fun deleteCodelist(id: Long) = codelistRepository.deleteById(id)

    fun updateCodelist(id: Long, codelist: Codelist) {
        codelistRepository.update("title = ?1, description = ?2 where id= ?3",
            codelist.title,
            codelist.description,
            id)
    }
}