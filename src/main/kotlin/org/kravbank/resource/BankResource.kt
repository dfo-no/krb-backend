package org.kravbank.resource

import org.kravbank.frontend.Bank
import org.kravbank.service.BankService
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/api/v1/banks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
//@Authenticated
class BankResource {

    @Inject
    private lateinit var bankService: BankService

    @GET
    fun get(
        @DefaultValue("500") @QueryParam("pageSize") pageSize: Int,
        @DefaultValue("0") @QueryParam("page") page: Int,
        @DefaultValue("title") @QueryParam("fieldName") fieldName: String,
        @DefaultValue("ASC") @QueryParam("order") order: String
    ): List<Bank> {

        return bankService.get(pageSize, page, fieldName, order)

    }
}

