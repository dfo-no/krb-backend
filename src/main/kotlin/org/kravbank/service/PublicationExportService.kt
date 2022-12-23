package org.kravbank.service

import org.kravbank.domain.Project
import org.kravbank.domain.PublicationExport
import org.kravbank.repository.PublicationExportRepository
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.util.*


class PublicationExportService(
    var project: Project,
    val publicationExportRepository: PublicationExportRepository
) {

    fun saveBlob(): ByteArray { //TODO skal være void

        val byteArrayOutputStream = ByteArrayOutputStream()
        val byteArrayOutputStreamToByteArray = byteArrayOutputStream.toByteArray()
        val objectOutputStream: ObjectOutputStream

        try {
            objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

            objectOutputStream.writeObject(project)

            objectOutputStream.flush()

            print(byteArrayOutputStreamToByteArray)

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
        publicationExport.content = byteArrayOutputStreamToByteArray

        return publicationExport.content

        //publicationExportRepository.persistAndFlush(publicationExport)

    }

    fun getBlob(byteArray: ByteArray) {

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
}


fun main() {


    //   val fileOutputStream = FileOutputStream("newFile.txt")
    //val objectOutputStream = ObjectOutputStream(fileOutputStream)


    val project = Project()
    val publicationExportRepository = PublicationExportRepository()
    val pes = PublicationExportService(project = project, publicationExportRepository = publicationExportRepository)

    val lagreByteArray = pes.saveBlob()

    //val hentByteArray = pes.getBlob(lagreByteArray)

    // print(hentByteArray)


}