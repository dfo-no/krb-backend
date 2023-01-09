package org.kravbank.resource

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kravbank.domain.*
import org.kravbank.domain.frontend.Banks
import org.kravbank.repository.ProjectRepository
import java.io.InputStream
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response


@Path("/api/v1/import")
// TODO: Auth
class ImportResource {
    private val mapper = jacksonObjectMapper()

    @Inject
    lateinit var projectRepository: ProjectRepository

    @POST
    @Path(value = "/bulk")
    @RolesAllowed("user")
    @Transactional
    fun bulkImport(file: InputStream): Response {

        val banks = mapper.readValue<Banks>(file)

        banks.forEach { bank ->

            val newProject = Project()

            val products = bank.products.map { product ->
                Product().apply {
                    title = product.title
                    description = product.description
                    project = newProject
                }
            }

            val publications = bank.publications.map { publication ->
                Publication().apply {
                    comment = publication.comment
                    version = publication.version
                }
            }


            val needs = bank.needs.map { need ->

                val foundRequirements = need.requirements.map { requirement ->
                    Requirement().apply {
                        title = requirement.title
                        description = requirement.description
                        project = newProject


                    }
                }

                println(foundRequirements)
                Need().apply {
                    title = need.title
                    description = need.description
                    project = newProject
                    requirements = foundRequirements.toMutableList()
                }
            }

//            val requirements = needs.map { need -> need.requirements }

            val codelist = bank.codelist.map { codelist ->
                Codelist().apply {
                    title = codelist.title
                    description = codelist.description
                    project = newProject
                }
            }

            newProject.apply {

                title = bank.title
                description = bank.description
                ref = bank.id
                this.products = products.toMutableList()
                this.publications = publications.toMutableList()
//                this.requirements = ;
                this.needs = needs.toMutableList()
                this.codelist = codelist.toMutableList()
            }

            projectRepository.persist(newProject)

        }




        return Response.ok("OK").build()
    }

}