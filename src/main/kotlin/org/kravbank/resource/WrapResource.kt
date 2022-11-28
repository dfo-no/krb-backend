package org.kravbank.resource

import com.google.common.io.ByteStreams
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.kravbank.service.WrapperService
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM
import javax.ws.rs.core.Response

@Path("/api/v1/wrap")
class WrapResource {

    @Inject
    lateinit var wrapperService: WrapperService

    @POST
    @Path(value = "/specification")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_OCTET_STREAM)
    fun generateSpecification(@RequestBody json: InputStream): Response {
        val pdf: InputStream = wrapperService.createPdf(json)

        return Response
            .ok(Base64.getEncoder().encode(ByteStreams.toByteArray(pdf)))
            .header("Content-Disposition", "attachment; filename=specification.pdf")
            .header("Access-Control-Allow-Origin", "*")
            .header("Pragma", "public")
            .build()
    }

    @POST
    @Path(value = "/report")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_OCTET_STREAM)
    fun generateResponse(@RequestBody json: InputStream): Response {
        val pdf: InputStream = wrapperService.createPdf(json)

        return Response
            .ok(Base64.getEncoder().encode(ByteStreams.toByteArray(pdf)))
            .header("Content-Disposition", "attachment; filename=report.pdf")
            .header("Access-Control-Allow-Origin", "*")
            .header("Pragma", "public")
            .build()
    }

    @POST
    @Path(value = "/prefilled")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_OCTET_STREAM)
    fun generatePrefilledResponse(@RequestBody json: InputStream): Response {
        val pdf: InputStream = wrapperService.createPdf(json)

        return Response
            .ok(Base64.getEncoder().encode(ByteStreams.toByteArray(pdf)))
            .header("Content-Disposition", "attachment; filename=prefilled.pdf")
            .header("Access-Control-Allow-Origin", "*")
            .header("Pragma", "public")
            .build()
    }
}