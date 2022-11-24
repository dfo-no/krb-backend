package org.kravbank.service

import com.google.common.io.ByteStreams
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import org.kravbank.lang.BackendException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@QuarkusTest
class WrapperServiceTest {

    @Inject
    lateinit var wrapperService: WrapperService

    @Test
    @Throws(BackendException::class)
    fun testCreateHtml() {
        try {
            // TODO Write somewhere else
            FileOutputStream("target/wrapper.html").use { outputStream ->
                javaClass.getResourceAsStream("/specification.json").use { inputStream ->
                    val result = wrapperService.createHtml(inputStream!!)
                    ByteStreams.copy(result, outputStream)
                }
            }
        } catch (e: IOException) {
            throw BackendException(e.message!!, e)
        }
    }

    @Test
    @Throws(BackendException::class)
    fun testCreatePdf() {
        try {
            // TODO Write somewhere else
            FileOutputStream("target/wrapper.pdf").use { outputStream ->
                javaClass.getResourceAsStream("/specification.json").use { inputStream ->
                    val result = wrapperService.createPdf(inputStream!!)
                    ByteStreams.copy(result, outputStream)
                }
            }
        } catch (e: IOException) {
            throw BackendException(e.message!!, e)
        }
    }
}