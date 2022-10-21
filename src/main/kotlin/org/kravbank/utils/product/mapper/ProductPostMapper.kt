package org.kravbank.utils.product.mapper

import org.kravbank.domain.Product
import org.kravbank.utils.Mapper
import org.kravbank.utils.product.dto.ProductPostDTO

class ProductPostMapper() : Mapper<ProductPostDTO, Product> {

    override fun fromEntity(entity: Product): ProductPostDTO =
        ProductPostDTO(
            entity.ref,
            entity.title,
            entity.description
        )

    override fun toEntity(domain: ProductPostDTO): Product {
        val p = Product()
        p.title = domain.title
        p.description = domain.description
        p.project = domain.project
        return p
    }
}