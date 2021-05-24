package au.gov.aims.ereefs.pojo.task;

import org.junit.Test;

import java.io.File;

/**
 * Test cases for {@link TaskDao}.
 */
public class TaskDaoFileImplTest extends AbstractTaskDaoImplTest {

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "task";

    /**
     * Factory method for instantiating a {@link TaskDao} instance.
     */
    protected TaskDao makeDao() {
        return new TaskDaoFileImpl(TEST_DATA_PATH);
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

}
