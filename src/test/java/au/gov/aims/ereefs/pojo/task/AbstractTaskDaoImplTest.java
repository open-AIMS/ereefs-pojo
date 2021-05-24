package au.gov.aims.ereefs.pojo.task;

import org.assertj.core.api.Assertions;

import java.util.List;

/**
 * Abstract base test class that implements tests that are common to all
 * {@link TaskDao} implementations.
 */
abstract public class AbstractTaskDaoImplTest {

    final static public String AGGREGATE_JOB_ID = "Job_ncaggregate";

    final static public String BASIC_JOB_ID = "Job_basic";

    final static public String OTHER_JOB_ID = "Job_other";


    /**
     * Factory method for instantiating a {@link TaskDao} instance.
     */
    abstract protected TaskDao makeDao();

    /**
     * Verify that {@link TaskDao#getByJobId(String)} returns the expected {@link Task}s for
     * {@link #AGGREGATE_JOB_ID}.
     */
    protected void doTestGetByAggregateJobId() {
        final TaskDao dao = this.makeDao();
        List<Task> tasks = dao.getByJobId(AGGREGATE_JOB_ID);
        Assertions
            .assertThat(tasks)
            .hasSize(2);
    }

    /**
     * Verify that {@link TaskDao#getByJobId(String)} returns the expected {@link Task}s for
     * {@link #BASIC_JOB_ID}.
     */
    protected void doTestGetByBasicJobId() {
        final TaskDao dao = this.makeDao();
        List<Task> tasks = dao.getByJobId(BASIC_JOB_ID);
        Assertions
            .assertThat(tasks)
            .hasSize(2);
    }

    /**
     * Verify that {@link TaskDao#getByJobId(String)} returns the expected {@link Task}s for
     * {@link #OTHER_JOB_ID}.
     */
    protected void doTestGetByOtherJobId() {
        final TaskDao dao = this.makeDao();
        List<Task> tasks = dao.getByJobId(OTHER_JOB_ID);
        Assertions
            .assertThat(tasks)
            .hasSize(1);
    }

}
