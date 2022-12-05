package org.kravbank.utils

class ErrorMessage {


    //TODO("Implementer i repos")

    companion object RepoError {

        const val CODELIST_NOTFOUND = "Codelist was not found!"
        const val CODELIST_BADREQUEST_DELETE = "Bad request! Codelist was not deleted"
        const val CODE_BADREQUEST_DELETE = "Bad request! Code was not deleted"
        const val CODE_NOTFOUND = "Code was not found!"

        //const val CODE_BADREQUEST_CREATE = "Bad request! Code was not created"
        const val PRODUCT_NOTFOUND = "Product not found"
        const val PROJECT_NOTFOUND = "Project not found"

        const val REQUIREMENT_NOTFOUND = "Requirement not found!"

        const val REQUIREMENT_BADREQUEST_DELETE = "Bad request! Requirement was not deleted"
        const val RREQUIREMENT_NOTFOUND = "Requirement not found"

        const val REQUIREMENT_VARIANT_NOTFOUND = "RequirementVariant was not found!"
    }


}