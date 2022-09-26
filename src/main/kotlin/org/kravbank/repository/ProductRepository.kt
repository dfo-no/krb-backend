package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Codelist
import org.kravbank.domain.Product
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductRepository : PanacheRepository<Product> {
    fun findByRef(ref: String) = find("ref", ref).firstResult<Product>()

}