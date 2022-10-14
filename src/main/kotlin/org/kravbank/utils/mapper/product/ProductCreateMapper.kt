package org.kravbank.utils.mapper.product

import org.kravbank.domain.Product
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.form.product.ProductForm
import org.kravbank.utils.form.product.ProductFormCreate
import org.kravbank.utils.mapper.Mapper

class ProductCreateMapper(val ref: String) : Mapper<ProductFormCreate, Product> {
    // FROM ENTITY
    override fun fromEntity(entity: Product): ProductFormCreate =
        ProductFormCreate(
            entity.ref,
            entity.title,
            entity.description,
            //entity.project,
        //entity.requirementvariant
        )


    //TO ENTITY
    override fun toEntity(domain: ProductFormCreate): Product {
        val p = Product()
        p.title = domain.title
        p.description = domain.description
        p.project = domain.project
        //sjekker om req variant i newProduct fra service eksisterer
        //val foundReqVariant = requirementVariantRepository.findByRef(id, domain.requirementVariant)
        println("REQ VARIANT STRING REF --> $ref")
        val requirementVariantRepository =  RequirementVariantRepository()
        val foundReqVariant = requirementVariantRepository.findByRefProduct(ref)
        println("FOUND $foundReqVariant.id")
        print("FOUND 2 $foundReqVariant.description")
        p.requirementvariant = foundReqVariant
        return p
    }
}