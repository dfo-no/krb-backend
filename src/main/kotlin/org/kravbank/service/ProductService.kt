package org.kravbank.service

import org.kravbank.dao.ProductForm
import org.kravbank.domain.Product
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.ProductRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementVariantRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductService(
    val productRepository: ProductRepository,
    val projectRepository: ProjectRepository,
    val requirementVariantRepository: RequirementVariantRepository,
) {

    @Throws(BackendException::class)
    fun get(projectRef: String, productRef: String): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        return productRepository.findByRef(foundProject.id, productRef)
    }

    @Throws(BackendException::class)
    fun list(projectRef: String): List<Product> {
        val foundProject = projectRepository.findByRef(projectRef)
        return productRepository.listAllProducts(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newProduct: ProductForm): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundReqVariant = requirementVariantRepository.findByRefProduct(newProduct.requirementVariantRef)
        val product = ProductForm().toEntity(newProduct)
        product.project = foundProject
        product.requirementvariant = foundReqVariant
        productRepository.createProduct(product)
        return product
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, productRef: String): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProduct = productRepository.findByRef(foundProject.id, productRef)
        val deleted = productRepository.deleteProduct(foundProduct.id)
        if (deleted) return foundProduct
        else throw BadRequestException("Bad request! Product was not deleted!")
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, productRef: String, updatedProduct: ProductForm): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProduct = productRepository.findByRef(foundProject.id, productRef)
        val update = ProductForm().toEntity(updatedProduct)
        productRepository.updateProduct(foundProduct.id, update)
        return update.apply { ref = foundProduct.ref }
    }
}