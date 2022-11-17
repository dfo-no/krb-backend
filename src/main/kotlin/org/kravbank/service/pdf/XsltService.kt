package org.kravbank.service.pdf.java

import net.sf.saxon.s9api.Processor
import net.sf.saxon.s9api.SaxonApiException
import net.sf.saxon.s9api.Serializer
import java.io.File
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource

class XsltService {
    fun transform(): Serializer? {
        try {
            val xsltStream = javaClass.classLoader.getResourceAsStream("test.xsl")
            val xmlStream = javaClass.classLoader.getResourceAsStream("test.xml")
            val xslSource: Source = StreamSource(xsltStream)
            val xmlSource: Source = StreamSource(xmlStream)
            val processor = Processor(false)
            val compiler = processor.newXsltCompiler()
            val stylesheet = compiler.compile(xslSource)
            val out = processor.newSerializer(File("result.xml"))
            out.setOutputProperty(Serializer.Property.METHOD, "xml")
            out.setOutputProperty(Serializer.Property.INDENT, "yes")
            val transformer = stylesheet.load30()
            transformer.transform(xmlSource, out)
            out.close()
            return out
        } catch (e: SaxonApiException) {
            e.printStackTrace()
        }
        return null
    }
}