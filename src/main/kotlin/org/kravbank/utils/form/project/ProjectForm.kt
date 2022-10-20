package org.kravbank.utils.form.project

import java.time.LocalDateTime

data class ProjectForm(
    var ref: String = "",
    var title: String = "",
    var description: String = "",
    //var version: Long? = null,
    //var publishedDate: LocalDateTime? = null

    //var deletedDate: LocalDateTime? = null,
    //var products: MutableList<Product> = mutableListOf(),
    //var needs: MutableList<Need> = mutableListOf()
    //var codelist: MutableList<Codelist> = mutableListOf()
    //var publications: MutableList<Publication> = mutableListOf()
   // var requirements: MutableList<RequirementForm> = mutableListOf()
)