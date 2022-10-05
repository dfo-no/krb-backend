package org.kravbank.response.project

import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.form.project.ProjectFormUpdate
import java.net.URI
import javax.ws.rs.core.Response

class ResponseProject {

    fun not_found () : Response  =  Response.status(Response.Status.NOT_FOUND).build()

    fun ok (form: ProjectForm): Response =  Response.ok(form).build()

    fun ok_list (list: List<ProjectForm>): Response =  Response.ok(list).build()

    fun ok_update (formUpdate: ProjectFormUpdate): Response = Response.ok(formUpdate).build()

    fun bad_req (): Response = Response.status(Response.Status.BAD_REQUEST).build()

    fun create_ok (pathRef: String): Response = Response.created(URI.create(pathRef)).build()

    fun delete_ok (): Response = Response.noContent().build()

}