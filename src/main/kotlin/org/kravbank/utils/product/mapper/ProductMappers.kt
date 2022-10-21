package org.kravbank.utils.product.mapper

import org.kravbank.domain.Product
import org.kravbank.utils.product.ProductMapper

class ProductMappers() : ProductMapper<Any, Product> {

    override fun getOneFromEntity(entity: Product): Any {
        TODO("Not yet implemented")
    }

    override fun getListFromEntity(entity: Product): Any {
        TODO("Not yet implemented")
    }

    override fun getRefFromEntity(entity: Product): Any {
        TODO("Not yet implemented")
    }

    override fun postToEntity(domain: Any): Product {
        TODO("Not yet implemented")
    }

    override fun putToEntity(domain: Any): Product {
        TODO("Not yet implemented")
    }


}