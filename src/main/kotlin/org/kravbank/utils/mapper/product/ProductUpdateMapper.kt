package org.kravbank.utils.mapper.product

import org.kravbank.domain.Product
import org.kravbank.utils.form.product.ProductFormUpdate
import org.kravbank.utils.mapper.Mapper

class ProductUpdateMapper :
    org.kravbank.utils.mapper.Mapper<ProductFormUpdate, Product> {

    // FROM ENTTY
    override fun fromEntity(entity: Product): ProductFormUpdate =
        ProductFormUpdate(
            entity.title,
            entity.description
        )

    // TO ENTITY
    override fun toEntity(domain: ProductFormUpdate): Product {
        val p = Product()
        p.title = domain.title
        p.description = domain.description
        return p
    }

}