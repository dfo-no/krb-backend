package org.kravbank.service

import io.quarkus.hibernate.orm.panache.PanacheEntity_.id
import org.kravbank.domain.ProductKtl
import org.kravbank.java.model.Product
import org.kravbank.repository.ProductRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductService(val productRepository: ProductRepository) {
    fun listProducts(): MutableList<ProductKtl> = productRepository.listAll()
    fun getProduct(id: Long): ProductKtl = productRepository.findById(id)
    fun createProduct(productKtl: ProductKtl) = productRepository.persistAndFlush(productKtl) //persist and flush vs persist
    fun exists(id: Long): Boolean = productRepository.count("id", id) == 1L
    fun deleteProduct(id: Long) = productRepository.deleteById(id)
}