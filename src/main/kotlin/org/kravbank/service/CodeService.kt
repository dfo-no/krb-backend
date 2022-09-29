package org.kravbank.service

import org.kravbank.domain.Code
import org.kravbank.repository.CodeRepository
import org.kravbank.utils.form.code.CodeForm
import org.kravbank.utils.form.code.CodeFormUpdate
import org.kravbank.utils.mapper.code.CodeMapper
import org.kravbank.utils.mapper.code.CodeUpdateMapper
import java.lang.IllegalArgumentException
import java.net.URI
import java.util.ArrayList
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class CodeService(
    val codeRepository: CodeRepository,
    val projectService: ProjectService,
    val codelistService: CodelistService
)  {
    fun getCodeByRefFromService(
        projectRef: String,
        codelistRef: String,
        codeRef: String
    ): Response {

        /**
         * todo
         * reuse
         */
        return if (projectService.refExists(projectRef) &&
            codelistService.refExists(codelistRef) &&
                refExists(codeRef)) {
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            val foundCodelist = project.codelist.find { codelist ->
                codelist.ref == codelistRef
            }
            if (foundCodelist != null) {
                val foundCodeInCodelist = foundCodelist.codes.find { code ->
                    code.ref == codeRef
                }
                return if (foundCodeInCodelist != null) {
                    val codeMapper = CodeMapper().fromEntity(foundCodeInCodelist)
                    Response.ok(codeMapper).build()
                } else {
                    Response.status(Response.Status.BAD_REQUEST).build()
            }} else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        }else {
             Response.status(Response.Status.NOT_FOUND).build()
        }
    }



    fun listCodesFromService(projectRef: String, codelistRef: String): Response {
        return try {
            if (projectService.refExists(projectRef)) {
                //list code by project ref
                val projectFromRef = projectService.getProjectByRefCustomRepo(projectRef)!!

                // val requirement = requirementService.getRequirementByRefFromService(projectRef, codelistRef)!!.
                val codelist = projectFromRef.codelist.find { codelist -> codelist.ref == codelistRef }!!.codes

                //convert to array of form
               val codeFormList = ArrayList<CodeForm>()
                for (code in codelist) codeFormList.add(CodeMapper().fromEntity(code))

                //returns the custom code form
                Response.ok(codeFormList).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("GET list of code failed")
        }
    }

    fun createCodeFromService(
        projectRef: String,
        codelistRef: String,
        newCode: CodeForm
    ): Response {
        //adds a code to relevant codelist
        try {
            val codeMapper = CodeMapper().toEntity(newCode)
            if (projectService.refExists(projectRef) &&
                codelistService.refExists(codelistRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!

                //project.codelist.find { requirement -> requirement.ref == codelistRef }?.codes?.add(codeMapper)
                val foundCodelist = project.codelist.find { codelist -> codelist.ref == codelistRef }!!.codes
                foundCodelist.add(codeMapper)
                projectService.updateProject(project.id, project)
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            return if (codeMapper.isPersistent) {
                Response.created(URI.create("/api/v1/projects/$projectRef/codelist/$codelistRef/codes/" + newCode.ref))
                    .build();
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("POST code failed")
        }
    }

    fun deleteCodeFromService(
        projectRef: String,
        codelistRef: String,
        codeRef: String
    ): Response {
        return try {
            //val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val code = getCodeByRefFromService(projectRef, codeRef)
            if (projectService.refExists(projectRef) &&
                codelistService.refExists(codelistRef) &&
                refExists(codeRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                val foundCodelist = project.codelist.find { codelist ->
                    codelist.ref == codelistRef
                }!!.codes
                val foundCode = foundCodelist.find { code ->
                    code.ref == codeRef
                }
                if (foundCode != null) {
                    foundCodelist.remove(foundCode)
                    projectService.updateProject(project.id, project)
                    Response.noContent().build()
                } else {
                    Response.status(Response.Status.BAD_REQUEST).build()
                }
            } else
                Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("DELETE code failed!")
        }
    }

    fun updateCodeFromService(
        projectRef: String,
        codelistRef: String,
        codeRef: String,
        updateCode: CodeFormUpdate
    ): Response {
        try {
            if (projectService.refExists(projectRef) &&
                codelistService.refExists(codelistRef) &&
                refExists(codeRef)
            ) {
                // if code exists in this project
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                //val foundProduct = getProductByRefCustomRepo(codeRef)

                val foundCodelist = project.codelist.find { codelist ->
                    codelist.ref == codelistRef
                }!!.codes
                val foundCode = foundCodelist.find { code ->
                    code.ref == codeRef
                }
                val codeMapper = CodeUpdateMapper().toEntity(updateCode)
                //if (code.project.ref == project.ref)

                return if (foundCode != null) {
                    codeRepository.update(
                        "title = ?1,  description = ?2  where id = ?3",
                        codeMapper.title,
                        codeMapper.description,
                        foundCode.id
                    )
                    Response.ok(updateCode).build()
                } else {
                    Response.status(Response.Status.BAD_REQUEST).build()
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("UPDATE requirement variant failed!")
        }
    }

    fun listCodes(): MutableList<Code> = codeRepository.listAll()

    fun getCode(id: Long): Code = codeRepository.findById(id)

    fun createCode(requirement: Code) = codeRepository.persistAndFlush(requirement)

    fun exists(id: Long): Boolean = codeRepository.count("id", id) == 1L
    fun refExists(ref: String): Boolean = codeRepository.count("ref", ref) == 1L

    fun deleteCode(id: Long) = codeRepository.deleteById(id)

    fun updateCode(id: Long, requirement: Code) {
       // codeRepository.update(
          //  "comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
            // requirement.comment, requirement.version, requirement.bankId, requirement.date, id
        //)

    }

}