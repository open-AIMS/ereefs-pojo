package au.gov.aims.ereefs.pojo.definition.product;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;

/**
 * Test cases for {@link ProductDefinitionDaoFileImpl}.
 */
public class ProductDefinitionDaoFileImplTest extends AbstractProductDefinitionDaoImplTest {

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "definitions" +
        File.separator + "products";

    /**
     * Factory method for instantiating a {@link ProductDefinitionDao} instance.
     */
    protected ProductDefinitionDao makeDao() {
        return new ProductDefinitionDaoFileImpl(TEST_DATA_PATH);
    }

    /**
     * Invoke {@link #doTestFindAllEnabled()} to test {@link ProductDefinitionDao#findAllEnabled()}.
     */
    @Test
    public void testFindAllEnabled() {
        super.doTestFindAllEnabled();
    }

    /**
     * Invoke {@link #doTestProductNcAggregate()}.
     */
    @Test
    public void testProductNcAggregate() {
        super.doTestProductNcAggregate();
    }

    /**
     * Invoke {@link #doTestProductNcAnimate()}.
     */
    @Test
    public void testProductNcAnimate() {
        super.doTestProductNcAnimate();
    }

    /**
     * Verify the {@link ProductDefinitionDao#persist(Object)} method throws a
     * {@code RuntimeException}.
     */
    @Test
    public void getPersist() {
        final ProductDefinitionDao dao = this.makeDao();
        Assertions
            .assertThatThrownBy(() -> dao.persist(null))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Not implemented.");
    }

}
