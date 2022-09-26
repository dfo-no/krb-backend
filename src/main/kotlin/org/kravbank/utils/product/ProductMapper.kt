package org.kravbank.utils.product

import org.kravbank.domain.Product
import org.kravbank.form.product.ProductForm
import org.kravbank.utils.Mapper

class ProductMapper : Mapper<ProductForm, Product> {


    // FROM ENTITY
    override fun fromEntity(entity: Product): ProductForm =
        ProductForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.deletedDate,
        )


    //TO ENTITY
    override fun toEntity(domain: ProductForm): Product {
      val p = Product()
        p.title = domain.title
        p.description = domain.description
        p.deletedDate = domain.deletedDate
        return p
    }
}