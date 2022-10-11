package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import org.kravbank.utils.form.product.ProductForm
import org.kravbank.utils.form.product.ProductFormUpdate
import org.kravbank.repository.ProductRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.mapper.product.ProductMapper
import org.kravbank.utils.mapper.product.ProductUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class ProductService(
    val productRepository: ProductRepository,
    val projectRepository: ProjectRepository
) {

    // @CacheResult(cacheName = "product-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, productRef: String): Response {
        val project = projectRepository.findByRef(projectRef)
        val foundProduct = productRepository.findByRef(project.id, productRef)
        val mappedProduct = ProductMapper().fromEntity(foundProduct)
        return Response.ok(mappedProduct).build()
    }

    //@CacheResult(cacheName = "product-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProducts = productRepository.listAllProducts(foundProject.id)
        val productsDTO = ArrayList<ProductForm>()
        for (p in foundProducts) productsDTO.add(ProductMapper().fromEntity(p))
        return Response.ok(productsDTO).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, productForm: ProductForm): Response {
        val project = projectRepository.findByRef(projectRef)
        productForm.project = project
        val product = ProductMapper().toEntity(productForm)
        productRepository.createProduct(product)
        return Response.created(URI.create("/api/v1/projects/$projectRef/products/" + product.ref)).build()
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, productRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        productRepository.deleteProduct(foundProject.id, productRef)
        return Response.noContent().build()
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, productRef: String, productForm: ProductFormUpdate): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProduct = productRepository.findByRef(foundProject.id, productRef)
        val product = ProductUpdateMapper().toEntity(productForm)
        productRepository.updateProduct(foundProduct.id, product)
        return Response.ok(productForm).build()
    }
}