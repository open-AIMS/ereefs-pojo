package au.gov.aims.ereefs.pojo.job;

import au.gov.aims.ereefs.pojo.task.TaskDaoFileImpl;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

/**
 * Extends {@link AbstractJobDaoImplTest} for testing the {@code File}-implementation of
 * {@link JobDao}.
 */
public class JobDaoFileImplTest extends AbstractJobDaoImplTest {

    /**
     * Constructor to instantiate {@link #jobDao} and {@link #testDataPopulator} to populate the
     * system with test data.
     */
    public JobDaoFileImplTest() {
        super();

        // Instantiate the DAO, pointing it at a randomly generated path.
        final String randomPath = System.getProperty("user.dir") + File.separator +
            "target" + File.separator + "test-output" + File.separator +
            UUID.randomUUID().toString() + File.separator;
        this.jobDao = new JobDaoFileImpl(
            randomPath + "job",
            new TaskDaoFileImpl(randomPath + "task")
        );

        // Instantiate the test data populator.
        this.testDataPopulator = new TestDataPopulator(this.jobDao);

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
