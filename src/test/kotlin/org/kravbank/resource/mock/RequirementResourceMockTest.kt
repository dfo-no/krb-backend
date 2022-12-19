package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementForm
import org.kravbank.domain.Requirement
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.RequirementRepository
import org.kravbank.resource.RequirementResource
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_BADREQUEST_DELETE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_NOTFOUND
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.need_requirementRef
import org.kravbank.utils.TestSetup.Arrange.newRequirement
import org.kravbank.utils.TestSetup.Arrange.requirement
import org.kravbank.utils.TestSetup.Arrange.requirementForm
import org.kravbank.utils.TestSetup.Arrange.requirements
import org.kravbank.utils.TestSetup.Arrange.updatedRequirementForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class RequirementResourceMockTest {

    @InjectMock
    lateinit var requirementRepository: RequirementRepository

    @Inject
    lateinit var requirementResource: RequirementResource

    private final val arrangeSetup = TestSetup.Arrange

    private val projectId: Long = arrangeSetup.project_needId
    private val projectRef: String = arrangeSetup.project_needRef
    private val requirementRef: String = arrangeSetup.need_projectRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun getRequirement_OK() {
        Mockito
            .`when`(requirementRepository.findByRef(projectId, requirementRef))
            .thenReturn(requirement)

        val response = requirementResource.getRequirement(projectRef, requirementRef)

        val entity: Requirement = RequirementForm().toEntity(response)

        assertNotNull(response)
        assertEquals(requirement.title, entity.title)
        assertEquals(requirement.description, entity.description)
    }

    @Test
    fun getRequirement_KO() {
        Mockito
            .`when`(requirementRepository.findByRef(projectId, requirementRef))
            .thenThrow(NotFoundException(REQUIREMENT_NOTFOUND))

        try {

            requirementResource.getRequirement(
                projectRef,
                requirementRef
            )

        } catch (e: Exception) {

            assertEquals(REQUIREMENT_NOTFOUND, e.message)
        }
    }

    @Test
    fun listRequirements_OK() {
        Mockito
            .`when`(requirementRepository.listAllRequirements(projectId))
            .thenReturn(requirements)

        val response = requirementResource.listRequirements(projectRef)

        assertNotNull(response)

        assertFalse(response.isEmpty())
        assertEquals(requirements[0].title, response[0].title)
        assertEquals(requirements[0].description, response[0].description)
    }

    @Test
    fun createRequirement_OK() {
        Mockito
            .doNothing()
            .`when`(requirementRepository).persist(ArgumentMatchers.any(Requirement::class.java))
        Mockito
            .`when`(requirementRepository.isPersistent(ArgumentMatchers.any(Requirement::class.java)))
            .thenReturn(true)

        val form = requirementForm
        form.needRef = need_requirementRef

        val response = requirementResource.createRequirement(projectRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteRequirement_OK() {
        Mockito
            .`when`(requirementRepository.deleteRequirement(projectId, requirementRef))
            .thenReturn(newRequirement)

        val response = requirementResource.deleteRequirement(projectRef, requirementRef)

        assertNotNull(response)
        assertEquals(newRequirement.ref, response.entity.toString())
    }

    @Test
    fun deleteRequirement_KO() {
        Mockito
            .`when`(requirementRepository.deleteRequirement(projectId, requirementRef))
            .thenThrow(BadRequestException(REQUIREMENT_BADREQUEST_DELETE))
        try {
            requirementResource.deleteRequirement(
                projectRef,
                requirementRef
            )
                .entity as BadRequestException
        } catch (e: Exception) {
            assertEquals(REQUIREMENT_BADREQUEST_DELETE, e.message)
        }
    }


    @Test
    fun updateRequirement_OK() {
        Mockito.`when`(requirementRepository.findByRef(projectId, requirementRef))
            .thenReturn(requirement)

        val form = updatedRequirementForm

        val response = requirementResource.updateRequirement(projectRef, requirementRef, form)

        val entity: Requirement = RequirementForm().toEntity(response)

        assertNotNull(response)
        assertEquals(form.title, entity.title)
        assertEquals(form.description, entity.description)
    }

    @Test
    fun updateRequirement_KO() {
        Mockito
            .`when`(requirementRepository.findByRef(projectId, requirementRef))
            .thenThrow(BadRequestException(REQUIREMENT_NOTFOUND))

        val form = updatedRequirementForm

        try {
            requirementResource.updateRequirement(projectRef, requirementRef, form)
        } catch (e: Exception) {
            assertEquals(REQUIREMENT_NOTFOUND, e.message)
        }
    }
}