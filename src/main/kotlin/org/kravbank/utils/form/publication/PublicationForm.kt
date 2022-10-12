package org.kravbank.utils.form.publication

import org.kravbank.domain.Project
import java.time.LocalDateTime

data class PublicationForm(

    var ref: String = "",
    var comment: String = "",
    var date: LocalDateTime? = LocalDateTime.now(),
    var version: Long = 0,
    var deletedDate: LocalDateTime? = null,
    var project: Project? = null
)
