package au.gov.aims.ereefs.pojo.metadata;

import au.gov.aims.ereefs.pojo.utils.MongoDbTestUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Test cases for {@link MetadataDaoMongoDbImpl}.
 */
public class MetadataDaoMongoDbImplTest extends AbstractMetadataDaoImplTest {

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "metadata";

    /**
     * Cached reference to the {@code MongoDB} collection for data manipulation during testing.
     */
    protected MongoCollection<Document> mongoCollection = null;

    /**
     * Cached reference to the {@code MongoDB}-based implementation of the
     * {@link MetadataDao} interface.
     */
    protected MetadataDao mongoDbDao;

    /**
     * Cached reference to the {@code File}-based implementation of the {@link MetadataDao}
     * interface.
     */
    protected MetadataDao fileDao;

    @Override
    protected MetadataDao makeDao() {
        return this.mongoDbDao;
    }

    /**
     * Constructor to instantiate the {@link MetadataDao} instances.
     */
    public MetadataDaoMongoDbImplTest() {
        MongoDatabase mongoDatabase = MongoDbTestUtils.getMongoDatabase();
        this.mongoCollection = mongoDatabase.getCollection(MetadataDaoMongoDbImpl.COLLECTION_NAME, Document.class);
        this.mongoDbDao = new MetadataDaoMongoDbImpl(mongoDatabase);
        this.fileDao = new MetadataDaoFileImpl(TEST_DATA_PATH);
    }

    /**
     * Set up method for preparing the test data.
     */
    @Before
    public void runBeforeTestMethod() {

        MongoDbTestUtils.clearCollection(this.mongoCollection);

        // Loop through all records from the file-based dao and insert them into the database.
        final MetadataDao dao = this.makeDao();
        final List<Metadata> downloadMetadata = this.fileDao.getByDefinitionId(
            DOWNLOAD_DEFINITIONS_ID,
            NetCDFMetadata.TYPE
        );
        for (final Metadata defn : downloadMetadata) {
            dao.persist(defn);
        }
        final List<Metadata> productMetadata = this.fileDao.getByDefinitionId(
            PRODUCT_DEFINITIONS_ID,
            NetCDFMetadata.TYPE
        );
        for (final Metadata defn : productMetadata) {
            dao.persist(defn);
        }
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
