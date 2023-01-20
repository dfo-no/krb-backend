package org.kravbank.repository

import org.kravbank.domain.Product
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.PRODUCT_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.PRODUCT_NOTFOUND
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductRepository : BackendRepository<Product>() {
    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Product {
        val entity = find(
            "ref = ?1 and project_id_fk = ?2",
            ref,
            projectId
        ).firstResult<Product>()

        return Optional.ofNullable(entity).orElseThrow { NotFoundException(PRODUCT_NOTFOUND) }

    }

    fun listAllProducts(id: Long): List<Product> {
        return find("project_id_fk", id)
            .stream<Product>()
            .toList()
    }

    @Throws(BackendException::class)
    fun updateProduct(id: Long, product: Product) {
        val updated = update(
            "title = ?1, description = ?2 where id = ?3",
            product.title,
            product.description,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException(PRODUCT_BADREQUEST_UPDATE) }
    }
}