package org.kravbank.service

import org.jboss.resteasy.annotations.Query
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.repository.CodelistRepository
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class CodelistService(val codelistRepository: CodelistRepository) {
    fun listCodelists(): MutableList<Codelist> = codelistRepository.listAll()

    fun getCodelist(id: Long): Codelist = codelistRepository.findById(id)

    fun getCodelistByRef(ref: String): Codelist? {
        return listCodelists().find { codelist ->
            codelist.ref == ref
        }
    }


    fun createCodelist(codelist: Codelist) = codelistRepository.persistAndFlush(codelist)

    fun exists(id: Long?): Boolean = codelistRepository.count("id", id) == 1L

    //   fun refExists(ref: String): Boolean = codelistRepository.count("ref", ref) == 1L


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