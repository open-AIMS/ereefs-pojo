package au.gov.aims.ereefs.pojo.extractionrequest;

import au.gov.aims.ereefs.pojo.metadata.MetadataDao;
import org.junit.Test;

import java.io.File;

/**
 * Test cases for {@link MetadataDao}.
 */
public class ExtractionRequestDaoFileImplTest extends AbstractExtractionRequestDaoImplTest {

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "extraction_request";

    /**
     * Factory method for instantiating a {@link ExtractionRequestDao} instance.
     */
    protected ExtractionRequestDao makeDao() {
        return new ExtractionRequestDaoFileImpl(TEST_DATA_PATH);
    }

    /**
     * Invoke {@link #doTestGetByIdValid()}.
     */
    @Test
    public void testGetByIdValid() {
        super.doTestGetByIdValid();
    }

    /**
     * Invoke {@link #doTestGetByIdInvalid()}.
     */
    @Test
    public void testGetByIdInvalid() {
        super.doTestGetByIdInvalid();
    }

}
