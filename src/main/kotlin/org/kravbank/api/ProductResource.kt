package org.kravbank.api

import org.kravbank.domain.ProductKtl
import org.kravbank.java.model.Product
import org.kravbank.service.ProductService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


//@Tags(value = [Tag(name = "Read products", description = "Read uploaded products.")])
@Path("/kt")
@RequestScoped
//@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
//@Authenticated

class ProductResource (val productService: ProductService){

   // @Inject
  //  lateinit var projectRepo : ProjectRepository

    //@Operation(summary = "List all products")
    @Produces("application/json")
    @Path("products/")
    @GET
    fun listProjects():MutableList<ProductKtl> =
        productService.listProducts();

    @Transactional
    @Produces("application/json")
    @Path("products/")
    @POST
    fun createProduct(productKtl: ProductKtl): Response {

        //try catch
        productService.createProduct(productKtl)
        if (productKtl.isPersistent){
            return Response.created(URI.create("/products" + productKtl.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
    }

    @DELETE
    @Path("product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteProdudctById(@PathParam("id") id: Long): Response {
        val deleted = productService.deleteProduct(id)
        return if (deleted) {
           //println(deleted)
            Response.noContent().build()
        } else Response.status(Response.Status.BAD_REQUEST).build()
    }
}