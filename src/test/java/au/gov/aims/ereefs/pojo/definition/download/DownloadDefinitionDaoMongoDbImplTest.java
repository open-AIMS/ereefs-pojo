package au.gov.aims.ereefs.pojo.definition.download;

import au.gov.aims.ereefs.pojo.utils.MongoDbTestUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Test cases for {@link DownloadDefinitionDaoMongoDbImpl}.
 */
public class DownloadDefinitionDaoMongoDbImplTest {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "definitions" +
        File.separator + "downloads";

    /**
     * Cached reference to the {@code MongoDB} collection for data manipulation during testing.
     */
    protected MongoCollection<Document> mongoCollection = null;

    /**
     * Cached reference to the {@code MongoDB}-based implementation of the
     * {@link DownloadDefinitionDao} interface.
     */
    protected DownloadDefinitionDao mongoDbDao;

    /**
     * Cached reference to the {@code File}-based implementation of the
     * {@link DownloadDefinitionDao} interface.
     */
    protected DownloadDefinitionDao fileDao;

    /**
     * Constructor to instantiate the {@link DownloadDefinitionDao} instances.
     */
    public DownloadDefinitionDaoMongoDbImplTest() {
        MongoDatabase mongoDatabase = MongoDbTestUtils.getMongoDatabase();
        this.mongoCollection = mongoDatabase.getCollection(DownloadDefinitionDaoMongoDbImpl.COLLECTION_NAME, Document.class);
        this.mongoDbDao = new DownloadDefinitionDaoMongoDbImpl(mongoDatabase);
        this.fileDao = new DownloadDefinitionDaoFileImpl(TEST_DATA_PATH);
    }

    /**
     * Helper method that deletes all records from the collection, and then populates the
     * collection with the JSON retrieved from {@link #fileDao}.
     */
    @Before
    public void runBeforeTestMethod() {

        MongoDbTestUtils.clearCollection(this.mongoCollection);

        // Loop through all records from the file-based dao and insert them into MongoDB.
        for (DownloadDefinition downloadDefinition : this.fileDao.findAllEnabled()) {
            this.mongoDbDao.persist(downloadDefinition);
        }
    }

    /**
     * Test the {@link DownloadDefinitionDaoMongoDbImpl#findAllEnabled()} method.
     */
    @Test
    public void testFindAllEnabled() {

        // Retrieve the list from the file-base DAO.
        final List<DownloadDefinition> fileBasedList = this.fileDao.findAllEnabled();

        // Retrieve the list from the MongoDB-based DAO.
        final List<DownloadDefinition> dbBasedList = this.mongoDbDao.findAllEnabled();

        // Verify the lists match.
        Assertions
            .assertThat(fileBasedList)
            .isNotEmpty();
        Assertions
            .assertThat(fileBasedList)
            .hasSameSizeAs(dbBasedList);
        Assertions
            .assertThat(dbBasedList)
            .hasSameSizeAs(fileBasedList);

        for (DownloadDefinition fileBasedDefn : fileBasedList) {
            boolean isFound = false;
            for (DownloadDefinition dbBasedDefn : dbBasedList) {
                if (fileBasedDefn.getId().equals(dbBasedDefn.getId())) {
                    isFound = true;
                    Assertions
                        .assertThat(fileBasedDefn)
                        .isEqualToComparingFieldByFieldRecursively(dbBasedDefn);
                }
            }
            Assertions
                .assertThat(isFound)
                .isTrue();
        }
    }

    /**
     * Verify that a record retrieved from the MongoDB collection via
     * {@link DownloadDefinitionDao#getById(String)} matches the same record retrieved from the
     * file-based DAO.
     */
    @Test
    public void testGetById() {

        final String DOWNLOAD_ID = "downloads__ereefs__gbr1_2-0";

        // Retrieve the data from the DAOs.
        final DownloadDefinition fileBasedDefn = this.fileDao.getById(DOWNLOAD_ID);
        final DownloadDefinition dbBasedDefn = this.mongoDbDao.getById(DOWNLOAD_ID);

        // Verify the records match.
        Assertions
            .assertThat(fileBasedDefn)
            .isNotNull()
            .isEqualToComparingFieldByFieldRecursively(dbBasedDefn);
    }

}
