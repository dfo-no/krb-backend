package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Product
import org.kravbank.utils.Mapper

class ProductForm() : Mapper<ProductForm, Product> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var ref: String = ""

    lateinit var title: String

    lateinit var description: String

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // denne gir feil i integrasjonstesten pga. den ikke deserialiserer
    var requirementVariantRef: String = ""

    override fun toEntity(domain: ProductForm): Product = Product().apply {
        title = domain.title
        description = domain.description
        requirementVariantRef = domain.requirementVariantRef
    }

    override fun fromEntity(entity: Product): ProductForm = ProductForm().apply {
        ref = entity.ref
        title = entity.title
        description = entity.description
    }
}
