package org.kravbank.api

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.domain.Codelist
import org.kravbank.domain.Product
import org.kravbank.domain.Project


@QuarkusTest
internal class CodelistResourceTest () {
/*

    @Inject
    var projectService: ProjectService? = null

    @InjectMock
    var projectRepository: ProjectRepository? = null

 */
/*
    @BeforeEach
    fun setUp() {
        `when`(bookRepository.findBy("Frank Herbert"))
            .thenReturn(
                Arrays.stream(
                    arrayOf(
                        Book("Dune", "Frank Herbert"),
                        Book("Children of Dune", "Frank Herbert")
                    )
                )
            )
    }

    @Test
    fun whenFindByAuthor_thenBooksShouldBeFound() {
        assertEquals(2, libraryService.find("Frank Herbert").size())
    }


 */
    val baseURI= "http://localhost:8080"
    val basePath = "/api/v1/"

   val useProjectRef = null //findProjectRef()

    /*
    fun findProjectRef() : Project {
        projectResourceTest.createProject()
        val project = projectService.getProject(0)
        return projectService.getProjectByRef(project.ref)!!
    }
   */



    @Test
    fun getCodelist() {

       // val statusCode = RestAssured.given().get("http://localhost:8080/api/v1/projects").statusCode

      //  print(statusCode)



        val path = "$baseURI$basePath/$useProjectRef/codelists/1"
        println(path)

        //val response = RestAssured.get("http://localhost:8080/kt/projects")
        // println(response.statusCode())
        RestAssured.given()
            .`when`().get("$baseURI$basePath/projects")
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))

    }

    @Test
    fun listCodelists() {

        //val response = RestAssured.get("http://localhost:8080/kt/projects")
        // println(response.statusCode())
        RestAssured.given()
            .`when`().get("$baseURI$basePath/projects/")
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))


    }

    @Test
    fun createCodelist() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "$baseURI"
        //RestAssured.port = 8080;
        RestAssured.basePath = "$basePath";

        val product = Product();
        product.title ="Integrasjonstittle produkttittel"
        product.description="Integrasjonstest produktbeskrivelse"
        product.deletedDate="21-02-91"

        val codelist = Codelist()

        codelist.title = "Integrasjonstest - Tittel 1"
        codelist.description = "Integrasjonstest - Beskrivelse 1"


        val projectRef = "/dsfdsf2435-2"

        RestAssured.given()
            .`when`()
            .body(codelist)
            .header("Content-type", "application/json")
            .post("$projectRef/codelists")
            .then()
            .statusCode(201) //envt 200


    }

    @Test
    fun deleteCodelistById() {
    }

    @Test
    fun updateCodelist() {
    }

    @Test
    fun getCodelistService() {
    }

    @Test
    fun getProjectService() {
    }
}