package org.kravbank.service

import org.kravbank.dao.ProductForm
import org.kravbank.domain.Product
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.ProductRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.Messages.RepoErrorMsg.PRODUCT_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class ProductService(
    val productRepository: ProductRepository,
    val projectRepository: ProjectRepository,
) {

    fun get(projectRef: String, productRef: String): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        return productRepository.findByRef(foundProject.id, productRef)
    }

    fun list(projectRef: String): List<Product> {
        val foundProject = projectRepository.findByRef(projectRef)
        return productRepository.listAllProducts(foundProject.id)
    }


    @Throws(BackendException::class)
    fun create(projectRef: String, newProduct: ProductForm): Product {

        val foundProject = projectRepository.findByRef(projectRef)

        return ProductForm().toEntity(newProduct).apply {
            project = foundProject
        }.also {
            productRepository.persistAndFlush(it)
            if (!productRepository.isPersistent(it)) throw BadRequestException(PRODUCT_BADREQUEST_CREATE)
        }
    }

    fun delete(projectRef: String, productRef: String): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProduct = productRepository.findByRef(foundProject.id, productRef)

        //TODO product in variants?
//        val productUsedInVariant =
//            requirementVariantRepository.listAllRequirementVariants(foundProject.id)
//                .map { p -> p.product }
//                .any { p -> p?.any()?.equals(foundProduct) == true }
//
//        if (productUsedInVariant) throw BackendException("Cannot delete. It is in use elsewhere")


        // TODO
        // pga deleteById bulksletter uavhengig av regler og relasjoner er det greit å sjekke om produkt inngår i varianter
        // samme med prosjekter ?
        productRepository.deleteById(foundProduct.id)

        return foundProduct
    }

    fun update(projectRef: String, productRef: String, updatedProduct: ProductForm): Product {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProduct = productRepository.findByRef(foundProject.id, productRef)

        val update = ProductForm().toEntity(updatedProduct)
        productRepository.updateProduct(foundProduct.id, update)
        return update.apply { ref = foundProduct.ref }
    }
}