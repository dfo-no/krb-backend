package org.kravbank.service.pdf;

import com.fasterxml.jackson.databind.JsonNode;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

//TODO: ikke funnet tilsvarende dependency "com.itextpdf" for quarkus/kotlin.

public class PdfService {

    private PdfService() {
        throw new IllegalStateException("Class not ment to be instanciated");
    }

    public static ByteArrayInputStream generateSpecification(JsonNode jsonNode) {
        var document = new Document();
        var out = new ByteArrayOutputStream();
        var ret = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            Paragraph p = new Paragraph();
            p.add("Kravbank Spesifikasjon");

            p.add(new Paragraph("Spesifikasjonen inneholder et vedlegg i JSON-format"));
            p.add(new Paragraph("Versjon 1"));
            document.add(p);
            document.close();

            String prettyString = jsonNode.toPrettyString();
            byte[] jsonBytes = prettyString.getBytes();

            var reader = new PdfReader(out.toByteArray());
            var stamper = new PdfStamper(reader, ret);
            PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(stamper.getWriter(), null, "bank.json", jsonBytes);
            stamper.addFileAttachment("Kravbank spesifikasjon beskrivelse", fs);
            stamper.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(ret.toByteArray());
    }

    public static ByteArrayInputStream generateResponse(JsonNode jsonNode) {
        var document = new Document();
        var out = new ByteArrayOutputStream();
        var ret = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            Paragraph p = new Paragraph();
            p.add("Kravbank Spesifikasjon");

            p.add(new Paragraph("Responsen inneholder et vedlegg i JSON-format"));
            p.add(new Paragraph("Versjon 1"));
            document.add(p);
            document.close();

            String prettyString = jsonNode.toPrettyString();
            byte[] jsonBytes = prettyString.getBytes();

            var reader = new PdfReader(out.toByteArray());
            var stamper = new PdfStamper(reader, ret);
            PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(stamper.getWriter(), null, "bank.json", jsonBytes);
            stamper.addFileAttachment("Kravbank besvarelse", fs);
            stamper.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(ret.toByteArray());
    }

    public static ByteArrayInputStream generatePrefilledResponse(JsonNode jsonNode) {
        var document = new Document();
        var out = new ByteArrayOutputStream();
        var ret = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            Paragraph p = new Paragraph();
            p.add("Kravbank Spesifikasjon");

            p.add(new Paragraph("Den preutfylte besvarelsen inneholder et vedlegg i JSON-format"));
            p.add(new Paragraph("Versjon 1"));
            document.add(p);
            document.close();

            String prettyString = jsonNode.toPrettyString();
            byte[] jsonBytes = prettyString.getBytes();

            var reader = new PdfReader(out.toByteArray());
            var stamper = new PdfStamper(reader, ret);
            PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(stamper.getWriter(), null, "bank.json", jsonBytes);
            stamper.addFileAttachment("Kravbank preutfylt besvarelse ", fs);
            stamper.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(ret.toByteArray());
    }

}