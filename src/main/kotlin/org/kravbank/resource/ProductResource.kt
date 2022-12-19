package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.dao.ProductForm
import org.kravbank.service.ProductService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/v1/projects/{projectRef}/products")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
class ProductResource(val productService: ProductService) {

    @GET
    @Path("/{productref}")
    fun getProduct(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productref") productref: String,
    ): ProductForm {
        val product = productService.get(projectRef, productref)
        return ProductForm().fromEntity(product)
    }

    @GET
    fun listProducts(@PathParam("projectRef") projectRef: String): List<ProductForm> {
        return productService.list(projectRef)
            .stream()
            .map(ProductForm()::fromEntity)
            .toList()
    }

    @Transactional
    @POST
    fun createProduct(@PathParam("projectRef") projectRef: String, newProduct: ProductForm): Response {
        val product = productService.create(projectRef, newProduct)
        //returnerer nytt product ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/products/" + product.ref))
            .build()
    }

    @DELETE
    @Path("/{productref}")
    @Transactional
    fun deleteProduct(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productref") productref: String
    ): Response {
        val product = productService.delete(projectRef, productref)
        val form = ProductForm().fromEntity(product)
        // returnerer slettet product ref i body
        return Response.ok(form.ref).build()
    }

    @PUT
    @Path("{productref}")
    @Transactional
    fun updateProduct(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productref") productref: String,
        updatedProduct: ProductForm
    ): ProductForm {
        val product = productService.update(projectRef, productref, updatedProduct)
        return ProductForm().fromEntity(product)
    }
}

