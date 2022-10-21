package org.kravbank.utils.product.mapper

import org.kravbank.domain.Product
import org.kravbank.utils.product.dto.ProductForm

class ProductMapper() : org.kravbank.utils.Mapper<ProductForm, Product> {

    override fun fromEntity(entity: Product): ProductForm =
        ProductForm(
            entity.ref,
            entity.title,
            entity.description
        )

    override fun toEntity(domain: ProductForm): Product {
        val p = Product()
        p.title = domain.title
        p.description = domain.description
        p.project = domain.project
        return p
    }
}