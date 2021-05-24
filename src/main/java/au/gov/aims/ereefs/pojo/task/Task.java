package au.gov.aims.ereefs.pojo.task;

import au.gov.aims.ereefs.pojo.Pojo;
import au.gov.aims.ereefs.pojo.Stage;
import au.gov.aims.ereefs.pojo.shared.StatusEvent;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for POJOs that represent a single {@code Task} to be performed by the system. A
 * {@code Task} is a single piece of work, normally to be performed by a single system component
 * (eg: {@code ncAggregate} or {@code ncAnimate}), and normally generates a single result
 * (eg: a single output file). This concept may change depending on the specialised {@code Task}.
 *
 * @author Aaron Smith
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = false,
    defaultImpl = Task.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Task.class, name = "basic"),
    @JsonSubTypes.Type(value = NcAggregateTask.class, name = "ncaggregate"),
    @JsonSubTypes.Type(value = NcAnimateTask.class, name = "ncanimate"),
    @JsonSubTypes.Type(value = CollectorTask.class, name = "collector"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task extends Pojo {

    final static protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * The {@link Task#getId() id}s of {@code Task}s that must be completed before this
     * {@code Task} can be started. Note that {@link #getDependsOnIds()} returns a new list, so
     * modifications to that list are ignored.
     */
    @JsonProperty("dependsOn")
    private List<String> dependsOnIds = new ArrayList<>();

    public void addDependOnId(String id) {
        this.dependsOnIds.add(id);
    }

    public List<String> getDependsOnIds() {
        return new ArrayList<>(dependsOnIds);
    }

    /**
     * The {@code id} of the {@code Job} that this {@code Task} belongs to.
     */
    @JsonProperty
    protected String jobId;

    public String getJobId() {
        return this.jobId;
    }

    /**
     * The {@code id} of the {@code Product} that this {@code Task} is generating, allowing the
     * executing service
     */
    @JsonProperty
    protected String productDefinitionId;

    public String getProductDefinitionId() {
        return this.productDefinitionId;
    }

    /**
     * The {@link TaskStatus status} of the {@code Task}.
     */
    @JsonProperty
    protected TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus newStatus,
                          String actor,
                          String optionalDetails) {
        this.status = newStatus;
        String message = "Status changed to " + newStatus.name() + " by " + actor;
        if (optionalDetails != null && optionalDetails.length() > 0) {
            message += ". " + optionalDetails;
        }
        this.history.add(
            new StatusEvent(
                dateTimeFormatter.format(ZonedDateTime.now()),
                newStatus.name(),
                message
            )
        );
    }

    /**
     * The list of {@link StatusEvent} objects that track {@link #status} changes to the {@link Task}.
     */
    @JsonProperty
    protected List<StatusEvent> history;

    public List<StatusEvent> getHistory() {
        return history;
    }

    /**
     * The lifecycle {@code Stage} of the {@code Product} that the {@code Task} relates to. This
     * value may be of use when scheduling {@code Task} execution.
     */
    protected Stage stage = Stage.OPERATIONAL;

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * JSON node containing information relating to the execution of the {@link Task}. This library
     * does not provide specialised access to the properties within this node as access has not
     * thus far been required. However the node is referenced here to support updates to the
     * {@link Task} without overwriting the contents of this node.
     */
    protected ObjectNode executionContext = new ObjectNode(new JsonNodeFactory(false));

    public ObjectNode getExecutionContext() {
        return this.executionContext;
    }

    /**
     * Constructor, primarily for use by Jackson to populate {@link Task} from the repository.
     *
     * @param id the unique {@code id} to use for the {@link Task}. If {@code null}, a unique
     *           {@code id} will be generated.
     */
    @JsonCreator
    public Task(@JsonProperty("_id") String id) {
        super(id);
    }

    /**
     * Convenience constructor, primarily for use by the application. This constructor sets the
     * {@link #status} to {@link TaskStatus#CREATED} and adds a corresponding
     * {@link #history status change event}.
     *
     * @param jobId               the unique identifier for the {@code Job} that this {@link Task}
     *                            belongs to.
     * @param productDefinitionId the unique identifier for the {@code Product} that this
     *                            {@link Task} is referencing.
     */
    public Task(String jobId, String productDefinitionId) {
        this(null);
        this.jobId = jobId;
        this.productDefinitionId = productDefinitionId;
        this.status = TaskStatus.CREATED;
        this.history = new ArrayList<StatusEvent>() {{
            add(
                new StatusEvent(
                    dateTimeFormatter.format(ZonedDateTime.now()),
                    TaskStatus.CREATED.name(),
                    "Task created."
                )
            );
        }};
    }

}
