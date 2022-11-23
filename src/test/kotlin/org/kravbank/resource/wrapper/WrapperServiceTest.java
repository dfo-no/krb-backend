package org.kravbank.resource.wrapper;

import com.google.common.io.ByteStreams;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.kravbank.lang.BackendException;
import org.kravbank.service.WrapperService;

import javax.inject.Inject;
import java.io.IOException;

@QuarkusTest
public class WrapperServiceTest {

    @Inject
    WrapperService wrapperService;

    @Test
    public void testCreateHtml() throws BackendException {
        try (var inputStream = getClass().getResourceAsStream("/specification.json")) {
            var result = wrapperService.createHtml(inputStream);

            ByteStreams.copy(result, System.out);

        } catch (IOException e) {
            throw new BackendException(e.getMessage(), e);
        }
    }
}
