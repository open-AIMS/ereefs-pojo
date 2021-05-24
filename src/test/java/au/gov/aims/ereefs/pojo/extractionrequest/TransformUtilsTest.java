package au.gov.aims.ereefs.pojo.extractionrequest;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Test cases for {@link TransformUtils}.
 */
public class TransformUtilsTest {

    @Test
    public void extractionRequest_null_exceptionThrown() {
        Assertions
            .assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> TransformUtils.transform(null))
            .withMessage("No ExtractionRequest provided.");
    }

    @Test
    public void extractionRequest_invalidInputId_exceptionThrown() {
        final String inputId = "invalidInputId";
        final ExtractionRequest extractionRequest = new ExtractionRequest(
            "id",
            new ExtractionRequest.Site[0],
            inputId,
            new String[0],
            "2010-01-01",
            "2020-12-31",
            "daily",
            new double[0]
        );
        Assertions
            .assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> TransformUtils.transform(extractionRequest))
            .withMessageStartingWith("Input source")
            .withMessageContaining(inputId)
            .withMessageEndingWith("not supported.");
    }

}
