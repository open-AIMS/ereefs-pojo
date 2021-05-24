package au.gov.aims.ereefs.pojo.job;

import au.gov.aims.ereefs.pojo.task.TaskDaoMongoDbImpl;
import au.gov.aims.ereefs.pojo.utils.MongoDbTestUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;

/**
 * Extends {@link AbstractJobDaoImplTest} for testing the {@code MongoDB}-implementation of
 * {@link JobDao}.
 */
public class JobDaoMongoDbImplTest extends AbstractJobDaoImplTest {

    /**
     * Cached reference to the {@code MongoDB} collection for data manipulation during testing.
     */
    protected MongoCollection<Document> mongoCollection = null;

    /**
     * Constructor to instantiate {@link #jobDao} and {@link #testDataPopulator} to populate the
     * system with test data.
     */
    public JobDaoMongoDbImplTest() {
        super();

        // Instantiate the DAO.
        MongoDatabase mongoDatabase = MongoDbTestUtils.getMongoDatabase();
        this.mongoCollection = mongoDatabase.getCollection(JobDaoMongoDbImpl.COLLECTION_NAME, Document.class);
        this.jobDao = new JobDaoMongoDbImpl(
            mongoDatabase,
            new TaskDaoMongoDbImpl(mongoDatabase)
        );

        // Instantiate the test data populator.
        this.testDataPopulator = new TestDataPopulator(this.jobDao);

    }

    /**
     * Set up method for preparing the test data.
     */
    public void runBeforeTestMethod() {
        MongoDbTestUtils.clearCollection(this.mongoCollection);
        super.runBeforeTestMethod();
    }

    /**
     * Invokes {@link super#doTestGetById()} to test {@link JobDao#persist(Object)}.
     */
    @Test
    public void testGetById() {
        this.doTestGetById();
    }

    /**
     * Invokes {@link super#doTestFindAllActive()} to test {@link JobDao#findAllActive()}.
     */
    @Test
    public void testFindAllActive() {
        this.doTestFindAllActive();
    }

}
