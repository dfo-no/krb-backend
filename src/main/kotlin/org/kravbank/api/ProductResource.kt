package org.kravbank.api

import org.kravbank.domain.Product
import org.kravbank.service.ProductService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


//@Tags(value = [Tag(name = "Read products", description = "Read uploaded products.")])
@Path("/products")
@RequestScoped
//@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
//@Authenticated

class ProductResource (val productService: ProductService){

   // @Inject
  //  lateinit var projectRepo : ProjectRepository

    //@Operation(summary = "List all products")

    //GET ONE
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    fun getProject(@PathParam("id") id : Long): Response {
        if (productService.exists(id)){
            return Response.ok(productService.getProduct(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //GET ALL
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listProducts():List<Product> =
        productService.listProducts();


    //CREATE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createProduct(product: Product): Response {

        //try catch
        productService.createProduct(product)
        if (product.isPersistent){
            return Response.created(URI.create("/products" + product.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
    }

    //DELETE
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteProdudctById(@PathParam("id") id: Long): Response {
        val deleted = productService.deleteProduct(id)
        return if (deleted) {
           //println(deleted)
            Response.noContent().build()
        } else Response.status(Response.Status.BAD_REQUEST).build()
    }

    //UPDATE

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateProduct(@PathParam("id") id: Long, product: Product): Response {
        if (productService.exists(id)) {
            productService.updateProduct(id, product)
            return Response.ok(productService.getProduct(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }


}