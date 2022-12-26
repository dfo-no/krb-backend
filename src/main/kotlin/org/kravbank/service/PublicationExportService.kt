package org.kravbank.service

import org.hibernate.engine.jdbc.BlobProxy
import org.kravbank.domain.Project
import org.kravbank.domain.PublicationExport
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.PublicationExportRepository
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationExportService(
    private val publicationExportRepository: PublicationExportRepository
) {


    //TODO split save blob and create entity

    fun saveBlob(
        project: Project
    ): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        val byteArrayOutputStreamToByteArray = byteArrayOutputStream.toByteArray()
        val objectOutputStream: ObjectOutputStream

        try {
            objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

            objectOutputStream.writeObject(project)

            objectOutputStream.flush()

            //    print(byteArrayOutputStreamToByteArray)

            //     print("\n iterate through byte array \n")
            //    for (b in byteArrayOutputStreamToByteArray) {
            //       print(b)
            // }

        } finally {
            try {
                byteArrayOutputStream.close()
            } catch (e: IOException) {
                println(e.message)
            }
        }

        val publicationExport = PublicationExport()

        publicationExport.ref = UUID.randomUUID().toString()
        publicationExport.blobFormat = BlobProxy.generateProxy(byteArrayOutputStreamToByteArray)

        publicationExportRepository.persistAndFlush(publicationExport)

        return publicationExport.ref


    }

    fun get(ref: String): PublicationExport {


        val export = publicationExportRepository.findByRef(ref)

        print(export)


        return export

        /*   val byteArrayInputStream: ByteArrayInputStream = ByteArrayInputStream(byteArray)

        var objectInput: ObjectInput? = null

        lateinit var obj: Any

        try {
            objectInput = ObjectInputStream(byteArrayInputStream)

            obj = objectInput.readObject()

        } finally {
            try {
                objectInput?.close()
            } catch (e: IOException) {
                println(e.message)
            }
        }

        print(obj)
    }

      */
    }

    fun list(): MutableList<PublicationExport> {


        val publicationExport = publicationExportRepository.findAll().stream<PublicationExport>()


        return Optional.ofNullable(publicationExport.toList()).orElseThrow { NotFoundException("Nothing here...") }
    }
}


fun main() {

    /*

     //   val fileOutputStream = FileOutputStream("newFile.txt")
     //val objectOutputStream = ObjectOutputStream(fileOutputStream)

     val project = Project()
     val need = Need()

     val publicationExportRepository = PublicationExportRepository()
     val pes = PublicationExportService(
         project = project,
         publicationExportRepository = publicationExportRepository,
         need = need
     )

     //save
     val lagreByteArray = pes.saveBlob()


     //get
     //val hentByteArray = pes.getBlob(lagreByteArray)
     // print(hentByteArray)


     */

}