package org.kravbank.service

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.kravbank.dao.PublicationExportForm
import org.kravbank.domain.Project
import org.kravbank.domain.PublicationExport
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationExportRepository
import org.kravbank.repository.PublicationRepository
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class PublicationExportService(
    val publicationExportRepository: PublicationExportRepository,
    val projectRepository: ProjectRepository,
    val publicationRepository: PublicationRepository
) {


    fun get(
        projectRef: String,
        publicationRef: String,
        publicationExportRef: String
    ): PublicationExportForm {

        val project = projectRepository.findByRef(projectRef)

        val publication = publicationRepository.findByRef(project.id, publicationRef)

        val publicationExport = publicationExportRepository.findByRef(publication.ref, publicationExportRef)

        val deserializedProject = deserializedProject(publicationExport.serializedProject)

        val form = PublicationExportForm().fromEntity(publicationExport).apply {
            this.deserializedProject = deserializedProject
        }

        return form
    }

    fun list(
        projectRef: String,
        publicationRef: String
    ): List<PublicationExport> {

        val project = projectRepository.findByRef(projectRef)
        val publication = publicationRepository.findByRef(project.id, publicationRef)
        return publicationExportRepository.list(publication.id)
    }


    fun create(
        projectRef: String,
        publicationRef: String
    ): PublicationExport {

        val project = projectRepository.findByRef(projectRef)
        val publication = publicationRepository.findByRef(project.id, publicationRef)

        val serializedProject = serializedProject(project)

        val newPublicationExport = PublicationExport().apply {
            this.serializedProject = serializedProject
            this.publicationRef = publicationRef
        }

        publicationExportRepository.persistAndFlush(newPublicationExport)

        if (!publicationExportRepository.isPersistent(newPublicationExport)) throw BadRequestException("${newPublicationExport.ref} not persisted")

        PublicationService.updateAndPersistPublicationExport(
            publication.id,
            publicationRepository,
            publication.ref
        )

        return newPublicationExport
    }


    companion object Serialization {

        private val objectMapper = jacksonObjectMapper()

        fun serializedProject(project: Project): String {
            objectMapper.registerModule(JavaTimeModule())

            return objectMapper.writeValueAsString(project)
        }

        fun deserializedProject(str: String): Project = objectMapper.readValue(str, Project::class.java)

    }


}
