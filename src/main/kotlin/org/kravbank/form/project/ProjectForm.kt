package org.kravbank.form.project


data class ProjectForm(
    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var version: String = "",
    var publishedDate: String = "",
    var deletedDate: String = ""
)
