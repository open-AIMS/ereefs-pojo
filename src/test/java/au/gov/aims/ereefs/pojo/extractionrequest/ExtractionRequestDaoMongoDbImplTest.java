package au.gov.aims.ereefs.pojo.extractionrequest;

import au.gov.aims.ereefs.pojo.metadata.MetadataDaoMongoDbImpl;
import au.gov.aims.ereefs.pojo.utils.DateTimeConstants;
import au.gov.aims.ereefs.pojo.utils.MongoDbTestUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Test cases for {@link ExtractionRequestDaoMongoDbImpl}.
 */
public class ExtractionRequestDaoMongoDbImplTest extends AbstractExtractionRequestDaoImplTest {

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "extraction_request";

    /**
     * Cached reference to the {@code MongoDB} collection for data manipulation during testing.
     */
    protected MongoCollection<Document> mongoCollection = null;

    /**
     * Cached reference to the {@code MongoDB}-based implementation of the
     * {@link ExtractionRequestDao} interface.
     */
    protected ExtractionRequestDao mongoDbDao;

    /**
     * Cached reference to the {@code File}-based implementation of the {@link ExtractionRequestDao}
     * interface.
     */
    protected ExtractionRequestDao fileDao;

    @Override
    protected ExtractionRequestDao makeDao() {
        return this.mongoDbDao;
    }

    /**
     * Constructor to instantiate the {@link ExtractionRequestDao} instances.
     */
    public ExtractionRequestDaoMongoDbImplTest() {
        MongoDatabase mongoDatabase = MongoDbTestUtils.getMongoDatabase();
        this.mongoCollection = mongoDatabase.getCollection(MetadataDaoMongoDbImpl.COLLECTION_NAME, Document.class);
        this.mongoDbDao = new ExtractionRequestDaoMongoDbImpl(mongoDatabase);
        this.fileDao = new ExtractionRequestDaoFileImpl(TEST_DATA_PATH);
    }

    /**
     * Set up method for preparing the test data.
     */
    @Before
    public void runBeforeTestMethod() {

        MongoDbTestUtils.clearCollection(this.mongoCollection);

        // Loop through all records from the file-based dao and insert them into the database.
        final ExtractionRequestDao dao = this.makeDao();
        dao.persist(this.fileDao.getById(TEST_ONE_ID));
        dao.persist(this.fileDao.getById(TEST_TWO_ID));
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

    /**
     * Verify the {@code JobId} can be updated.
     */
    @Test
    public void testJobIdUpdate() {
        final ExtractionRequestDao dao = this.makeDao();

        // Verify the expected default values.
        final String ORIGINAL_JOB_ID = "ParentJobId";
        ExtractionRequest extractionRequest = dao.getById(TEST_TWO_ID);
        Assertions
            .assertThat(extractionRequest.getExecutionContext().get("jobId").asText())
            .isEqualTo(ORIGINAL_JOB_ID);
        Assertions
            .assertThat(extractionRequest.getExecutionContext().get("otherFlag").asBoolean())
            .isEqualTo(true);
        LocalDateTime createdAt = extractionRequest.getCreatedAt();
        Assertions
            .assertThat(createdAt)
            .isNotNull()
            .isEqualTo(
                ZonedDateTime
                    .parse("2020-01-17T00:38:05.323Z")
                    .withZoneSameInstant(DateTimeConstants.DEFAULT_TIME_ZONE_ID)
                    .toLocalDateTime()
            );
        // Note we don't compare "updatedAt" with the source file because it has been persisted
        // since then, which updates the "updatedAt" property.
        LocalDateTime updatedAt = extractionRequest.getUpdatedAt();
        Assertions
            .assertThat(updatedAt)
            .isNotNull();

        // Update JobId.
        final String UPDATED_JOB_ID = "updatedJobId";
        extractionRequest.getExecutionContext().put("jobId", UPDATED_JOB_ID);
        dao.persist(extractionRequest);

        // Verify fields that should have been updated.
        extractionRequest = dao.getById(TEST_TWO_ID);
        Assertions
            .assertThat(extractionRequest.getExecutionContext().get("jobId").asText())
            .isEqualTo(UPDATED_JOB_ID);
        Assertions
            .assertThat(extractionRequest.getExecutionContext().get("otherFlag").asBoolean())
            .isEqualTo(true);
        Assertions
            .assertThat(extractionRequest.getCreatedAt())
            .isEqualTo(createdAt);
        Assertions
            .assertThat(extractionRequest.getUpdatedAt())
            .isNotEqualTo(updatedAt);
        Assertions
            .assertThat(extractionRequest.getUpdatedAt().isAfter(updatedAt))
            .isTrue();
        Assertions
            .assertThat(extractionRequest.getUpdatedAt().isAfter(createdAt))
            .isTrue();
        updatedAt = extractionRequest.getUpdatedAt();

        // Update JobId again.
        extractionRequest.getExecutionContext().put("jobId", ORIGINAL_JOB_ID);
        dao.persist(extractionRequest);

        // Verify fields that should have been updated.
        extractionRequest = dao.getById(TEST_TWO_ID);
        Assertions
            .assertThat(extractionRequest.getExecutionContext().get("jobId").asText())
            .isEqualTo(ORIGINAL_JOB_ID);
        Assertions
            .assertThat(extractionRequest.getExecutionContext().get("otherFlag").asBoolean())
            .isEqualTo(true);
        Assertions
            .assertThat(extractionRequest.getCreatedAt())
            .isEqualTo(createdAt);
        Assertions
            .assertThat(extractionRequest.getUpdatedAt())
            .isNotEqualTo(updatedAt);
        Assertions
            .assertThat(extractionRequest.getUpdatedAt().isAfter(updatedAt))
            .isTrue();
        Assertions
            .assertThat(extractionRequest.getUpdatedAt().isAfter(createdAt))
            .isTrue();

        // Write JobId to the first test file that does NOT contain an ExecutionContext.
        extractionRequest = dao.getById(TEST_ONE_ID);
        Assertions
            .assertThat(extractionRequest.getExecutionContext())
            .isNullOrEmpty();
        createdAt = extractionRequest.getCreatedAt();
        updatedAt = extractionRequest.getUpdatedAt();
        extractionRequest.getExecutionContext().put("jobId", "dummy");
        dao.persist(extractionRequest);
        extractionRequest = dao.getById(TEST_ONE_ID);
        Assertions
            .assertThat(extractionRequest.getExecutionContext())
            .isNotEmpty();
        Assertions
            .assertThat(extractionRequest.getExecutionContext().get("jobId").asText())
            .isEqualTo("dummy");
        Assertions
            .assertThat(extractionRequest.getCreatedAt())
            .isEqualTo(createdAt);
        Assertions
            .assertThat(extractionRequest.getUpdatedAt())
            .isNotEqualTo(updatedAt);
        Assertions
            .assertThat(extractionRequest.getUpdatedAt().isAfter(updatedAt))
            .isTrue();
        Assertions
            .assertThat(extractionRequest.getUpdatedAt().isAfter(createdAt))
            .isTrue();


    }

    /**
     * Test setting and getting JobId.
     */
    @Test
    public void testJobId() {
        final ExtractionRequestDao dao = this.makeDao();
        final String NEW_JOB_ID = "newJobId";

        Assertions
            .assertThat(dao.getJobId(TEST_TWO_ID))
            .isEqualTo("ParentJobId");
        dao.setJobId(TEST_TWO_ID, NEW_JOB_ID);
        Assertions
            .assertThat(dao.getJobId(TEST_TWO_ID))
            .isEqualTo(NEW_JOB_ID);

        Assertions
            .assertThat(dao.getJobId(TEST_ONE_ID))
            .isNull();
        dao.setJobId(TEST_ONE_ID, NEW_JOB_ID);
        Assertions
            .assertThat(dao.getJobId(TEST_ONE_ID))
            .isEqualTo(NEW_JOB_ID);

    }

}
