package org.kravbank.service

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hibernate.engine.jdbc.BlobProxy
import org.kravbank.dao.PublicationExportForm
import org.kravbank.domain.Project
import org.kravbank.domain.PublicationExport
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationExportRepository
import org.kravbank.repository.PublicationRepository
import java.sql.Blob
import java.util.*
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class PublicationExportService(
    private val publicationExportRepository: PublicationExportRepository,
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

        val decodedProject = decodeBlob(publicationExport.blobFormat)
        val form = PublicationExportForm().fromEntity(publicationExport)
        form.content.add(decodedProject)

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

        val newPublicationExport = PublicationExport()
        newPublicationExport.ref = UUID.randomUUID().toString()
        newPublicationExport.blobFormat = encodeBlob(writeValueAsBytes(project))
        newPublicationExport.publicationRef = publicationRef

        publicationExportRepository.persistAndFlush(newPublicationExport)

        if (!publicationExportRepository.isPersistent(newPublicationExport)) throw BadRequestException("${newPublicationExport.ref} not persisted")

        PublicationService.updateAndPersistPublicationExport(
            publication.id,
            publicationRepository,
            publication.ref
        )

        return newPublicationExport
    }

    companion object {

        private val objectMapper = jacksonObjectMapper()


        //ENCODE

        fun writeValueAsBytes(
            project: Project,
        ): ByteArray {

            objectMapper.registerModule(JavaTimeModule())

            return objectMapper.writeValueAsBytes(project)
        }

        fun encodeBlob(byteArray: ByteArray): Blob = BlobProxy.generateProxy(byteArray)


        //DECODE


        // TODO ("Handle specifications & answer when needed")
        private fun readValueFromBytes(
            byteArray: ByteArray
        ): Project {


            //val project = objectMapper.readValue<Project>(byteArray)

            return objectMapper.readValue(byteArray, Project::class.java)


        }


        fun decodeBlob(blob: Blob): Project {

            val bytes = generateBytes(blob)

            return readValueFromBytes(bytes)
        }


        private fun generateBytes(blob: Blob): ByteArray {

            val bytes = blob.getBytes(1, blob.length().toInt())

            blob.free()

            return bytes
        }
    }
}
