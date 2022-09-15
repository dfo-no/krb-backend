package org.kravbank.service

import org.kravbank.domain.Codelist
import org.kravbank.repository.CodelistRepository
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class CodelistService(val codelistRepository: CodelistRepository) {
    fun listCodelists(): MutableList<Codelist> = codelistRepository.listAll()

    fun getCodelist(id: Long): Codelist = codelistRepository.findById(id)

    fun createCodelist(codelist: Codelist) = codelistRepository.persistAndFlush(codelist)

    fun exists(id: Long?): Boolean = codelistRepository.count("id", id) == 1L

    //   fun refExists(ref: String): Boolean = codelistRepository.count("ref", ref) == 1L

    fun deleteCodelist(id: Long) = codelistRepository.deleteById(id)

    fun updateCodelist(id: Long, codelist: Codelist) {
       //codelistRepository.update(
         //  "codelist = ?1", codelist)
          //"comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
           //  requirement.comment, requirement.version, requirement.bankId, requirement.date, id
        //)

    }

}