package org.kravbank.api

import org.kravbank.domain.ProductKtl
import org.kravbank.service.ProductService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
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
}