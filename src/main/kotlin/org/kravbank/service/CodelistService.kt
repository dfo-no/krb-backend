package org.kravbank.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.Code2
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

        val codelist = codelistRepository.findByRef(foundProject.id, codelistRef)

        val deserializedCodes = objectMapper.readValue<List<Code2>>(
            codelist.serializedCodes,
            objectMapper.typeFactory.constructCollectionType(List::class.java, Code2::class.java)
        )

        val codelistForm = CodelistForm().fromEntity(codelist).apply {
            codes = deserializedCodes
        }

        return codelistForm
    }

    @Throws(BackendException::class)
    fun list(projectRef: String): List<CodelistForm> {

        val foundProject = projectRepository.findByRef(projectRef)

        return codelistRepository.listAllCodelists(foundProject.id)
            .stream()
            .map(CodelistForm()::fromEntity)
            .toList()
            .onEach {
                it.codes = objectMapper.readValue<List<Code2>>(
                    it.serializedCodes,
                    objectMapper.typeFactory.constructCollectionType(List::class.java, Code2::class.java)
                )
            }
    }


    @Throws(BackendException::class)
    fun create(projectRef: String, newCodelist: CodelistForm): Codelist {

        val foundProject = projectRepository.findByRef(projectRef)

        val codelist = CodelistForm().toEntity(newCodelist).apply {
            project = foundProject
            serializedCodes = objectMapper.writeValueAsString(newCodelist.codes)
        }

        codelistRepository.persistAndFlush(codelist)
        if (!codelistRepository.isPersistent(codelist)) throw BadRequestException(CODELIST_BADREQUEST_CREATE)

        return codelist

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

        val update = CodelistForm().toEntity(updatedCodelist)
        codelistRepository.updateCodelist(foundCodelist.id, update)
        return update.apply { ref = foundCodelist.ref }
    }
}