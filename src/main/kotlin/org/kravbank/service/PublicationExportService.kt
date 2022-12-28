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


    fun get(
        projectRef: String,
        publicationRef: String,
        publicationExportRef: String
    ): PublicationExport {

        val project = projectRepository.findByRef(projectRef)

        val publicationId = publicationRepository.findByRef(project.id, publicationRef).id

        return publicationExportRepository.findByRef(publicationId, publicationExportRef)

        // return decodeBlob(publicationExport)


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

    fun decodeBlob(publicationExport: PublicationExport): ArrayList<Any> {

        println("start decode blob")

        val blob = publicationExport.blobFormat
        val bytes = generateBytes(blob)
        val fromBytesToList = convertFromBytes(bytes)


        println("Inside decode blob $fromBytesToList")

        return fromBytesToList
    }


    fun encodeBlob(byteArrayOutputStream: ByteArrayOutputStream): Blob {

        println("\n inside encode blob : $byteArrayOutputStream \n")
        return BlobProxy.generateProxy(byteArrayOutputStream.toByteArray())
    }

    fun generateBytes(blob: Blob): ByteArray {

        val bytes = blob.getBytes(1, blob.length().toInt())

        blob.free()

        println("inside generateBytes")

        return bytes
    }

    @Throws(IOException::class)
    fun convertToBytes(
        project: Project,
        publication: Publication
    ): ByteArrayOutputStream {

        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream: ObjectOutputStream

        try {

            objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

            val list = generateListOfOutputObjects(project, publication)

            objectOutputStream.writeObject(list)
            objectOutputStream.flush()

            println("inside convert to bytes")
            return byteArrayOutputStream


            // TODO create this
            //create putput stream of specifications
            // create outputstream of answer


            // TODO Delete this
            // print("\n iterate through byte array \n")
            //    for (b in byteArrayOutputStream.toByteArray()) {
            //       print(b)
            // }


        } finally {
            try {
                byteArrayOutputStream.close()
            } catch (e: IOException) {
                println("INSIDE CATCH 1 $e.message")
            }
        }
    }


    /**
     *  TODO SEALED CLASS?
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


    @Throws(IOException::class, ClassNotFoundException::class)
    private fun convertFromBytes(byteArray: ByteArray): ArrayList<Any> {
        val bis = ByteArrayInputStream(byteArray)
        val objectInputStream = ObjectInputStream(bis)

        println("inside convert from bytes")
        return objectInputStream.use { objectInputStream.readObject() } as ArrayList<Any>
    }

}


fun main() {

    //val fileOutputStream = FileOutputStream("newFile.txt")
    //val objectOutputStream = ObjectOutputStream(fileOutputStream)
    /*
        val publication = Publication()
        publication.comment = "kommentrar hade"
        val project = Project()
        project.title = "Tittel hei"


        val pes = PublicationExportService(
            projectRepository = ProjectRepository(),
            publicationExportRepository = PublicationExportRepository(),
            publicationRepository = PublicationRepository()
        )

        //save

        val saveString = pes.save(project.ref, publication.ref)


        print(saveString)


     */
    //get


    // print(hentByteArray)


}