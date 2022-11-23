package org.kravbank.service;

import kotlin.io.ByteStreamsKt;
import net.sf.saxon.s9api.*;
import org.kravbank.lang.BackendException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class WrapperService {

    private Processor processor;

    private XsltExecutable executable;

    @Inject
    public void init() throws BackendException {
        // Initiate a Saxon processor
        this.processor = new Processor(false);

        // Load the XSLT used to create HTML for wrapper
        try (InputStream inputStream = getClass().getResourceAsStream("/xslt/wrapper.xslt")) {
            this.executable = this.processor.newXsltCompiler().compile(new StreamSource(inputStream));
        } catch (SaxonApiException | IOException e) {
            throw new BackendException("Unable to load XSLT parser.", e);
        }
    }

    public InputStream createHtml(InputStream jsonSource) throws BackendException {
        try {
            var baos = new ByteArrayOutputStream();

            var transformer = executable.load();
            transformer.setSource(new StreamSource(new ByteArrayInputStream("<Input />".getBytes())));
            transformer.setDestination(processor.newSerializer(baos));
            transformer.setParameter(new QName("json"), new XdmAtomicValue(new String(ByteStreamsKt.readBytes(jsonSource))));
            transformer.transform();

            return new ByteArrayInputStream(baos.toByteArray());
        } catch (SaxonApiException e) {
            throw new BackendException("Unable to create HTML from JSON.", e);
        }
    }
}
