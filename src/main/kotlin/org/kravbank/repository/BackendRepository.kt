package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.SoftDeletable
import org.kravbank.lang.NotFoundException
import java.time.LocalDateTime
import java.util.*

open class BackendRepository<T : PanacheEntity> : PanacheRepository<T> {

    override fun delete(entity: T) = when (entity) {
        is SoftDeletable -> {
            val deletedDate = LocalDateTime.now()
            entity.deletedDate = deletedDate
            persistAndFlush(entity)
        }

        else -> {
            super.delete(entity)
        }
    }


    fun findByRef(parentId: Long?, vararg ref: String): T {
        val objectInConstructor = javaClass.declaredConstructors[0].toString()
        val entityClassName: String = objectInConstructor.split("repository.")[1].split("Repository")[0]

        var foundEntity: T? = null

        //TODO
        val childrenOfProject = listOf("Product", "Publication", "Requirement", "Need", "Codelist")
        val childrenOfRequirement = listOf("RequirementVariant")
        val childrenOfCodelist = listOf("Code")

        when (entityClassName) {

            childrenOfProject.find { e -> e == entityClassName } -> {
                foundEntity = find(
                    "ref = ?1 and project.id = ?2",
                    ref,
                    parentId
                ).firstResult()
            }

            childrenOfRequirement.find { e -> e == entityClassName } -> {
                foundEntity = find(
                    "ref = ?1 and requirement.id = ?2",
                    ref,
                    parentId
                ).firstResult()
            }

            childrenOfCodelist.find { e -> e == entityClassName } -> {
                foundEntity = find(
                    "ref = ?1 and codelist.id = ?2",
                    ref,
                    parentId
                ).firstResult()
            }

            else -> {
                //is project
                foundEntity = find("ref", ref).firstResult()
            }
        }
        return Optional.ofNullable(foundEntity).orElseThrow { NotFoundException("Not found") }
    }
}
