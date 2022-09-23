package org.kravbank.service

import org.kravbank.domain.Codelist
import org.kravbank.domain.Product
import org.kravbank.domain.Publication
import org.kravbank.repository.ProductRepository
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductService(val productRepository: ProductRepository) {
    fun listProducts(): List<Product> = productRepository.listAll()

    fun getProduct(id: Long): Optional<Product>? = productRepository.findByIdOptional(id)
    fun getProductByRefCustomRepo (ref: String): Product = productRepository.findByRef(ref)

    fun createProduct(product: Product) = productRepository.persist(product) //persist and flush vs persist

    fun exists(id: Long): Boolean = productRepository.count("id", id) == 1L

    fun refExists(ref: String): Boolean = productRepository.count("ref", ref) == 1L


    fun deleteProduct(id: Long) = productRepository.deleteById(id)

    fun updateProduct(id: Long, product: Product) {
        productRepository.update("title = ?1, description = ?2 where id= ?3",
            product.title,
            product.description,
            id)
    }
}