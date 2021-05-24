package au.gov.aims.ereefs.pojo.definition.product;

import au.gov.aims.ereefs.pojo.utils.MongoDbTestUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Test cases for {@link ProductDefinitionDaoMongoDbImpl}.
 */
public class ProductDefinitionDaoMongoDbImplTest extends AbstractProductDefinitionDaoImplTest {

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "definitions" +
        File.separator + "products";

    /**
     * Cached reference to the {@code MongoDB} collection for data manipulation during testing.
     */
    protected MongoCollection<Document> mongoCollection = null;

    /**
     * Cached reference to the {@code MongoDB}-based implementation of the
     * {@link ProductDefinitionDao} interface.
     */
    protected ProductDefinitionDao mongoDbDao;

    /**
     * Cached reference to the {@code File}-based implementation of the
     * {@link ProductDefinitionDao} interface.
     */
    protected ProductDefinitionDao fileDao;

    /**
     * Constructor to instantiate the {@link ProductDefinitionDao} instances.
     */
    public ProductDefinitionDaoMongoDbImplTest() {
        MongoDatabase mongoDatabase = MongoDbTestUtils.getMongoDatabase();
        this.mongoCollection = mongoDatabase.getCollection(ProductDefinitionDaoMongoDbImpl.COLLECTION_NAME, Document.class);
        this.mongoDbDao = new ProductDefinitionDaoMongoDbImpl(mongoDatabase);
        this.fileDao = new ProductDefinitionDaoFileImpl(TEST_DATA_PATH);
    }

    @Override
    protected ProductDefinitionDao makeDao() {
        return this.mongoDbDao;
    }

    /**
     * Helper method that deletes all records from the collection, and then populates the
     * collection with the JSON retrieved from {@link #fileDao}.
     */
    @Before
    public void runBeforeTestMethod() {

        MongoDbTestUtils.clearCollection(this.mongoCollection);

        // Loop through all records from the file-based dao and insert them into the database.
        for (ProductDefinition productDefinition : this.fileDao.findAllEnabled()) {
            this.mongoDbDao.persist(productDefinition);
        }
    }

    /**
     * Verify that the database table has been pre-populated with all of the records from the file-
     * based DAO.
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

}
