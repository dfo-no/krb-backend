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
    @Path("/{productRef}")
    fun getProduct(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productRef") productRef: String,
    ): ProductForm {
        val product = productService.get(projectRef, productRef)
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
    @Path("/{productRef}")
    @Transactional
    fun deleteProduct(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productRef") productRef: String
    ): Response {
        productService.delete(projectRef, productRef)
        return Response.noContent().build()
    }

    @PUT
    @Path("/{productRef}")
    @Transactional
    fun updateProduct(
        @PathParam("projectRef") projectRef: String,
        @PathParam("productRef") productRef: String,
        updatedProduct: ProductForm
    ): ProductForm {
        val product = productService.update(projectRef, productRef, updatedProduct)
        return ProductForm().fromEntity(product)
    }
}

