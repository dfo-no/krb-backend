package org.kravbank.resource.pdf

import com.fasterxml.jackson.databind.JsonNode
import com.itextpdf.text.pdf.codec.Base64
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.jboss.resteasy.annotations.cache.NoCache
import org.kravbank.service.pdf.PdfService
import java.io.ByteArrayInputStream
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM
import javax.ws.rs.core.Response

@Path("/api/v1/pdf")
class DownloadResource {

    @POST
    @Path(value = "/generateSpecification")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_OCTET_STREAM) //PDF
    @NoCache
    fun generateSpecification(@RequestBody jsonNode: JsonNode): Response {
        val bis: ByteArrayInputStream = PdfService.generateSpecification(jsonNode)

        return Response
            .ok(Base64.InputStream(bis))
            .header("CONTENT_DISPOSITION", "attachment; filename=report.pdf") // Headers for Swagger UI.
            .header("CONTENT_TYPE", "APPLICATION_PDF_VALUE")
            .header("PRAGMA", "public")
            .header("ACCESS_CONTROL_ALLOW_ORIGIN", "*")
            .build()
    }

    @POST
    @Path(value = "/generateResponse")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_OCTET_STREAM)
    @NoCache
    fun generateResponse(@RequestBody jsonNode: JsonNode): Response {
        val bis: ByteArrayInputStream = PdfService.generateResponse(jsonNode)
        return Response
            .ok(Base64.InputStream(bis))
            .header("CONTENT_DISPOSITION", "attachment; filename=report.pdf") // Headers for Swagger UI.
            .header("CONTENT_TYPE", "APPLICATION_PDF_VALUE")
            .header("PRAGMA", "public")
            .header("ACCESS_CONTROL_ALLOW_ORIGIN", "*")
            .build()
    }

    @POST
    @Path(value = "/generatePrefilledResponse")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_OCTET_STREAM)
    @NoCache
    fun generatePrefilledResponse(@RequestBody jsonNode: JsonNode): Response {
        val bis: ByteArrayInputStream = PdfService.generatePrefilledResponse(jsonNode)
        return Response
            .ok(Base64.InputStream(bis))
            .header("CONTENT_DISPOSITION", "attachment; filename=report.pdf") // Headers for Swagger UI.
            .header("CONTENT_TYPE", "APPLICATION_PDF_VALUE")
            .header("PRAGMA", "public")
            .header("ACCESS_CONTROL_ALLOW_ORIGIN", "*")
            .build()
    }
}