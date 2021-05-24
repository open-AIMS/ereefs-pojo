package au.gov.aims.ereefs.pojo.job;

/**
 * Enumeration of the supported {@link Job#status} values.
 *
 * @author Aaron Smith
 */
public enum JobStatus {

    /**
     * {@link Job} has been created by the {@code JobPlanner}. Status can only be set by
     * {@code JobPlanner}. Next acceptable statuses are: {@link #AWAITING_APPROVAL},
     * {@link #APPROVED}, or {@link #TERMINATING}.
     */
    CREATED,

    /**
     * {@link Job} has been identified by the {@code JobApprover} as requiring manual approval, but
     * approval has not yet been received. Status can only be set by {@code JobApprover}. Next
     * acceptable statuses are: {@link #APPROVED}, {@link #APPROVAL_DENIED} or {@link #TERMINATING}.
     */
    AWAITING_APPROVAL,

    /**
     * {@link Job} has either been approved automatically by the {@code JobApprover} (effort <
     * threshold), or manual approval has been granted. Status can only be set by
     * {@code JobApprover}. Next acceptable statuses are: {@link #RUNNING} or {@link #TERMINATING}.
     */
    APPROVED,

    /**
     * {@link Job} has not been approved. This is a final status. Status can only be set by
     * {@code JobApprover}.
     */
    APPROVAL_DENIED,

    /**
     * {@link Job} is currently being executed by the {@code JobScheduler}. Status can only be set
     * by {@code JobScheduler}. Next acceptable statuses are: {@link #COMPLETED}, {@link #FAILED}
     * or {@link #TERMINATING}.
     */
    RUNNING,

    /**
     * {@link Job} has successfully completed running. This is a final status. Status can only be
     * set by {@code JobScheduler}.
     */
    COMPLETED,

    /**
     * {@link Job} has unsuccessfully completed running. This is a final status. Status can only be
     * set by {@code JobScheduler}.
     */
    FAILED,

    /**
     * {@link Job} has been terminated/cancelled, but one or more {@code Tasks} are still executing.
     * Status can only be set by {@code JobTerminator}. Next acceptable status is
     * {@link #TERMINATED}.
     */
    TERMINATING,

    /**
     * {@link Job} was terminated and no {@code Tasks} are executing anymore. This is a final
     * status. Status can only be set by {@code JobTerminator}.
     */
    TERMINATED;

}
