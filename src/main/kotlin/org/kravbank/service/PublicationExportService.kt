package org.kravbank.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.hibernate.engine.jdbc.BlobProxy
import org.kravbank.dao.PublicationExportForm
import org.kravbank.domain.Project
import org.kravbank.domain.PublicationExport
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationExportRepository
import org.kravbank.repository.PublicationRepository
import java.io.IOException
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

        val publicationId = publicationRepository.findByRef(project.id, publicationRef).id

        val publicationExport = publicationExportRepository.findByRef(publicationId, publicationExportRef)

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


    fun save(
        projectRef: String,
        publicationRef: String
    ): String {

        val project = projectRepository.findByRef(projectRef)
        val publication = publicationRepository.findByRef(project.id, publicationRef)

        val convertProjectToBytes = writeValueAsBytes(project)

        val newPublicationExport = PublicationExport()
        newPublicationExport.ref = UUID.randomUUID().toString()

        newPublicationExport.blobFormat = encodeBlob(convertProjectToBytes)
        newPublicationExport.publication = publication

        publicationExportRepository.persistAndFlush(newPublicationExport)

        return newPublicationExport.ref
    }

    //ENCODE

    fun writeValueAsBytes(
        project: Project,
    ): ByteArray {

        val objectMapper = ObjectMapper()

        objectMapper.registerModule(JavaTimeModule())

        return objectMapper.writeValueAsBytes(project)
    }

    fun encodeBlob(byteArray: ByteArray): Blob = BlobProxy.generateProxy(byteArray)


    //DECODE


    // TODO ("Handle specifications & answer when needed")
    fun readValueFromBytes(
        byteArray: ByteArray
    ): Project {
        val inObjectMapper = ObjectMapper()

        val project = inObjectMapper.readValue(byteArray, Project::class.java)

        if (project is Project) {
            return project
        } else throw IOException("An error with Input/Output/ObjectMapper")
    }


    fun decodeBlob(blob: Blob): Project {

        val bytes = generateBytes(blob)

        return readValueFromBytes(bytes)
    }


    fun generateBytes(blob: Blob): ByteArray {

        val bytes = blob.getBytes(1, blob.length().toInt())

        blob.free()

        return bytes
    }
}
