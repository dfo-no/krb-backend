package org.kravbank.utils.form.product

import org.kravbank.domain.Product
import java.time.LocalDateTime


data class ProductForm(

    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var deletedDate: LocalDateTime? = null
)
