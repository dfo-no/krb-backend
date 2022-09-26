package org.kravbank.api

import org.kravbank.domain.Product
import org.kravbank.form.product.ProductForm
import org.kravbank.service.ProductService
import org.kravbank.service.ProjectService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


//@Tags(value = [Tag(name = "Read products", description = "Read uploaded products.")])
//@Path("/products")
@Path("/api/v1/projects/{projectref}/products")
@RequestScoped
//@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
//@Authenticated

class ProductResource(val productService: ProductService, val projectService: ProjectService) {
    //GET PROJECT
    //@Operation(summary = "List all products")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{productref}")
    fun getProduct(
        @PathParam("productref") productref: String,
        @PathParam("projectref") projectref: String,
    ): Response =
        productService.getProductByRefFromService(projectref, productref)

    //LIST PRODUCTS
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listProducts(@PathParam("projectref") projectref: String): Response =
        productService.listProductsFromService(projectref)


    //CREATE PRODUCT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createProduct(@PathParam("projectref") projectref: String, product: ProductForm): Response =
        productService.createProductFromService(projectref, product)


    //DELETE PRODUCT
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

    //UPDATE PRODUCT
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

