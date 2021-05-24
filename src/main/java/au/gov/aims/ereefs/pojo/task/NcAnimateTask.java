package au.gov.aims.ereefs.pojo.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Specialisation of the {@link Task} base class to define a single {@code Task} for the
 * {@code ncAnimate} functionality.
 *
 * @author Aaron Smith
 */
public class NcAnimateTask extends Task {

    protected String region;

    public String getRegion() {
        return this.region;
    }

    /**
     * Constructor, primarily for use by Jackson to populate {@link Task} from the repository.
     */
    @JsonCreator
    public NcAnimateTask(@JsonProperty("_id") String id) {
        super(id);
    }

    /**
     * Convenience constructor.
     */
    public NcAnimateTask(String jobId,
                         String productDefinitionId,
                         String region) {
        super(jobId, productDefinitionId);
        this.region = region;
    }

}
