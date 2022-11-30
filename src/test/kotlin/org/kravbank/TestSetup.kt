package org.kravbank

import org.kravbank.dao.NeedForm
import org.kravbank.dao.ProjectForm
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.*
import java.time.LocalDateTime

class TestSetup {
    companion object Arrange {
        //project
        var project = Project()
        var projects: MutableList<Project> = mutableListOf()
        lateinit var projectForm: ProjectForm
        lateinit var updatedProjectForm: ProjectForm

        //code
        var code = Code()
        var codes: MutableList<Code> = mutableListOf()

        //publication
        var publication = Publication()
        var publications: MutableList<Publication> = mutableListOf()
        lateinit var newPublication: Publication
        lateinit var publicationForm: PublicationForm
        lateinit var updatedPublicationForm: PublicationForm
        val publication_projectRef: String = "zzz4db69-edb2-431f-855a-4368e2bcddd1"
        val project_publicationId: Long = 3L
        val project_publicationRef: String = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

        //product
        var product = Product()
        var products: MutableList<Product> = mutableListOf()

        //requirement
        var requirement = Requirement()
        var requirements: MutableList<Requirement> = mutableListOf()

        //need
        var need = Need()
        var needs: MutableList<Need> = mutableListOf()
        lateinit var needForm: NeedForm
        lateinit var newNeed: Need
        lateinit var updatedNeedForm: NeedForm
        val project_needRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val project_needId = 2L
        val need_projectRef: String = "need1b69-edb2-431f-855a-4368e2bcddd1"

        //codelist
        var codelist = Codelist()
        var codelists: MutableList<Codelist> = mutableListOf()

        val dateTime: LocalDateTime = LocalDateTime.of(2021, 2, 21, 10, 10, 10)

        fun start() {
            //project
            project.title = "første prosjekt"
            project.description = "første prosjektbeskrivelse"
            project.ref = "ccc4db69-edb2-431f-855a-4368e2bcddd1"
            project.id = 120
            project.codelist = codelists
            project.requirements = requirements
            project.publications = publications
            project.needs = needs
            project.products = products

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

            updatedProjectForm = ProjectForm()
            updatedProjectForm.title = "Oppdatert tittel"
            updatedProjectForm.description = "Oppdatert beskrivelse"

            projects.add(project)
            projects.add(newProject)

            //code
            code = Code()
            code.title = "Tittel kode"
            code.description = "beskrivelse kode"
            code.codelist = codelist

            //codelist
            codelist = Codelist()
            codelist.title = "Første codelist"
            codelist.description = "første codelist beskrivelse"
            codelist.ref = "hello243567"
            codelist.project = project
            codelist.codes = codes
            codelist.id = (1L)

            //product
            product = Product()
            product.project = project
            product.id = 121L
            product.ref = "34352"
            product.title = "prod"
            product.description = "desc"

            //need
            need = Need()
            need.ref = "ewdsfsada567"
            need.id = 122L
            need.title = "Ny need tittel"
            need.description = "desv"
            need.requirements = requirements
            need.project = project

            newNeed = Need()
            newNeed.ref = "3565-245tfdsf43-r4wet3435"
            newNeed.id = 123L
            newNeed.title = "Ny need tittel form"
            newNeed.description = "need beskrivelse"
            newNeed.requirements = requirements
            newNeed.project = project

            needForm = NeedForm().fromEntity(newNeed)

            updatedNeedForm = NeedForm()
            updatedNeedForm.title = "Endre tittel som need"
            updatedNeedForm.description = "Endre beskrivelse som need"

            needs.add(need)
            needs.add(newNeed)

            //publication
            publication = Publication()
            publication.id = 200
            publication.ref = "zzz4db69-edb2-431f-855a-4368e2bcddd1"
            publication.project = project
            publication.date = dateTime
            publication.comment = "En til kommentar"
            publication.version = 10

            newPublication = Publication()
            newPublication.id = 201
            newPublication.ref = "asdsa-fdsf-34-fsd-dsgf-35463fd"
            newPublication.project = project
            //newPublication.date = dateTime
            newPublication.comment = "En ny kommentar"

            publicationForm = PublicationForm().fromEntity(newPublication)

            updatedPublicationForm = PublicationForm()
            updatedPublicationForm.comment = "Oppdatert tittel"

            publications.add(publication)
            publications.add(newPublication)

            //requirement
            requirement = Requirement()

        }
    }
}