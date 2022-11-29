package org.kravbank

import org.kravbank.dao.ProjectForm
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.*
import java.time.LocalDateTime

class TestSetup {

    //arrange
    companion object SetDomains {

        //entity
        var project = Project()
        var code = Code()
        var publication = Publication()
        var product = Product()
        var requirement = Requirement()
        var need = Need()
        var codelist = Codelist()

        //list
        var projects: MutableList<Project> = mutableListOf()
        var codes: MutableList<Code> = mutableListOf()
        var codelists: MutableList<Codelist> = mutableListOf()
        var requirements: MutableList<Requirement> = mutableListOf()
        var needs: MutableList<Need> = mutableListOf()
        var publications: MutableList<Publication> = mutableListOf()
        var products: MutableList<Product> = mutableListOf()

        lateinit var projectForm: ProjectForm

        lateinit var publicationForm: PublicationForm

        lateinit var updatedProjectForm: ProjectForm

        lateinit var updatedPublicationForm: PublicationForm

        val dateTime: LocalDateTime = LocalDateTime.of(2021, 2, 21, 10, 10, 10)

        val publication_projectRef: String = "zzz4db69-edb2-431f-855a-4368e2bcddd1"
        val project_publicationId: Long = 3L
        val project_publicationRef: String = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

        val project_needRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val project_needId = 2L
        val need_projectRef: String = "need1b69-edb2-431f-855a-4368e2bcddd1"

        fun arrange() {
            //DOMAIN
            project.title = "første prosjekt"
            project.description = "første prosjektbeskrivelse"
            project.ref = "ccc4db69-edb2-431f-855a-4368e2bcddd1"
            project.id = 120
            project.codelist = codelists
            project.requirements = requirements
            project.publications = publications
            project.needs = needs
            project.products = products

            code = Code()
            code.title = "Tittel kode"
            code.description = "beskrivelse kode"
            code.codelist = codelist

            codelist = Codelist()
            codelist.title = "Første codelist"
            codelist.description = "første codelist beskrivelse"
            codelist.ref = "hello243567"
            codelist.project = project
            codelist.codes = codes
            codelist.id = (1L)

            product = Product()
            product.project = project
            product.id = 121L
            product.ref = "34352"
            product.title = "prod"
            product.description = "desc"

            need = Need()
            need.ref = "ewdsfsada567"
            need.id = 122L
            need.title = "tittel"
            need.description = "desv"
            need.requirements = requirements
            need.project = project


            publication = Publication()
            publication.id = 200
            publication.ref = "zzz4db69-edb2-431f-855a-4368e2bcddd1"
            publication.project = project
            publication.date = dateTime
            publication.comment = "En publicationskommentar"

            requirement = Requirement()
            projects.add(project)

            val newProject = Project()
            newProject.title = "andre prosjekt"
            newProject.description = "andre prosjektbeskrivelse"
            newProject.ref = "dfs245678ivcbcccdsgfgdsf5db69-edb2-431f-855a-4368e2bcddd1"
            newProject.id = 121
            newProject.codelist = codelists
            newProject.requirements = requirements
            newProject.publications = publications
            newProject.needs = needs
            newProject.products = products

            projectForm = ProjectForm().fromEntity(newProject)

            val newPublication = Publication()
            newPublication.id = 201
            newPublication.ref = "asdsa-fdsf-34-fsd-dsgf-35463fd"
            newPublication.project = project
            newPublication.date = dateTime
            newPublication.comment = "En ny publicationskommentar"

            publicationForm = PublicationForm().fromEntity(newPublication)

            //FORM Update Project
            updatedProjectForm = ProjectForm()
            updatedProjectForm.title = "Oppdatert tittel"
            updatedProjectForm.description = "Oppdatert beskrivelse"


            //FORM Update Project
            updatedPublicationForm = PublicationForm()
            updatedPublicationForm.ref = "zzz4db69-edb2-431f-855a-4368e2bcddd1"
            updatedPublicationForm.comment = "Oppdatert tittel"
            updatedPublicationForm.date = LocalDateTime.of(2018, 2, 21, 10, 10, 10)
        }
    }
}