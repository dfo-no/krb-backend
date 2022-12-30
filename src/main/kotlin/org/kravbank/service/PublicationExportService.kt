package org.kravbank.service

import org.hibernate.engine.jdbc.BlobProxy
import org.kravbank.domain.Project
import org.kravbank.domain.Publication
import org.kravbank.domain.PublicationExport
import org.kravbank.lang.BackendException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationExportRepository
import org.kravbank.repository.PublicationRepository
import java.io.*
import java.sql.Blob
import java.util.*
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class PublicationExportService(
    private val publicationExportRepository: PublicationExportRepository,
    val projectRepository: ProjectRepository,
    val publicationRepository: PublicationRepository
) {

    
    fun getDecodedBlob(
        projectRef: String,
        publicationRef: String,
        publicationExportRef: String
    ): ArrayList<*> {

        val project = projectRepository.findByRef(projectRef)

        val publicationId = publicationRepository.findByRef(project.id, publicationRef).id

        val publicationExport = publicationExportRepository.findByRef(publicationId, publicationExportRef)

        return decodeBlob(publicationExport)

    }

    fun get(
        projectRef: String,
        publicationRef: String,
        publicationExportRef: String
    ): PublicationExport {

        val project = projectRepository.findByRef(projectRef)

        val publicationId = publicationRepository.findByRef(project.id, publicationRef).id

        return publicationExportRepository.findByRef(publicationId, publicationExportRef)

    }


    @Throws(BackendException::class)
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

        val convertObjectsToBytes = convertToBytes(project, publication)

        val newPublicationExport = PublicationExport()
        newPublicationExport.ref = UUID.randomUUID().toString()
        newPublicationExport.blobFormat = encodeBlob(convertObjectsToBytes)
        newPublicationExport.publication = publication

        publicationExportRepository.persistAndFlush(newPublicationExport)

        return newPublicationExport.ref
    }

    fun decodeBlob(publicationExport: PublicationExport): ArrayList<*> {

        val blob = publicationExport.blobFormat

        val bytes = generateBytes(blob)

        return convertFromBytes(bytes)
    }


    fun encodeBlob(byteArrayOutputStream: ByteArrayOutputStream): Blob {

        return BlobProxy.generateProxy(byteArrayOutputStream.toByteArray())
    }

    fun generateBytes(blob: Blob): ByteArray {

        val bytes = blob.getBytes(1, blob.length().toInt())

        blob.free()

        return bytes
    }

    @Throws(IOException::class)
    fun convertToBytes(
        project: Project,
        publication: Publication
    ): ByteArrayOutputStream {

        val byteArrayOutputStream = ByteArrayOutputStream()

        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

        val list = generateListOfOutputObjects(project, publication)

        //converting
        /**
         *
         * TODO
         * include specifications & answer
         *
         */
        objectOutputStream.use {
            objectOutputStream.writeObject(list)
            objectOutputStream.flush()
            objectOutputStream.close()
        }

        return byteArrayOutputStream

    }


    //TODO delete
    @Throws(IOException::class)
    fun convertToBytesMethod2(
        project: Project,
        publication: Publication
    ): ByteArrayOutputStream {

        val byteArrayOutputStream = ByteArrayOutputStream()

        val objectOutputStream: ObjectOutputStream

        try {
            objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

            objectOutputStream.writeObject(generateListOfOutputObjects(project, publication))

            objectOutputStream.writeObject(project)

            objectOutputStream.flush()
            return byteArrayOutputStream
        } finally {
            try {
                byteArrayOutputStream.close()
            } catch (e: IOException) {
                throw IOException("Feil med outputstream. Feilmelding: $e")
            }
        }
    }


    /**
     *  TODO
     *  List of SEALED CLASS?
     */
    fun generateListOfOutputObjects(
        project: Project,
        publication: Publication
    ): ArrayList<Any> {

        val listAsOutput = ArrayList<Any>()

        listAsOutput.add(project)
        listAsOutput.add(publication)

        return listAsOutput
    }


    /**
     *
     * TODO fix
     * Getting following exception:
     * InvalidClassException: org.kravbank.domain.Project; no valid constructor
     *
     */

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun convertFromBytes(byteArray: ByteArray): ArrayList<*> {

        val bis = ByteArrayInputStream(byteArray)
        val objectInputStream = ObjectInputStream(bis)

        //converting
        val objs = objectInputStream.use {
            objectInputStream.readObject() as ArrayList<*>
        }
        return objs
    }
}