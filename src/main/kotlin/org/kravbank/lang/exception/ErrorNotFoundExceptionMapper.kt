package org.kravbank.lang.exception

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ErrorNotFoundExceptionMapper : ExceptionMapper<NotFoundException> {

    override fun toResponse(exception: NotFoundException): Response {
        return Response.status(Response.Status.NOT_FOUND).entity(exception.message).build()
    }
}