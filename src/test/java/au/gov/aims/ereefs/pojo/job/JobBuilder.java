package au.gov.aims.ereefs.pojo.job;

import au.gov.aims.ereefs.pojo.task.NcAggregateTask;
import au.gov.aims.ereefs.pojo.task.TaskBuilder;

import java.util.UUID;

/**
 * Utility class for building a {@link Job} for testing.
 */
public class JobBuilder {

    /**
     * Helper method to instantiate a random new {@link Job} for testing.
     */
    static public Job buildJob(JobStatus jobStatus) {

        // Instantiate a Job.
        final Job job = Job.make("pojoTest", "OPERATIONAL");
        if (!jobStatus.equals(job.getStatus())) {
            job.setStatus(
                jobStatus,
                "pojoTest",
                "Set different status for testing."
            );
        }

        // Build some Tasks.
        final String productDefintionId = UUID.randomUUID().toString();
        NcAggregateTask task1 = TaskBuilder.buildAggregationTask(job, productDefintionId);
        job.addTask(task1);

        NcAggregateTask task2 = TaskBuilder.buildAggregationTask(job, productDefintionId);
        task2.addDependOnId(task1.getId());
        job.addTask(task2);

        return job;

    }

}
