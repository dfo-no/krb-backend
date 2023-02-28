package org.kravbank.domain

import java.util.*

data class Code(

    var ref: String = UUID.randomUUID().toString(),

    var title: String = "",

    var description: String = "",

    )