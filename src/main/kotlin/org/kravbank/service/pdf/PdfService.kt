package org.kravbank.service.pdf

import com.fasterxml.jackson.databind.JsonNode
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfFileSpecification
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

class PdfService
private constructor() {
    init {
        throw IllegalStateException("Class not ment to be instanciated")
    }

    companion object {
        fun generateSpecification(jsonNode: JsonNode): ByteArrayInputStream {
            val document = Document()
            val out = ByteArrayOutputStream()
            val ret = ByteArrayOutputStream()
            try {
                PdfWriter.getInstance(document, out)
                document.open()
                val p = Paragraph()
                p.add("Kravbank Spesifikasjon")
                p.add(Paragraph("Spesifikasjonen inneholder et vedlegg i JSON-format"))
                p.add(Paragraph("Versjon 1"))
                document.add(p)
                document.close()
                val prettyString = jsonNode.toPrettyString()
                val jsonBytes = prettyString.toByteArray()
                val reader = PdfReader(out.toByteArray())
                val stamper = PdfStamper(reader, ret)
                val fs = PdfFileSpecification.fileEmbedded(stamper.writer, null, "bank.json", jsonBytes)
                stamper.addFileAttachment("Kravbank spesifikasjon beskrivelse", fs)
                stamper.close()
            } catch (e: DocumentException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ByteArrayInputStream(ret.toByteArray())
        }

        fun generateResponse(jsonNode: JsonNode): ByteArrayInputStream {
            val document = Document()
            val out = ByteArrayOutputStream()
            val ret = ByteArrayOutputStream()
            try {
                PdfWriter.getInstance(document, out)
                document.open()
                val p = Paragraph()
                p.add("Kravbank Spesifikasjon")
                p.add(Paragraph("Responsen inneholder et vedlegg i JSON-format"))
                p.add(Paragraph("Versjon 1"))
                document.add(p)
                document.close()
                val prettyString = jsonNode.toPrettyString()
                val jsonBytes = prettyString.toByteArray()
                val reader = PdfReader(out.toByteArray())
                val stamper = PdfStamper(reader, ret)
                val fs = PdfFileSpecification.fileEmbedded(stamper.writer, null, "bank.json", jsonBytes)
                stamper.addFileAttachment("Kravbank besvarelse", fs)
                stamper.close()
            } catch (e: DocumentException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ByteArrayInputStream(ret.toByteArray())
        }

        fun generatePrefilledResponse(jsonNode: JsonNode): ByteArrayInputStream {
            val document = Document()
            val out = ByteArrayOutputStream()
            val ret = ByteArrayOutputStream()
            try {
                PdfWriter.getInstance(document, out)
                document.open()
                val p = Paragraph()
                p.add("Kravbank Spesifikasjon")
                p.add(Paragraph("Den preutfylte besvarelsen inneholder et vedlegg i JSON-format"))
                p.add(Paragraph("Versjon 1"))
                document.add(p)
                document.close()
                val prettyString = jsonNode.toPrettyString()
                val jsonBytes = prettyString.toByteArray()
                val reader = PdfReader(out.toByteArray())
                val stamper = PdfStamper(reader, ret)
                val fs = PdfFileSpecification.fileEmbedded(stamper.writer, null, "bank.json", jsonBytes)
                stamper.addFileAttachment("Kravbank preutfylt besvarelse ", fs)
                stamper.close()
            } catch (e: DocumentException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ByteArrayInputStream(ret.toByteArray())
        }
    }
}