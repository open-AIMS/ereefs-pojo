package au.gov.aims.ereefs.pojo.extractionrequest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.Assertions;

/**
 * Abstract base test class that implements tests that are common to all
 * {@link ExtractionRequestDao} implementations.
 */
abstract public class AbstractExtractionRequestDaoImplTest {

    final static public String TEST_ONE_ID = "extraction-request-one";
    final static public String TEST_TWO_ID = "extraction-request-two";

    /**
     * Factory method for instantiating a {@link ExtractionRequestDao} instance.
     */
    abstract protected ExtractionRequestDao makeDao();

    /**
     * Verify the {@link ExtractionRequestDao#getById(String)} method returns a
     * {@link ExtractionRequest} object.
     */
    public void doTestGetByIdValid() {
        final ExtractionRequestDao dao = this.makeDao();
        ExtractionRequest extractionRequest = dao.getById(TEST_ONE_ID);
        Assertions
            .assertThat(extractionRequest)
            .hasFieldOrPropertyWithValue("dataCollection", "downloads__ereefs__gbr1_2-0")
            .hasFieldOrPropertyWithValue("dateRangeFrom", "2015-11-01")
            .hasFieldOrPropertyWithValue("dateRangeTo", "2015-11-10")
            .hasFieldOrPropertyWithValue("outputTimeStep", "hourly");
        Assertions
            .assertThat(extractionRequest.getSites()[0])
            .hasFieldOrPropertyWithValue("name", "Site 1")
            .hasFieldOrPropertyWithValue("latitude", "-18.60209401")
            .hasFieldOrPropertyWithValue("longitude", "146.4874632");
        Assertions
            .assertThat(extractionRequest.getVariables())
            .containsExactly("salt");
        Assertions
            .assertThat(extractionRequest.getDepths())
            .containsExactly(-315.0);

        ObjectNode executionContextJson = extractionRequest.getExecutionContext();
        Assertions
            .assertThat(executionContextJson)
            .isNullOrEmpty();

        extractionRequest = dao.getById(TEST_TWO_ID);
        Assertions
            .assertThat(extractionRequest)
            .hasFieldOrPropertyWithValue("dataCollection", "downloads__ereefs__gbr1_2-0")
            .hasFieldOrPropertyWithValue("dateRangeFrom", "2015-11-01")
            .hasFieldOrPropertyWithValue("dateRangeTo", "2015-11-10")
            .hasFieldOrPropertyWithValue("outputTimeStep", "hourly");
        Assertions
            .assertThat(extractionRequest.getSites()[0])
            .hasFieldOrPropertyWithValue("name", "Site 1")
            .hasFieldOrPropertyWithValue("latitude", "-18.60209401")
            .hasFieldOrPropertyWithValue("longitude", "146.4874632");
        Assertions
            .assertThat(extractionRequest.getVariables())
            .containsExactly("salt");
        Assertions
            .assertThat(extractionRequest.getDepths())
            .containsExactly(-315.1);

        executionContextJson = executionContextJson = extractionRequest.getExecutionContext();
        Assertions
            .assertThat(executionContextJson.get("jobId").asText())
            .isEqualTo("ParentJobId");
    }

    /**
     * Verify the {@link ExtractionRequestDao#getById(String)} method returns a null.
     */
    public void doTestGetByIdInvalid() {
        final ExtractionRequestDao dao = this.makeDao();
        final ExtractionRequest extractionRequest = dao.getById("INVALID_METADATA_ID");
        Assertions
            .assertThat(extractionRequest)
            .isNull();

    }

}
