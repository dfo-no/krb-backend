package org.kravbank.utils.product.mapper

import org.kravbank.domain.Product
import org.kravbank.utils.Mapper
import org.kravbank.utils.product.dto.ProductGetDTO

class ProductGetMapper() : Mapper<ProductGetDTO, Product> {

    override fun fromEntity(entity: Product): ProductGetDTO =
        ProductGetDTO(
            entity.ref,
            entity.title,
            entity.description
        )

    override fun toEntity(domain: ProductGetDTO): Product {
        val p = Product()
        p.title = domain.title
        p.description = domain.description
        return p
    }
}