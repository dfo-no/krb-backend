package org.kravbank.utils.publication.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import org.kravbank.domain.Project
import java.time.LocalDateTime

data class PublicationForm(

    var ref: String = "",
    var comment: String = "",
    var date: LocalDateTime? = null,
    var version: Long = 0,
    @JsonIgnore
    var project: Project? = null

)
