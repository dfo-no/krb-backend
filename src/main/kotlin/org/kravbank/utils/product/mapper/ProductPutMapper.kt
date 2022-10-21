package org.kravbank.utils.product.mapper

import org.kravbank.domain.Product
import org.kravbank.utils.Mapper
import org.kravbank.utils.product.dto.ProductPutDTO

class ProductPutMapper : Mapper<ProductPutDTO, Product> {

    override fun fromEntity(entity: Product): ProductPutDTO =
        ProductPutDTO(
            entity.title,
            entity.description,
        )

    override fun toEntity(domain: ProductPutDTO): Product {
        val p = Product()
        p.title = domain.title
        p.description = domain.description
        return p
    }

}