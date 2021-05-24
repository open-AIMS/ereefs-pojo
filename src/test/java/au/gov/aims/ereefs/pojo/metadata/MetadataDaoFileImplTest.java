package au.gov.aims.ereefs.pojo.metadata;

import org.junit.Test;

import java.io.File;

/**
 * Test cases for {@link MetadataDao}.
 */
public class MetadataDaoFileImplTest extends AbstractMetadataDaoImplTest {

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "metadata";

    /**
     * Factory method for instantiating a {@link MetadataDao} instance.
     */
    protected MetadataDao makeDao() {
        return new MetadataDaoFileImpl(TEST_DATA_PATH);
    }

    /**
     * Invoke {@link #doTestGetByDefinitionIdValid()}.
     */
    @Test
    public void testGetByDefinitionIdValid() {
        super.doTestGetByDefinitionIdValid();
    }

    /**
     * Invoke {@link #doTestGetByDefinitionIdInvalid()}.
     */
    @Test
    public void testGetByDefinitionIdInvalid() {
        super.doTestGetByDefinitionIdInvalid();
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
