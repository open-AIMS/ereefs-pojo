package au.gov.aims.ereefs.pojo.task;

/**
 * Enumeration of the supported {@link Task#status} values.
 *
 * @author Aaron Smith
 */
public enum TaskStatus {

    /**
     * {@link Task} has been created by the {@code JobPlanner}. Status can only be set by
     * {@code JobPlanner}. Next acceptable statuses are: {@link #ASSIGNED} or
     * {@link #TERMINATED}.
     */
    CREATED,

    /**
     * {@link Task} has been assigned to a {@code TaskHandler} (eg: {@code ncAggregate} or
     * {@code ncAnimate}) by the {@code JobScheduler}. This is normally a temporary status while
     * the {@code TaskHandler} is starting. Status can only be set by {@code JobScheduler}. Next
     * acceptable statuses are: {@link #RUNNING} or {@link #TERMINATING}.
     */
    ASSIGNED,

    /**
     * {@link Task} is currently being executed by a {@code TaskHandler}. Status can only be set by
     * {@code TaskHandler}. Next acceptable statuses are: {@link #SUCCESS}, {@link #FAILED}, or
     * {@link #TERMINATING}.
     */
    RUNNING,

    /**
     * {@link Task} has completed successfully. This is a final status. Status can only be set by
     * {@code TaskHandler}.
     */
    SUCCESS,

    /**
     * {@link Task} has been completed unsuccessfully. This is a final status. Status can only be
     * set by {@code TaskHandler}.
     */
    FAILED,

    /**
     * {@link Task} has been terminated/cancelled, but is still being executed by a
     * {@code TaskHandler}. Status can only be set by {@code JobTerminator}. Next acceptable status
     * is {@link #TERMINATED}.
     */
    TERMINATING,

    /**
     * {@link Task} has terminated and is no longer executing. This is a final status. Status can
     * only be set by {@code JobTerminator}.
     */
    TERMINATED,

    /**
     * The {@code Job} for this {@link Task} has not been approved. This is a final status. Status can only be
     * set by {@code TaskHandler}.
     */
    JOB_APPROVAL_DENIED,

    /**
     * {@link Task} has been terminated/cancelled but can retry to finish its processing (property "retries" > 0).
     * Status can only be set by {@code TaskHandlerController}. Next acceptable states are: {@link #ASSIGNED},
     * {@link #TERMINATED} or {@link #FAILED}.
     */
    RETRYING;

}
