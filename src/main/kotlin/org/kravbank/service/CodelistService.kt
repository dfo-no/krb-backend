package org.kravbank.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodelistService(
    val codelistRepository: CodelistRepository,
    val projectRepository: ProjectRepository
) {

    private val objectMapper = jacksonObjectMapper()

    @Throws(BackendException::class)
    fun get(projectRef: String, codelistRef: String): CodelistForm {

        val foundProject = projectRepository.findByRef(projectRef)

        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)

        return CodelistForm().fromEntity(foundCodelist).apply {
            codes = deserializeCodes(foundCodelist.serialized_codes)
        }
    }


    @Throws(BackendException::class)
    fun list(projectRef: String): List<CodelistForm> {

        val foundProject = projectRepository.findByRef(projectRef)

        val foundCodelists = codelistRepository.listAllCodelists(foundProject.id)

        return foundCodelists
            .stream()
            .map(CodelistForm()::fromEntity)
            .toList()
            .onEach { form ->
                foundCodelists.map { entity ->
                    if (entity.ref == form.ref) {
                        if (entity.serialized_codes != "") form.codes = deserializeCodes(entity.serialized_codes)
                    }
                }
            }
    }


    @Throws(BackendException::class)
    fun create(projectRef: String, newCodelist: CodelistForm): Codelist {

        val foundProject = projectRepository.findByRef(projectRef)

        return CodelistForm().toEntity(newCodelist).apply {
            project = foundProject
            serialized_codes = objectMapper.writeValueAsString(newCodelist.codes)
        }.also {
            codelistRepository.persistAndFlush(it)
            if (!codelistRepository.isPersistent(it)) throw BadRequestException(CODELIST_BADREQUEST_CREATE)
        }
    }


    @Throws(BackendException::class)
    fun delete(projectRef: String, codelistRef: String): Boolean {

        val foundProject = projectRepository.findByRef(projectRef)

        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)

        return codelistRepository.deleteById(foundCodelist.id)
    }


    @Throws(BackendException::class)
    fun update(projectRef: String, codelistRef: String, updatedCodelist: CodelistForm): Codelist {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)

        //TODO send codelistform
        return CodelistForm().toEntity(updatedCodelist).apply {
            serialized_codes = objectMapper.writeValueAsString(updatedCodelist.codes)
        }.also {
            codelistRepository.updateCodelist(foundCodelist.id, it)
        }
    }


    fun deserializeCodes(serialized: String): List<Code>? =
        objectMapper.readValue<List<Code>>(
            serialized,
            objectMapper.typeFactory
                .constructCollectionType(
                    List::class.java,
                    Code::class.java
                )
        )
}