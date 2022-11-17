package org.kravbank.resource.pdf

import com.fasterxml.jackson.databind.JsonNode
import com.itextpdf.text.pdf.codec.Base64
import io.vertx.mutiny.core.http.HttpHeaders.headers
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.jboss.resteasy.annotations.cache.NoCache
import org.kravbank.service.pdf.PdfService
import java.io.ByteArrayInputStream
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM
import javax.ws.rs.core.Response

@Path("/api/v1/pdf")
class DownloadResource {

    @POST
    @Path(value = "/generateSpecification")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_OCTET_STREAM)
    @NoCache
    fun generateSpecification(@RequestBody jsonNode: JsonNode?): Response {
        val bis: ByteArrayInputStream = PdfService.generateSpecification(jsonNode)

        /**
         * Todo: Utkommentert kode er fra den originale java-filen. Slettes hvis migrering ok.
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
        headers.setCacheControl(CacheControl.noCache());
        headers.add(HttpHeaders.PRAGMA, "public");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
        .body(new InputStreamResource(bis));
         */
        headers().add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
        headers().add(HttpHeaders.ALLOW, "*")
        return Response.ok(Base64.InputStream(bis)).build() //todo: blir headers satt i Responsen?
    }

    @POST
    @Path(value = "/generateResponse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @NoCache
    fun generateResponse(@RequestBody jsonNode: JsonNode?): Response {
        val bis: ByteArrayInputStream = PdfService.generateResponse(jsonNode)
        headers().add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
        headers().add(HttpHeaders.ALLOW, "*")
        return Response.ok(Base64.InputStream(bis)).build()
    }

    @POST
    @Path(value = "/generatePrefilledResponse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @NoCache
    fun generatePrefilledResponse(@RequestBody jsonNode: JsonNode?): Response {
        val bis: ByteArrayInputStream = PdfService.generatePrefilledResponse(jsonNode)
        headers().add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
        headers().add(HttpHeaders.ALLOW, "*")
        return Response.ok(Base64.InputStream(bis)).build()
    }
}