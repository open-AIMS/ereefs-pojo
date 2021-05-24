package au.gov.aims.ereefs.pojo.job;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for populating the relevant table(s) with test data before tests are executed.
 */
public class TestDataPopulator {

    protected JobDao jobDao;

    public TestDataPopulator(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    public List<Job> populate() {
        final List<Job> jobList = new ArrayList<>();

        // Add all active Jobs.
        for (JobStatus activeJobStatus : JobStatusHelper.ACTIVE_JOB_STATUSES) {
            jobList.add(this.jobDao.persist(JobBuilder.buildJob(activeJobStatus)));
        }

        // Add all inactive Jobs.
        for (JobStatus inactiveJobStatus : JobStatusHelper.INACTIVE_JOB_STATUSES) {
            jobList.add(this.jobDao.persist(JobBuilder.buildJob(inactiveJobStatus)));
        }

        return jobList;
    }

}
