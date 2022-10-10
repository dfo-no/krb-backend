package org.kravbank.utils.mapper.product

import org.kravbank.domain.Product
import org.kravbank.utils.form.product.ProductForm
import org.kravbank.utils.mapper.Mapper

class ProductMapper :
    org.kravbank.utils.mapper.Mapper<ProductForm, Product> {


    // FROM ENTITY
    override fun fromEntity(entity: Product): ProductForm =
        ProductForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.deletedDate,
            entity.project
        )


    //TO ENTITY
    override fun toEntity(domain: ProductForm): Product {
      val p = Product()
        p.title = domain.title
        p.description = domain.description
        p.deletedDate = domain.deletedDate
        p.project = domain.project
        println("p.project: ${p.project}")
        return p
    }
}