package org.kravbank.form.publication

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class PublicationFormUpdate(

  //var ref : String = "",
    @NotNull
    @NotBlank
    @NotEmpty
    var comment: String = "",

    var deletedDate: String = ""


)
