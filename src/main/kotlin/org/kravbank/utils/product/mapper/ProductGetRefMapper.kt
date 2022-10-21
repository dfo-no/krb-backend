package org.kravbank.utils.product.mapper

import org.kravbank.domain.Product
import org.kravbank.utils.Mapper
import org.kravbank.utils.product.dto.ProductGetRefDTO

class ProductGetRefMapper() : Mapper<ProductGetRefDTO, Product> {

    override fun fromEntity(entity: Product): ProductGetRefDTO =
        ProductGetRefDTO(
            entity.ref,
        )

    override fun toEntity(domain: ProductGetRefDTO): Product {
        return Product()
    }
}