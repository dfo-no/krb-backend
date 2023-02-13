package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.frontend.Bank
import org.kravbank.service.BankService
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/api/v1/banks/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@Authenticated
class BankResource {

    @Inject
    private lateinit var bankService: BankService

    @GET
    fun get(
    ): List<Bank> = bankService.get()

}
