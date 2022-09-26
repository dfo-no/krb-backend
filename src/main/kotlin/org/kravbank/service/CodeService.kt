package org.kravbank.service

import org.kravbank.domain.Code
import org.kravbank.repository.CodeRepository
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class CodeService(val codeRepository: CodeRepository) {
    fun listCodes(): MutableList<Code> = codeRepository.listAll()

    fun getCode(id: Long): Code = codeRepository.findById(id)

    fun createCode(requirement: Code) = codeRepository.persistAndFlush(requirement)

    fun exists(id: Long): Boolean = codeRepository.count("id", id) == 1L

    fun deleteCode(id: Long) = codeRepository.deleteById(id)

    fun updateCode(id: Long, requirement: Code) {
       // codeRepository.update(
          //  "comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
            // requirement.comment, requirement.version, requirement.bankId, requirement.date, id
        //)

    }

}