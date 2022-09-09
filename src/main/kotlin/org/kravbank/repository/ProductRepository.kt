package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.ProductKtl
import org.kravbank.domain.ProjectKtl
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductRepository : PanacheRepository<ProductKtl> {
}