package au.gov.aims.ereefs.pojo.shared;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * A supporting POJO that records a single status change event for {@code Job}s and {@code Task}s.
 *
 * @author Aaron Smith
 */
public class StatusEvent {

    protected String timestamp;
    public String getTimestamp() {
        return timestamp;
    }

    protected String status;
    public String getStatus() {
        return status;
    }

    protected String description;
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public StatusEvent() {
    }

    /**
     * Convenience constructor.
     */
    public StatusEvent(String timestamp,
                       String status,
                       String description) {
        this.timestamp = timestamp;
        this.status = status;
        this.description = description;
    }

}
