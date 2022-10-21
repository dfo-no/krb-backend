package org.kravbank.utils.product.mapper

import org.kravbank.domain.Product
import org.kravbank.utils.product.dto.ProductFormCreate

class ProductCreateMapper() : org.kravbank.utils.Mapper<ProductFormCreate, Product> {
    override fun fromEntity(entity: Product): ProductFormCreate =
        ProductFormCreate(
            entity.ref,
            entity.title,
            entity.description
        )

    override fun toEntity(domain: ProductFormCreate): Product {
        val p = Product()
        p.title = domain.title
        p.description = domain.description
        p.project = domain.project
        return p
    }
}