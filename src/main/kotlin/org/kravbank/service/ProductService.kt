package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.domain.Product
import org.kravbank.exception.BackendException
import org.kravbank.utils.form.product.ProductForm
import org.kravbank.utils.form.product.ProductFormUpdate
import org.kravbank.repository.ProductRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.form.product.ProductFormCreate
import org.kravbank.utils.mapper.product.ProductCreateMapper
import org.kravbank.utils.mapper.product.ProductMapper
import org.kravbank.utils.mapper.product.ProductUpdateMapper
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductService(
    val productRepository: ProductRepository,
    val projectRepository: ProjectRepository,
    val requirementVariantRepository: RequirementVariantRepository,
    val requirementService: RequirementService,
    val requirementVariantService: RequirementVariantService

) {

    // @CacheResult(cacheName = "product-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, productRef: String): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        return productRepository.findByRef(foundProject.id, productRef)
    }

    //@CacheResult(cacheName = "product-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): List<Product> {
        val foundProject = projectRepository.findByRef(projectRef)
        return productRepository.listAllProducts(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newProduct: ProductFormCreate): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        newProduct.project = foundProject
        val product = ProductCreateMapper(newProduct.requirementvariant).toEntity(newProduct)
        productRepository.createProduct(product)
        return product
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, productRef: String): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProduct = productRepository.findByRef(foundProject.id, productRef)
        productRepository.deleteProduct(foundProduct.id)
        return foundProduct
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, productRef: String, updatedProduct: ProductFormUpdate): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProduct = productRepository.findByRef(foundProject.id, productRef)
        val product = ProductUpdateMapper().toEntity(updatedProduct)
        productRepository.updateProduct(foundProduct.id, product)
        return product
    }
}