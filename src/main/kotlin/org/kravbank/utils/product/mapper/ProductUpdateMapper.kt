package org.kravbank.utils.product.mapper

import org.kravbank.domain.Product
import org.kravbank.utils.product.dto.ProductFormUpdate

class ProductUpdateMapper : org.kravbank.utils.Mapper<ProductFormUpdate, Product> {

    override fun fromEntity(entity: Product): ProductFormUpdate =
        ProductFormUpdate(
            entity.title,
            entity.description,
        )

    override fun toEntity(domain: ProductFormUpdate): Product {
        val p = Product()
        p.title = domain.title
        p.description = domain.description
        return p
    }

}