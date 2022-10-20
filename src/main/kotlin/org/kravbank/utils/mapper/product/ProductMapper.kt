package org.kravbank.utils.mapper.product

import org.kravbank.domain.Product
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.form.product.ProductForm
import org.kravbank.utils.mapper.Mapper

class ProductMapper() : Mapper<ProductForm, Product> {
    // FROM ENTITY
    override fun fromEntity(entity: Product): ProductForm =
        ProductForm(
            entity.ref,
            entity.title,
            entity.description,
            //entity.deletedDate,
            //entity.project,
        //entity.requirementvariant
        )


    //TO ENTITY
    override fun toEntity(domain: ProductForm): Product {
        val p = Product()
        p.title = domain.title
        p.description = domain.description
        p.project = domain.project

       //sjekker om req variant i newProduct fra service eksisterer
        //val foundReqVariant = requirementVariantRepository.findByRef(id, domain.requirementVariant)
      /*  println("REQ VARIANT STRING REF --> $ref")

        val requirementVariantRepository = RequirementVariantRepository()
        val foundReqVariant = requirementVariantRepository.findByRefProduct(ref)
        println(foundReqVariant.id)
        print(foundReqVariant.description)

        p.requirementvariant = foundReqVariant

       */
        return p
    }
}