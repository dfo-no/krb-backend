package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.dao.RequirementVariantForm
import org.kravbank.service.RequirementVariantService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/v1/projects/{projectRef}/needs/{needRef}/requirements/{requirementRef}/requirementvariants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@Authenticated
class RequirementVariantResource(val requirementVariantService: RequirementVariantService) {

    @GET
    @Path("/{requirementVariantRef}")
    fun getRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String
    ): RequirementVariantForm {
        val reqVariant = requirementVariantService.get(projectRef, needRef, requirementRef, requirementVariantRef)
        return RequirementVariantForm().fromEntity(reqVariant)
    }

    @GET
    fun listRequirementVariants(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        @PathParam("requirementRef") requirementRef: String,
    ): List<RequirementVariantForm> {
        return requirementVariantService.list(projectRef, needRef, requirementRef)
            .stream()
            .map(RequirementVariantForm()::fromEntity)
            .toList()
    }

    @Transactional
    @POST
    fun createRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        @PathParam("requirementRef") requirementRef: String,
        newReqVariant: RequirementVariantForm
    ): Response {
        val reqVariant = requirementVariantService.create(projectRef, needRef, requirementRef, newReqVariant)

        //returnerer ny req variant ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/requirements/$requirementRef/requirementvariants/" + reqVariant.ref))
            .build()
    }

    @DELETE
    @Path("/{requirementVariantRef}")
    @Transactional
    fun deleteRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String
    ): Response {
        requirementVariantService.delete(projectRef, needRef, requirementRef, requirementVariantRef)
        return Response.noContent().build()
    }

    @PUT
    @Path("{requirementVariantRef}")
    @Transactional
    fun updateRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String,
        updatedReqVariant: RequirementVariantForm
    ): RequirementVariantForm {
        val reqVariant =
            requirementVariantService.update(
                projectRef,
                needRef,
                requirementRef,
                requirementVariantRef,
                updatedReqVariant
            )
        return RequirementVariantForm().fromEntity(reqVariant)
    }
}