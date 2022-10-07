package org.kravbank.utils.form.project

import java.time.LocalDateTime

data class ProjectForm(
    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var version: Long? = null, //Long = 1,
    var publishedDate: LocalDateTime = LocalDateTime.now(),
    var deletedDate: LocalDateTime? = null
)