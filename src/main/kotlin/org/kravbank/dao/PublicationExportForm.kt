package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import org.kravbank.domain.PublicationExport
import org.kravbank.utils.Mapper

class PublicationExportForm() : Mapper<PublicationExportForm, PublicationExport> {

    @JsonProperty(access = Access.READ_ONLY)
    lateinit var ref: String

    //TODO list of project, answer, specification
    var content = ArrayList<Any>()


    override fun toEntity(domain: PublicationExportForm): PublicationExport = PublicationExport().apply {
        //TODO nothing here now
    }


    override fun fromEntity(entity: PublicationExport): PublicationExportForm = PublicationExportForm().apply {
        ref = entity.ref
    }
}

