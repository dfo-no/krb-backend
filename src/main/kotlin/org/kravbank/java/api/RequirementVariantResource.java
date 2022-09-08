package org.kravbank.java.api;


import org.kravbank.java.model.RequirementVariant;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/requirementvariants")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

public class RequirementVariantResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<RequirementVariant> requirementvariants = RequirementVariant.listAll();
        return Response.ok(requirementvariants).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") Long id) {
        return RequirementVariant.findByIdOptional(id)
                .map(requirement -> Response.ok(requirement).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(RequirementVariant requirement) {
        RequirementVariant.persist((requirement));
        if (requirement.isPersistent()) {
            return Response.created(URI.create("/requirementvariants" + requirement.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @GET
    @Path ("requirementvariant/{useproduct}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUseProduct(@PathParam("useproduct") String useproduct) {
        boolean useProductBool = useproduct.contentEquals("true");
        List <RequirementVariant> requirementvariants = RequirementVariant.list("SELECT p from RequirementVariant p WHERE p.useProduct = ?1 ORDER BY id DESC", useProductBool);
        return Response.ok(requirementvariants).build();
    }


    /*
    @GET
    @Path ("requirement/{needid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByBankId(@PathParam("needid") String needid) {
        return RequirementVariant.find("needId", needid)
                .singleResultOptional()
                .map(requirement -> Response.ok(requirement).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    */

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteById(@PathParam("id") Long id){
        boolean deleted = RequirementVariant.deleteById(id);
        if (deleted){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}

