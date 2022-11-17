package org.kravbank.resource.pdf

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/api/v1/pdf")
class PingResource {

    @Path("/ping")
    @GET
    fun getPing(): Response {
        val date = Date()
        val strDateFormat = "HH:mm:ss"
        val dateFormat: DateFormat = SimpleDateFormat(strDateFormat)
        val formattedDate = dateFormat.format(date)
        return Response.ok("pong 18 $formattedDate").build()
    }
}