package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Product
import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductRepository: PanacheRepository<Product> {
    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Product {
        val product =
            find(
                "ref = ?1 and project_id_fk = ?2",
                ref,
                projectId
            ).firstResult<Product>()
        return Optional.ofNullable(product).orElseThrow { NotFoundException("Product not found") }
    }

    @Throws(BackendException::class)
    fun listAllProducts(id: Long): MutableList<Product> {
        return find("project_id_fk", id).list<Product>()
    }

    @Throws(BackendException::class)
    fun createProduct(product: Product) {
        persistAndFlush(product)
        if (!product.isPersistent) {
            throw BadRequestException("Bad request! Product not created")
        }
    }

    @Throws(BackendException::class)
    fun deleteProduct(projectId: Long, productRef: String) {
        val deleted: Boolean
        val found = findByRef(projectId, productRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException("Bad request! Product not deleted")
    }

    @Throws(BackendException::class)
    fun updateProduct(id: Long, product: Product) {
        val updated = update(
            "title = ?1, description= ?2 where id = ?3",
            product.title,
            product.description,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Fail! Product did not update") }
    }
}