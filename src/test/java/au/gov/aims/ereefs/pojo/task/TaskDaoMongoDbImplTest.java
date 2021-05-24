package au.gov.aims.ereefs.pojo.task;

import au.gov.aims.ereefs.pojo.metadata.MetadataDao;
import au.gov.aims.ereefs.pojo.metadata.MetadataDaoMongoDbImpl;
import au.gov.aims.ereefs.pojo.utils.MongoDbTestUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Test cases for {@link TaskDaoMongoDbImpl}.
 */
public class TaskDaoMongoDbImplTest extends AbstractTaskDaoImplTest {

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "task";

    /**
     * Cached reference to the {@code MongoDB} collection for data manipulation during testing.
     */
    protected MongoCollection<Document> mongoCollection = null;

    /**
     * Cached reference to the {@code MongoDB}-based implementation of the
     * {@link TaskDao} interface.
     */
    protected TaskDao mongoDbDao;

    /**
     * Cached reference to the {@code File}-based implementation of the {@link TaskDao} interface.
     */
    protected TaskDao fileDao;

    @Override
    protected TaskDao makeDao() {
        return this.mongoDbDao;
    }

    /**
     * Constructor to instantiate the {@link MetadataDao} instances.
     */
    public TaskDaoMongoDbImplTest() {
        MongoDatabase mongoDatabase = MongoDbTestUtils.getMongoDatabase();
        this.mongoCollection = mongoDatabase.getCollection(MetadataDaoMongoDbImpl.COLLECTION_NAME, Document.class);
        this.mongoDbDao = new TaskDaoMongoDbImpl(mongoDatabase);
        this.fileDao = new TaskDaoFileImpl(TEST_DATA_PATH);
    }

    /**
     * Set up method for preparing the test data.
     */
    @Before
    public void runBeforeTestMethod() {

        MongoDbTestUtils.clearCollection(this.mongoCollection);

        // Loop through all records from the file-based dao and insert them into the database.
        final TaskDao dao = this.makeDao();
        for (final Task task : this.fileDao.getByJobId(AGGREGATE_JOB_ID)) {
            dao.persist(task);
        }
        for (final Task task : this.fileDao.getByJobId(BASIC_JOB_ID)) {
            dao.persist(task);
        }
        for (final Task task : this.fileDao.getByJobId(OTHER_JOB_ID)) {
            dao.persist(task);
        }
    }

    /**
     * Invoke {@link #doTestGetByAggregateJobId()}.
     */
    @Test
    public void testGetByAggregateJobId() {
        super.doTestGetByAggregateJobId();
    }

    /**
     * Invoke {@link #doTestGetByBasicJobId()}.
     */
    @Test
    public void testGetByBasicJobId() {
        super.doTestGetByBasicJobId();
    }

    /**
     * Invoke {@link #doTestGetByOtherJobId()}.
     */
    @Test
    public void testGetByOtherJobId() {
        super.doTestGetByOtherJobId();
    }

    /**
     * Test the {@link NcAggregateTask#outcome} property.
     */
    @Test
    public void testPopulateOutcome() {
        final TaskDao dao = this.makeDao();

        // Verify the starting point of the Test.
        final String ID = "Task_ncaggregate_1";
        Task tempTask = dao.getById(ID);
        Assertions
            .assertThat(tempTask)
            .isInstanceOf(NcAggregateTask.class);
        NcAggregateTask task = (NcAggregateTask) tempTask;
        ObjectNode executionContext = task.getExecutionContext();
        Assertions
            .assertThat(executionContext.get("assignToken").asText())
            .isEqualTo("assignToken");
        Assertions
            .assertThat(executionContext.get("retries").asInt())
            .isEqualTo(0);
        Assertions
            .assertThat(executionContext.get("awsBatchJobId").asText())
            .isEqualTo("awsBatchJobId");
        Assertions
            .assertThat(executionContext.get("containerInstanceArn").asText())
            .isEqualTo("containerInstanceArn");
        Assertions
            .assertThat(executionContext.get("logStreamName").asText())
            .isEqualTo("logStreamName");

        // Ensure the Task has no generated files.
        Assertions
            .assertThat(task.getOutcome().getGeneratedFiles())
            .isEmpty();

        // Add a generated file to the Task and persist.
        final String URL = "netcdfOutcomeUrl";
        task.getOutcome().addGeneratedFile(
            new NcAggregateTask.GeneratedFile(NcAggregateTask.GeneratedFileType.NETCDF, URL)
        );
        dao.persist(task);

        // Reload the Task.
        tempTask = dao.getById(ID);
        Assertions
            .assertThat(tempTask)
            .isInstanceOf(NcAggregateTask.class);
        task = (NcAggregateTask) tempTask;

        // Verify the generated file is specified.
        Assertions
            .assertThat(task.getOutcome().getGeneratedFiles())
            .hasSize(1);
        NcAggregateTask.GeneratedFile netcdfGeneratedFile = task.getOutcome().getGeneratedFiles().get(0);
        Assertions
            .assertThat(netcdfGeneratedFile)
            .hasFieldOrPropertyWithValue("type", NcAggregateTask.GeneratedFileType.NETCDF)
            .hasFieldOrPropertyWithValue("url", URL);

        // Verify other fields were not updated.
        executionContext = task.getExecutionContext();
        Assertions
            .assertThat(executionContext.get("assignToken").asText())
            .isEqualTo("assignToken");
        Assertions
            .assertThat(executionContext.get("retries").asInt())
            .isEqualTo(0);
        Assertions
            .assertThat(executionContext.get("awsBatchJobId").asText())
            .isEqualTo("awsBatchJobId");
        Assertions
            .assertThat(executionContext.get("containerInstanceArn").asText())
            .isEqualTo("containerInstanceArn");
        Assertions
            .assertThat(executionContext.get("logStreamName").asText())
            .isEqualTo("logStreamName");
    }

}
