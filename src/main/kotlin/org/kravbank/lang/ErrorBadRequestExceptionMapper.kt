package org.kravbank.lang

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ErrorBadRequestExceptionMapper : ExceptionMapper<BadRequestException> {

    override fun toResponse(exception: BadRequestException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.message).build()
    }
}