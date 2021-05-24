package au.gov.aims.ereefs.pojo.job;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for interacting with {@link JobStatus}s.
 *
 * @author Aaron Smith
 */
public class JobStatusHelper {

    final static public List<JobStatus> ACTIVE_JOB_STATUSES = new ArrayList<JobStatus>() {{
        add(JobStatus.CREATED);
        add(JobStatus.AWAITING_APPROVAL);
        add(JobStatus.APPROVED);
        add(JobStatus.RUNNING);
    }};

    final static public List<JobStatus> INACTIVE_JOB_STATUSES = new ArrayList<JobStatus>() {{
        add(JobStatus.APPROVAL_DENIED);
        add(JobStatus.COMPLETED);
        add(JobStatus.FAILED);
        add(JobStatus.TERMINATING);
        add(JobStatus.TERMINATED);
    }};

}
