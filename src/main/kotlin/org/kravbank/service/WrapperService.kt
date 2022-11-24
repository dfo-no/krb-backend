package org.kravbank.service

import com.google.common.io.ByteStreams
import com.lowagie.text.pdf.PdfFileSpecification
import net.sf.saxon.s9api.*
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.kravbank.lang.BackendException
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.xml.transform.stream.StreamSource

@ApplicationScoped
class WrapperService
@Inject @Throws(BackendException::class) constructor() {

    // Saxon processor
    private final var processor: Processor = Processor(false)

    private final var executable: XsltExecutable

    @ConfigProperty(name = "kravbank.frontend.link")
    lateinit var krbLink: String

    init {
        try {
            // Load the XSLT used to create HTML for wrapper
            javaClass.getResourceAsStream("/xslt/wrapper.xslt").use { inputStream ->
                executable = processor.newXsltCompiler().compile(
                    StreamSource(inputStream)
                )
            }
        } catch (e: SaxonApiException) {
            throw BackendException("Unable to load XSLT parser.", e)
        } catch (e: IOException) {
            throw BackendException("Unable to load XSLT parser.", e)
        }
    }

    @Throws(BackendException::class)
    fun createPdf(jsonSource: InputStream): InputStream {
        // Read json content for reuse
        val json = ByteArrayOutputStream()
        ByteStreams.copy(jsonSource, json)

        // Create HTML
        val html = createHtml(ByteArrayInputStream(json.toByteArray()))

        val pdf = ByteArrayOutputStream()
        val renderer = ITextRenderer()

        // Render HTML as PDF
        renderer.sharedContext.isPrint = true
        renderer.sharedContext.isInteractive = false
        renderer.setDocumentFromString(String(ByteStreams.toByteArray(html)))
        renderer.layout()
        renderer.createPDF(pdf, false)

        // Add attachment
        renderer.writer.addFileAttachment(
            PdfFileSpecification.fileEmbedded(renderer.writer, null, "bank.json", json.toByteArray()))

        // Close file
        renderer.finishPDF()

        return ByteArrayInputStream(pdf.toByteArray());
    }

    @Throws(BackendException::class)
    fun createHtml(jsonSource: InputStream): InputStream {
        return try {
            val baos = ByteArrayOutputStream()

            val transformer = executable.load()
            transformer.setSource(StreamSource(ByteArrayInputStream("<Input />".toByteArray())))
            transformer.destination = processor.newSerializer(baos)
            transformer.setParameter(QName("json"), XdmAtomicValue(String(jsonSource.readBytes())))
            transformer.setParameter(QName("krb_link"), XdmAtomicValue(krbLink))
            transformer.transform()

            ByteArrayInputStream(baos.toByteArray())
        } catch (e: SaxonApiException) {
            throw BackendException("Unable to create HTML from JSON.", e)
        }
    }
}