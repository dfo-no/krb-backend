package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Product
import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductRepository(val projectRepository: ProjectRepository) : PanacheRepository<Product> {

    fun deleteByRef(ref:String)  = find("ref", ref).firstResult<Product>().delete()

    @Throws(BackendException::class)
    fun findByRef(ref: String): Product {
        //val foundProjectProducts = projectRepository.findByRef(ref).products
        val found = find("ref", ref).firstResult<Product>()
        return Optional.ofNullable(found).orElseThrow { NotFoundException("Product not found by ref: $ref") }
    }

    @Throws(BackendException::class)
    fun listAllProducts(projectRef: String): MutableList<Product> {
        //val foundProjectProducts = projectRepository.findByRef(projectRef).products
        val foundProjectsProductsRepo = find("projectRef", projectRef).list<Product>()

        if (foundProjectsProductsRepo.isNotEmpty()) return foundProjectsProductsRepo else throw NotFoundException("No products found")
    }

    @Throws(BackendException::class)
    fun createProduct(product: Product) {
        persistAndFlush(product)
        if (!product.isPersistent) throw BadRequestException("Fail! Product not created")
    }

    @Throws(BackendException::class)
    fun deleteProduct(projectRef: String) {
        val deleted: Boolean
        val found = findByRef(projectRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException("Fail! Product not deleted")
    }

    @Throws(BackendException::class)
    fun updateProduct(projectRef: String, project: Product) {
        val foundProduct = findByRef(projectRef)
        val updated = update(
            "title = ?1, description= ?2 where id = ?3",
            project.title,
            project.description,
            foundProduct.id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Fail! Product did not update") }
    }


}