package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.TestSetup.Arrange.requirementVariant
import org.kravbank.TestSetup.Arrange.requirementVariantForm
import org.kravbank.TestSetup.Arrange.requirementVariants
import org.kravbank.TestSetup.Arrange.updatedRequirementVariantForm
import org.kravbank.dao.RequirementVariantForm
import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.resource.RequirementVariantResource
import org.kravbank.utils.ErrorMessage.RepoError.REQUIREMENT_VARIANT_NOTFOUND
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class RequirementVariantResourceMockTest {

    @InjectMock
    lateinit var requirementVariantRepository: RequirementVariantRepository

    @Inject
    lateinit var requirementVariantResource: RequirementVariantResource

    private final val arrangeSetup = TestSetup.Arrange


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
    }

    private final val requirementId: Long = arrangeSetup.requirement_requirementVariantId
    private final val requirementRef: String = arrangeSetup.requirement_project_requirementVariantRef
    private final val reqVariantRef: String = arrangeSetup.requirementVariant_requirementRef
    private final val projectRef: String = arrangeSetup.project_requirementVariantRef


    @Test
    fun getRequirementVariant_OK() {
        Mockito
            .`when`(requirementVariantRepository.findByRef(requirementId, reqVariantRef))
            .thenReturn(requirementVariant)

        val response: Response = requirementVariantResource.getRequirementVariant(
            projectRef,
            requirementRef,
            reqVariantRef
        )

        val entity: RequirementVariant = RequirementVariantForm()
            .toEntity(response.entity as RequirementVariantForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals(requirementVariant.instruction, entity.instruction)
        assertEquals(requirementVariant.description, entity.description)
        assertEquals(requirementVariant.requirementText, entity.requirementText)
        assertEquals(requirementVariant.useProduct, entity.useProduct)
        assertEquals(requirementVariant.useQualification, entity.useQualification)
        assertEquals(requirementVariant.useSpesification, entity.useSpesification)
    }

    @Test
    fun getRequirementVariant_KO() {

        Mockito
            .`when`(requirementVariantRepository.findByRef(requirementId, reqVariantRef))
            .thenThrow(NotFoundException(REQUIREMENT_VARIANT_NOTFOUND))
        try {

            requirementVariantResource.getRequirementVariant(
                projectRef,
                requirementRef,
                reqVariantRef
            ).entity as NotFoundException

        } catch (e: Exception) {
            assertEquals(REQUIREMENT_VARIANT_NOTFOUND, e.message)
        }
    }

    @Test
    fun listRequirementVariant_OK() {
        Mockito
            .`when`(requirementVariantRepository.listAllRequirementVariants(requirementId))
            .thenReturn(requirementVariants)

        val response: Response = requirementVariantResource.listRequirementVariants(projectRef, requirementRef)

        val entity: List<RequirementVariantForm> = response.entity as List<RequirementVariantForm>

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals(requirementVariants[0].instruction, entity[0].instruction)
        assertEquals(requirementVariants[0].description, entity[0].description)
        assertEquals(requirementVariants[0].requirementText, entity[0].requirementText)
        assertEquals(requirementVariants[0].useProduct, entity[0].useProduct)
        assertEquals(requirementVariants[0].useQualification, entity[0].useQualification)
        assertEquals(requirementVariants[0].useSpesification, entity[0].useSpesification)
    }

    @Test
    fun createRequirementVariant_OK() {
        Mockito
            .doNothing()
            .`when`(requirementVariantRepository)
            .persist(ArgumentMatchers.any(RequirementVariant::class.java))

        Mockito
            .`when`(requirementVariantRepository.isPersistent(ArgumentMatchers.any(RequirementVariant::class.java)))
            .thenReturn(true)

        val form = requirementVariantForm

        val response: Response = requirementVariantResource.createRequirementVariant(projectRef, requirementRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteRequirementVariant_OK() {
        //mock
        Mockito
            .`when`(requirementVariantRepository.deleteRequirementVariant(requirementId, reqVariantRef))
            .thenReturn(requirementVariant)

        val response: Response = requirementVariantResource.deleteRequirementVariant(
            projectRef,
            requirementRef,
            reqVariantRef
        )

        assertNotNull(response)
        assertEquals(requirementVariant.ref, response.entity.toString())

    }

    @Test
    fun updateRequirementVariant_OK() {

        Mockito
            .`when`(
                requirementVariantRepository.findByRef(
                    requirementId,
                    reqVariantRef
                )
            )
            .thenReturn(requirementVariant)

        val form = updatedRequirementVariantForm

        val response: Response = requirementVariantResource.updateRequirementVariant(
            projectRef,
            requirementRef,
            reqVariantRef,
            form
        )

        val entity: RequirementVariant = RequirementVariantForm().toEntity(response.entity as RequirementVariantForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(form.instruction, entity.instruction)
        assertEquals(form.description, entity.description)
    }


}