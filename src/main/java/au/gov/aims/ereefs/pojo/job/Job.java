package au.gov.aims.ereefs.pojo.job;

import au.gov.aims.ereefs.pojo.Pojo;
import au.gov.aims.ereefs.pojo.shared.StatusEvent;
import au.gov.aims.ereefs.pojo.task.Task;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A POJO that represents a single {@code Job} to be performed by the system. A {@code Job} is a
 * co-ordinating concept that is made up of multiple {@link Task}s that define the individual
 * pieces of work to be performed.
 *
 * @author Aaron Smith
 */
@JsonIgnoreProperties({"tasks"})
public class Job extends Pojo {

    final static protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * The {@link JobStatus status} of the {@code Job}.
     */
    @JsonProperty
    protected JobStatus status;

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status,
                          String actor,
                          String optionalDetails) {
        this.status = status;
        this.createdBy = actor;
        String message = "Status changed to " + status.name() + " by " + actor;
        if (optionalDetails != null && optionalDetails.length() > 0) {
            message += ". " + optionalDetails;
        }
        this.history.add(
            new StatusEvent(
                dateTimeFormatter.format(ZonedDateTime.now()),
                status.name(),
                message
            )
        );
    }

    /**
     * Text-based name of the system component/actor that created the {@link Job}.
     */
    @JsonProperty
    protected String createdBy;

    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Text-based description of the system component/actor that caused the {@link Job} to be
     * created.
     */
    @JsonProperty
    protected String triggeredBy;

    public String getTriggeredBy() {
        return this.triggeredBy;
    }

    /**
     * The list of {@link Task}s that define the individual pieces of work to be performed for this
     * {@code Job}. This is marked as {@code JsonIgnore} so {@code Jackson} does not attempt to
     * use this field when de/serialising a {@link Job}. This is for convenience access only, and
     * is populated manually by the {@link au.gov.aims.ereefs.pojo.task.TaskDao} implementation.
     */
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void addTasks(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(this.tasks);
    }

    /**
     * The list of {@link StatusEvent} objects that track {@link #status} changes to the {@link Job}.
     */
    @JsonProperty
    protected List<StatusEvent> history = new ArrayList<>();

    public List<StatusEvent> getHistory() {
        return history;
    }

    protected void setHistory(List<StatusEvent> history) {
        this.history = history;
    }

    @JsonCreator
    public Job(@JsonProperty("_id") String id) {
        super(id);
    }

    /**
     * Factory method to instantiate a {@link Job} and set the {@link #status} to
     * {@link JobStatus#CREATED} and add a corresponding {@link #history status change event}.
     *
     * @param createdBy the system component/actor creating the {@link Job}.
     * @param triggeredBy the system component/actor that caused the {@link Job} to be created.
     */
    static public Job make(String createdBy,
                           String triggeredBy) {
        final Job job = new Job(null);
        job.setStatus(JobStatus.CREATED, createdBy, null);
        job.setHistory(new ArrayList<StatusEvent>() {{
                           add(
                               new StatusEvent(
                                   dateTimeFormatter.format(ZonedDateTime.now()),
                                   JobStatus.CREATED.name(),
                                   "Job created by " + createdBy
                               )
                           );
                       }}
        );
        job.triggeredBy = triggeredBy;
        return job;
    }

}
