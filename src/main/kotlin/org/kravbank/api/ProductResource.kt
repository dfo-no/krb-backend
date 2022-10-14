package org.kravbank.api

import org.kravbank.utils.form.product.ProductForm
import org.kravbank.utils.form.product.ProductFormUpdate
import org.kravbank.service.ProductService
import org.kravbank.service.ProjectService
import org.kravbank.utils.form.product.ProductFormCreate
import org.kravbank.utils.mapper.product.ProductMapper
import org.kravbank.utils.mapper.product.ProductUpdateMapper
import java.net.URI
import java.util.ArrayList
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
    ): Response {
        val product = productService.get(projectRef, productref)
        //mapper fra entity
        val productDTO = ProductMapper().fromEntity(product)
        return Response.ok(productDTO).build()
    }

    //LIST PRODUCTS
    @GET
    fun listProducts(@PathParam("projectRef") projectRef: String): Response {
        val products = productService.list(projectRef)
        val productsDTO = ArrayList<ProductForm>()
        for (n in products) productsDTO.add(ProductMapper().fromEntity(n))
        return Response.ok(productsDTO).build()
    }

    //CREATE PRODUCT
    @Transactional
    @POST
    fun createProduct(@PathParam("projectRef") projectRef: String, newProduct: ProductFormCreate): Response {
        val product = productService.create(projectRef, newProduct)
        //sender ny product ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/products/" + product.ref)).build()
    }

    //DELETE PRODUCT
    @DELETE
    @Path("/{productref}")
    @Transactional
    fun deleteProdudctById(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productref") productref: String
    ): Response {
        val product = productService.delete(projectRef, productref)
        val productDTO = ProductMapper().fromEntity(product)
        // sender slettet product ref i body
        return Response.ok(productDTO.ref).build()
    }

    //UPDATE PRODUCT
    @PUT
    @Path("{productref}")
    @Transactional
    fun updateProduct(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productref") productref: String,
        updatedProduct: ProductFormUpdate
    ): Response {
        val product = productService.update(projectRef, productref, updatedProduct)
        // mapper fra entity
        val productUpdateDTO = ProductUpdateMapper().fromEntity(product)
        return Response.ok(productUpdateDTO).build()
    }
}

