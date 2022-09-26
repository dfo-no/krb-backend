package org.kravbank.form.publication

import javax.persistence.Column
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


data class PublicationForm(

    var ref: String = "",
    var comment: String = "",
    //var version: String = "",
    var deletedDate: String = ""
)
