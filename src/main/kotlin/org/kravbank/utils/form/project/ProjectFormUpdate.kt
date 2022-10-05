package org.kravbank.utils.form.project

import org.kravbank.domain.Project
import java.time.LocalDateTime

data class ProjectFormUpdate(
    var title: String = "",
    var description: String = "",
    var version: Long = 0, // Long = 0,
    var publishedDate: LocalDateTime = LocalDateTime.now(),
    var deletedDate: LocalDateTime? =  null
    ) {
  //  operator fun inc() = ProjectFormUpdate("","",version + 1)
}

