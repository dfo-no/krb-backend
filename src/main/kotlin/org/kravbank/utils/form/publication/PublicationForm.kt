package org.kravbank.utils.form.publication

import java.time.LocalDateTime
import javax.persistence.Column
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


data class PublicationForm(

    var ref: String = "",
    var comment: String = "",
    var date: LocalDateTime? = LocalDateTime.now(),
    var version: Long = 0,
    var deletedDate: LocalDateTime? = null
)
