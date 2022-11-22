package org.kravbank.resource.pdf

import org.kravbank.service.pdf.java.XsltService
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/api/v1/pdf")
class TransformResource {
    @GET
    @Path("/transform")
    fun xml(): Response {
        val xmlService = XsltService()
        xmlService.transform()
        return Response.ok("All is vell").build()
    }
}