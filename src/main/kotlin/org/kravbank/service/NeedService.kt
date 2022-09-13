package org.kravbank.service

import org.kravbank.domain.Need
import org.kravbank.repository.NeedRepository
import java.util.*
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class NeedService(val needRepository: NeedRepository) {
    fun listNeeds(): MutableList<Need> = needRepository.listAll()

    fun getNeed(id: Long): Need = needRepository.findById(id)

    fun createNeed(requirementVariant: Need) = needRepository.persistAndFlush(requirementVariant)

    fun exists(id: Long): Boolean = needRepository.count("id", id) == 1L

    fun deleteNeed(id: Long) = needRepository.deleteById(id)

    fun updateNeed(id: Long, requirementVariant: Need) {
       // needRepository.update(
          //  "comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
            // requirementVariant.comment, requirementVariant.version, requirementVariant.bankId, requirementVariant.date, id
        //)

    }

}