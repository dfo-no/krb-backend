package org.kravbank.java.api;



import org.kravbank.java.model.Project;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/projects")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

public class ProjectResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Project> projects = Project.listAll();
        return Response.ok(projects).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") Long id) {
        return Project.findByIdOptional(id)
                .map(project -> Response.ok(project).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }



    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Project project){
        Project.persist((project));
        if(project.isPersistent()){
            return Response.created(URI.create("/projects" + project.id)).build();
        }
        return Response.status (Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path ("project/{publishedDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByPublishedDate(@PathParam("publishedDate") String publishedDate) {
        List <Project> projects = Project.list("SELECT p from Project p WHERE p.publishedDate = ?1 ORDER BY id DESC", publishedDate);
        return Response.ok(projects).build();
    }

    @GET
    @Path ("projectid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByProjectId(@PathParam("id") String id) {
        return Project.find("projectId", id)
                .singleResultOptional()
                .map(project -> Response.ok(project).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteById(@PathParam("id") Long id){
        boolean deleted = Project.deleteById(id);
        if (deleted){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
