package org.kravbank.utils

import org.kravbank.dao.*
import org.kravbank.dao.code.CodeCreateRequest
import org.kravbank.dao.code.CodeUpdateRequest
import org.kravbank.domain.*
import java.time.LocalDateTime

class TestSetup {

    companion object Arrange {

        //project
        var project = Project()
        var newProject = Project()
        var projects: MutableList<Project> = mutableListOf()
        lateinit var projectForm: ProjectForm
        lateinit var updatedProjectForm: ProjectForm
        val projectRef = "ccc4db69-edb2-431f-855a-4368e2bcddd1"


        //code
        var code = Code()
        var codes: MutableList<Code> = mutableListOf()
        lateinit var newCode: Code
        lateinit var codeForm: CodeForm
        lateinit var updatedCodeForm: CodeForm
        val project_codeRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelist_codeId = 4L
        val codelist_codeRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"
        val code_codelistRef = "script1b69-edb2-431f-855a-4368e2bcddd1"

        val codeUpdateRequest = CodeUpdateRequest(
            title = "Oppdatert kode tittel fra Code update request",
            description = "Oppdatert kode beskrivelse fra Code update request"
        )
        val codeCreateRequest = CodeCreateRequest(
            title = "Ny kode tittel fra Code create request",
            description = "Ny kode beskrivelse fra Code create request"
        )


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
        lateinit var newProduct: Product
        lateinit var productForm: ProductForm
        lateinit var updatedProductForm: ProductForm
        val project_productRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val project_productId = 3L
        val product_projectRef = "edb4db69-edb2-431f-855a-4368e2bcddd1"
        val reqVariant_productRef = "rvrv1b69-edb2-431f-855a-4368e2bcddd1"


        //requirement
        var requirement = Requirement()
        var requirements: MutableList<Requirement> = mutableListOf()
        lateinit var newRequirement: Requirement
        lateinit var requirementForm: RequirementForm
        lateinit var updatedRequirementForm: RequirementForm
        val project_requirementRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val project_requirementId = 2L
        val requirement_projectRef = "req1b69-edb2-431f-855a-4368e2bcddd1"
        val need_requirementRef = "need2b69-edb2-431f-855a-4368e2bcddd1"


        //requirement variant
        var requirementVariant = RequirementVariant()
        var requirementVariants: MutableList<RequirementVariant> = mutableListOf()
        lateinit var newRequirementVariant: RequirementVariant
        lateinit var requirementVariantForm: RequirementVariantForm
        lateinit var updatedRequirementVariantForm: RequirementVariantForm
        val project_requirementVariantRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val requirement_requirementVariantId = 12L
        val requirement_requirementVariantRef = "req1b69-edb2-431f-855a-4368e2bcddd1"
        val requirementVariant_requirementRef = "rvrv3b69-edb2-431f-855a-4368e2bcddd1"


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
        lateinit var newCodelist: Codelist
        lateinit var newCodelist_2: Codelist
        lateinit var codelistForm: CodelistForm
        lateinit var updatedCodelistForm: CodelistForm
        val project_codelistRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val project_codelistId = 3L
        val codelist_projectRef: String = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        private val dateTime: LocalDateTime = LocalDateTime.of(2021, 2, 21, 10, 10, 10)

        fun start() {

            //project
            project.title = "første prosjekt"
            project.description = "første prosjektbeskrivelse"
            project.ref = projectRef
            project.id = 120
            project.codelist = codelists
            project.requirements = requirements
            project.publications = publications
            project.needs = needs
            project.products = products

            newProject = Project()
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
            code.id = 506
            code.ref = "dsafdsjkl823jhkfkjdhkjl"

            newCode = Code()
            newCode.title = "Ny kodelist tittel"
            newCode.description = "Ny beskrivelse"
            newCode.codelist = codelist
            newCode.id = 507
            code.ref = "6rtrsad5678-sadsa-34-dsfsd-sxdsf"

            codeForm = CodeForm().fromEntity(newCode)

            updatedCodeForm = CodeForm()
            updatedCodeForm.title = "Endre tittel som need"
            updatedCodeForm.description = "Endre beskrivelse som need"





            codes.add(code)
            codes.add(newCode)


            //codelist
            codelist = Codelist()
            codelist.title = "Første codelist"
            codelist.description = "første codelist beskrivelse"
            codelist.ref = "hello243567"
            codelist.project = project
            codelist.codes = codes
            codelist.id = (1L)

            newCodelist = Codelist()
            newCodelist.project = project
            newCodelist.title = "Ny kodelist tittel"
            newCodelist.description = "Ny beskrivelse"
            newCodelist.codes = codes
            newCodelist.id = 505

            codelistForm = CodelistForm().fromEntity(newCodelist)

            newCodelist_2 = Codelist()
            newCodelist_2.ref = "dsfdsgs<'fåowi39543tdsf"
            newCodelist_2.project = project
            newCodelist_2.title = "Ny kodelist tittel"
            newCodelist_2.description = "Ny beskrivelse"
            newCodelist_2.codes = codes
            newCodelist_2.id = 506

            updatedCodelistForm = CodelistForm()
            updatedCodelistForm.title = "Endre tittel som need"
            updatedCodelistForm.description = "Endre beskrivelse som need"

            codelists.add(codelist)
            codelists.add(newCodelist)


            //product
            product = Product()
            product.project = project
            product.id = 121L
            product.ref = "34352"
            product.title = "prod"
            product.description = "desc"

            newProduct = Product()
            newProduct.project = project
            newProduct.id = 121L
            newProduct.title = "Nytt produkt"
            newProduct.description = "Ny beskrivelse av produkt"

            productForm = ProductForm().fromEntity(newProduct)
            productForm.requirementVariantRef = reqVariant_productRef

            updatedProductForm = ProductForm()
            updatedProductForm.title = "Endre tittel som need"
            updatedProductForm.description = "Endre beskrivelse som need"

            products.add(product)
            products.add(newProduct)


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
            newPublication.project = newProject
            newPublication.comment = "En ny kommentar"

            publicationForm = PublicationForm().fromEntity(newPublication)

            updatedPublicationForm = PublicationForm()
            updatedPublicationForm.comment = "Oppdatert tittel"
            updatedPublicationForm.version = 11

            publications.add(publication)
            publications.add(newPublication)


            //requirement
            requirement = Requirement()
            requirement.id = 1000L
            requirement.title = "Tittel requirement"
            requirement.description = "Beskrivelse requirement"
            requirement.project = project
            requirement.need = need
            requirement.requirementvariants = requirementVariants

            newRequirement = Requirement()
            newRequirement.title = "Ny requirement tittel"
            newRequirement.description = "Ny requirement beskrivelse"
            newRequirement.project = project
            newRequirement.need = need
            newRequirement.requirementvariants = requirementVariants
            newRequirement.id = 1001L

            requirementForm = RequirementForm().fromEntity(newRequirement)
            requirementForm.needRef = need_requirementRef

            updatedRequirementForm = RequirementForm()
            updatedRequirementForm.title = "Oppdatert tittel for requirement"
            updatedRequirementForm.description = "Oppdatert requirement beskrivelse"

            requirements.add(requirement)
            requirements.add(newRequirement)


            //requirement variant
            requirementVariant = RequirementVariant()
            requirementVariant.id = 89
            requirementVariant.description = "En beskrivelse"
            requirementVariant.instruction = "Instruksjon"
            requirementVariant.useProduct = true
            requirementVariant.useSpecification = true
            requirementVariant.useQualification = true
            requirementVariant.requirementText = "Variant tekst"
            requirementVariant.product = products
            requirementVariant.requirement = requirement

            newRequirementVariant = RequirementVariant()
            requirementVariant.id = 88
            newRequirementVariant.description = "Ny beskrivelse"
            newRequirementVariant.instruction = "Ny Instruksjon"
            newRequirementVariant.useProduct = false
            newRequirementVariant.useSpecification = false
            newRequirementVariant.useQualification = false
            newRequirementVariant.product = products
            newRequirementVariant.requirementText = "Variant tekst"
            newRequirementVariant.requirement = requirement

            requirementVariantForm = RequirementVariantForm().fromEntity(newRequirementVariant)

            updatedRequirementVariantForm = RequirementVariantForm()
            updatedRequirementVariantForm.ref = requirementVariant_requirementRef
            updatedRequirementVariantForm.instruction = "Oppdatert instruksjon for requirement"
            updatedRequirementVariantForm.description = "Oppdatert requirement variant beskrivelse"
            updatedRequirementVariantForm.useProduct = false
            updatedRequirementVariantForm.useSpecification = false
            updatedRequirementVariantForm.useQualification = false

            requirementVariants.add(requirementVariant)
            requirementVariants.add(newRequirementVariant)

        }
    }
}