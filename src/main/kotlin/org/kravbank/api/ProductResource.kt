package org.kravbank.api

import org.kravbank.utils.form.product.ProductForm
import org.kravbank.utils.form.product.ProductFormUpdate
import org.kravbank.service.ProductService
import org.kravbank.service.ProjectService
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

//@Tags(value = [Tag(name = "Read products", description = "Read uploaded products.")])
@Path("/api/v1/projects/{projectRef}/products")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
//@Authenticated
class ProductResource(val productService: ProductService, val projectService: ProjectService) {
    //GET PROJECT
    @GET
    @Path("/{productref}")
    fun getProduct(
        @PathParam("productref") productref: String,
        @PathParam("projectRef") projectRef: String,
    ): Response =
        productService.getProductByRefFromService(projectRef, productref)

    //LIST PRODUCTS
    @GET
    fun listProducts(@PathParam("projectRef") projectRef: String): Response =
        productService.list(projectRef)

    //CREATE PRODUCT
    @Transactional
    @POST
    fun createProduct(@PathParam("projectRef") projectRef: String, product: ProductForm): Response =
        productService.createProductFromService(projectRef, product)


    //DELETE PRODUCT
    @DELETE
    @Path("/{productref}")
    @Transactional
    fun deleteProdudctById(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productref") productref: String
    ): Response =
        productService.deleteProductFromService(projectRef, productref)

    //UPDATE PRODUCT
    @PUT
    @Path("{productref}")
    @Transactional
    fun updateProduct(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productref") productref: String,
        product: ProductFormUpdate
    ): Response =
        productService.updateProductFromService(projectRef, productref, product)
}

