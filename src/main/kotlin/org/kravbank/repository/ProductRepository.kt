package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Product
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import java.time.LocalDateTime
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductRepository : PanacheRepository<Product> {
    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Product {
        val product =
            find(
                "ref = ?1 and project_id_fk = ?2",
                ref,
                projectId
            ).firstResult<Product>()
        if (product?.deletedDate == null) {
            return product
        } else throw NotFoundException("Product not found")
    }

    //@Throws(BackendException::class)
    fun listAllProducts(id: Long): List<Product> {
        //returerer bare elementer som ikke er soft deleted
        return find("project_id_fk", id).list<Product>().filter { p -> p.deletedDate == null }
    }

    @Throws(BackendException::class)
    fun createProduct(product: Product) {
        persistAndFlush(product)
        if (!product.isPersistent) {
            throw BadRequestException("Bad request! Product not created")
        }
    }

    //@Throws(BackendException::class)
    fun deleteProduct(id: Long){
        val deletedDate = LocalDateTime.now()
        update("deleteddate = ?1 where id = ?2", deletedDate,id)
        //
    }

    @Throws(BackendException::class)
    fun updateProduct(id: Long, product: Product) {
        val updated = update(
            "title = ?1, description = ?2 where id = ?3",
            product.title,
            product.description,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Fail! Product did not update") }
    }
}