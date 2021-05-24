package au.gov.aims.ereefs.pojo.job;

import au.gov.aims.ereefs.pojo.shared.StatusEvent;
import au.gov.aims.ereefs.pojo.task.Task;
import org.assertj.core.api.Assertions;
import org.junit.Before;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract base test class that implements tests that are common to all {@link JobDao}
 * implementations.
 */
abstract public class AbstractJobDaoImplTest {

    /**
     * Cached reference to the {@link JobDao} being tested.
     */
    protected JobDao jobDao;

    /**
     * Cached reference to the {@link TestDataPopulator} implementation.
     */
    protected TestDataPopulator testDataPopulator;

    /**
     * Cache of the test data populated from the most recent invocation of
     * {@link #testDataPopulator}.
     */
    protected List<Job> testData;

    /**
     * Set up method for preparing the test data.
     */
    @Before
    public void runBeforeTestMethod() {

        // Populate the test data.
        this.testData = this.testDataPopulator.populate();

    }

    /**
     * Test {@link JobDao#getById(String)} by retrieving data populated by the
     * {@link #testDataPopulator} and comparing against the original data.
     */
    protected void doTestGetById() {

        // Verify every persisted Job.
        for (Job preparedJob : this.testData) {

            // Retrieve the Job.
            final Job persistedJob = this.jobDao.getById(preparedJob.getId());

            Assertions
                .assertThat(persistedJob)
                .isEqualToComparingOnlyGivenFields(
                    preparedJob,
                    "id", "status", "createdBy", "triggeredBy"
                );
            Assertions
                .assertThat(persistedJob.getCreatedBy())
                .isNotEmpty();
            Assertions
                .assertThat(persistedJob.getTriggeredBy())
                .isNotEmpty();

            // Verify the History of the Job.
            final List<StatusEvent> persistedJobHistory = persistedJob.getHistory();
            final List<StatusEvent> preparedJobHistory = preparedJob.getHistory();

            // If Job status is CREATED, then expect only a single History entry, otherwise expect
            // 2.
            if (persistedJob.getStatus().equals(JobStatus.CREATED)) {
                Assertions
                    .assertThat(persistedJobHistory)
                    .hasSize(1);
            } else {
                Assertions
                    .assertThat(persistedJobHistory)
                    .hasSize(2);
            }

            // Verify the same number of history entries.
            Assertions
                .assertThat(persistedJobHistory)
                .hasSameSizeAs(preparedJobHistory);

            // Compare each history entry.
            for (int index = 0; index < preparedJobHistory.size(); index++) {
                Assertions
                    .assertThat(persistedJobHistory.get(index))
                    .isEqualToComparingFieldByField(preparedJobHistory.get(index));
            }

            // Verify the Tasks of the Job.
            final List<Task> persistedJobTasks = persistedJob.getTasks();
            final List<Task> jobTasks = preparedJob.getTasks();

            Assertions
                .assertThat(persistedJobTasks)
                .hasSize(2)
                .hasSameSizeAs(jobTasks);

            int compareCount = 0;
            for (Task persistedJobTask : persistedJobTasks) {
                for (Task jobTask : jobTasks) {
                    if (persistedJobTask.getId().equalsIgnoreCase(jobTask.getId())) {
                        Assertions
                            .assertThat(persistedJobTask)
                            .isEqualToComparingFieldByFieldRecursively(jobTask);
                        compareCount++;
                    }
                }
            }
            Assertions.assertThat(compareCount).isEqualTo(2);

        }

    }

    /**
     * Test {@link JobDao#findAllActive()} by retrieving data populated by the
     * {@link #testDataPopulator} and comparing against the original data.
     */
    protected void doTestFindAllActive() {

        // Extract only Active Jobs from the test data.
        final List<Job> activeTestJobs = this.testData.stream()
            .filter(job -> JobStatusHelper.ACTIVE_JOB_STATUSES.contains(job.getStatus()))
            .collect(Collectors.toList());

        // Retrieve Active Jobs from database.
        List<Job> activePersistedJobs = this.jobDao.findAllActive();

        // Ensure the expected number of Jobs has been retrieved.
        Assertions
            .assertThat(activeTestJobs)
            .hasSameSizeAs(JobStatusHelper.ACTIVE_JOB_STATUSES);
        Assertions
            .assertThat(activePersistedJobs)
            .hasSameSizeAs(JobStatusHelper.ACTIVE_JOB_STATUSES);

        // Expect a Job for each active job status.
        for (JobStatus jobStatus : JobStatusHelper.ACTIVE_JOB_STATUSES) {
            final Job testJob = activeTestJobs.stream()
                .filter(job -> job.getStatus().equals(jobStatus))
                .findFirst()
                .orElse(null);
            final Job persistedJob = activePersistedJobs.stream()
                .filter(job -> job.getStatus().equals(jobStatus))
                .findFirst()
                .orElse(null);
            Assertions
                .assertThat(testJob)
                .isNotNull();
            Assertions
                .assertThat(persistedJob)
                .isNotNull();
            Assertions
                .assertThat(testJob)
                .isEqualToComparingOnlyGivenFields(
                    persistedJob,
                    "status", "createdBy", "triggeredBy"
                );

            // Verify all Tasks are loaded.
            Assertions
                .assertThat(testJob.getTasks())
                .isNotEmpty();
            Assertions
                .assertThat(persistedJob.getTasks())
                .isNotEmpty();
            for (Task testJobTask : testJob.getTasks()) {
                boolean isTaskFound = false;
                for (Task persistedJobTask : persistedJob.getTasks()) {
                    if (testJobTask.getId().equals(persistedJobTask.getId())) {
                        isTaskFound = true;
                    }
                }
                Assertions
                    .assertThat(isTaskFound)
                    .isTrue();
            }
            for (Task testJobTask : persistedJob.getTasks()) {
                boolean isTaskFound = false;
                for (Task persistedJobTask : testJob.getTasks()) {
                    if (testJobTask.getId().equals(persistedJobTask.getId())) {
                        isTaskFound = true;
                    }
                }
                Assertions
                    .assertThat(isTaskFound)
                    .isTrue();
            }
        }

    }

}
