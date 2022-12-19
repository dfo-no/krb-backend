package org.kravbank.resource

import com.lowagie.text.pdf.PRStream
import com.lowagie.text.pdf.PdfDictionary
import com.lowagie.text.pdf.PdfName
import com.lowagie.text.pdf.PdfReader
import org.jboss.resteasy.annotations.providers.multipart.PartType
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import javax.ws.rs.FormParam
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/v1/unwrap")
class UnwrapResource {

    @POST
    @Path(value = "/uploadPdf")
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    fun generatePdf(file: InputStream): Response {

        if (!file.markSupported()) {
            return Response.ok("Error").build()
        }
        var response = ""
        try {
            val reader = PdfReader(file)
            val root = reader.catalog
            val documentnames = root.getAsDict(PdfName.NAMES)
            val embeddedfiles = documentnames.getAsDict(PdfName.EMBEDDEDFILES)
            val filespecs = embeddedfiles.getAsArray(PdfName.NAMES)
            var filespec: PdfDictionary
            var refs: PdfDictionary
            var stream: PRStream


            // TODO: This is really a hack. This will only work if the file contains only
            // one attachemnt, and that attachment is json. There is no check if the file
            // contains a virus etc.
            var i = 0
            while (i < filespecs.size()) {
                filespecs.getAsString(i++)
                filespec = filespecs.getAsDict(i++)
                refs = filespec.getAsDict(PdfName.EF)
                for (key in refs.keys) {
                    stream = PdfReader.getPdfObject(refs.getAsIndirectObject(key)) as PRStream
                    response = String(PdfReader.getStreamBytes(stream), StandardCharsets.UTF_8)
                }
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Response.ok(response).build()
    }
}