package au.gov.aims.ereefs.pojo.definition.product;

import au.gov.aims.ereefs.pojo.Pojo;
import au.gov.aims.ereefs.pojo.Stage;
import au.gov.aims.ereefs.pojo.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.*;

import java.util.Date;

/**
 * Extends {@link Pojo} to represent a definition of a single {@code Product}. Annotation has been
 * applied to facilitate populating an object via {@code Apache Jackson}.
 *
 * @author Aaron Smith
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = false,
    defaultImpl = ProductDefinition.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = NcAggregateProductDefinition.class, name = "ncaggregate"),
    @JsonSubTypes.Type(value = NcAnimateProductDefinition.class, name = "ncanimate"),
    @JsonSubTypes.Type(value = NcAnimateProductDefinition.class, name = "dataextraction"),
})
@JsonIgnoreProperties({"tasks"})
public class ProductDefinition extends Pojo {

    // ---------------------------------------------------------------------------------------------
    // Input
    // ---------------------------------------------------------------------------------------------

    /**
     * Abstract base class representing the definition of an input to the {@code Product}. Refer to
     * specialised sub-classes for any significance assigned to the properties defined in this
     * class.
     */
    static abstract public class Input {

        /**
         * The unique {@code id} of the input dataset.
         */
        protected String id;

        /**
         * Getter for the {@link #id} property.
         *
         * @return the value assigned to {@link #id}.
         */
        public String getId() {
            return this.id;
        }

        /**
         * Identifies the type of dataset referenced by this {@link Input}. Allowed values depend
         * on the application.
         */
        protected String type;

        /**
         * Getter method for the {@link #type} property.
         *
         * @return the value assigned to {@link #type}.
         */
        public String getType() {
            return this.type;
        }

    }

    // ---------------------------------------------------------------------------------------------
    // Filters
    // ---------------------------------------------------------------------------------------------
    static public class DateRange {

        /**
         * The date (local to the thing of interest) for the start of the date range. This is taken
         * to mean the {@code beginning} of the day specified.
         */
        protected String from;

        public String getFrom() {
            return this.from;
        }

        protected Double fromSinceEpoch;

        public Double getFromSinceEpoch() {
            return this.fromSinceEpoch;
        }

        /**
         * The date (local to the thing of interest) for the end of the date range. This is taken
         * to mean the {@code end} of the day specified (which also means the beginning of the
         * following day.
         */
        protected String to;

        public String getTo() {
            return this.to;
        }

        /**
         * See notes on {@link #to} about this being the {@code end} of the date specified.
         */
        protected Double toSinceEpoch;

        public Double getToSinceEpoch() {
            return this.toSinceEpoch;
        }

        @JsonCreator
        public DateRange(@JsonProperty("from") String from,
                         @JsonProperty("to") String to) {
            this.from = from;
            this.to = to;

            this.fromSinceEpoch = DateTimeUtils.sinceEpochInDays(
                DateTimeUtils.parseAsDate(from)
            );
            this.toSinceEpoch = DateTimeUtils.sinceEpochInDays(
                DateTimeUtils.parseAsDate(to).plusDays(1)
            );
        }
    }

    static public class Filters {
        protected DateRange[] dateRanges;

        public DateRange[] getDateRanges() {
            return this.dateRanges;
        }

        @JsonCreator
        public Filters(@JsonProperty("dateRanges") DateRange[] dateRanges) {
            this.dateRanges = dateRanges;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Definition
    // ---------------------------------------------------------------------------------------------
    @JsonProperty("enabled")
    protected boolean isEnabled;

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean flag) {
        this.isEnabled = flag;
    }

    protected Date lastModified;

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * The lifecycle {@code Stage} of the {@code Product}. The default value is
     * {@link Stage#OPERATIONAL}, which means that {@code Product} should be updated/generated
     * automatically. A value of {@link Stage#PROTOTYPE} means that the {@code Product} should only
     * be updated/generated manually. A value of {@link Stage#DISCONTINUED} means the
     * {@code Product} should never be updated/generated.
     */
    protected Stage stage = Stage.OPERATIONAL;

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @JsonProperty(value = "filters", required = false)
    protected Filters filters;

    public Filters getFilters() {
        return this.filters;
    }

    protected void setFilters(Filters filters) {
        this.filters = filters;
    }

    @JsonCreator
    public ProductDefinition(@JsonProperty("_id") String id) {
        super(id);
    }

    /**
     * Convenience constructor.
     */
    public ProductDefinition(String id,
                             boolean isEnabled,
                             Date lastModified) {
        super(id);
        this.isEnabled = isEnabled;
        this.lastModified = lastModified;
    }

    /**
     * Convenience constructor.
     */
    public ProductDefinition(String id,
                             boolean isEnabled,
                             Date lastModified,
                             String targetTimeZone) {
        super(id);
        this.isEnabled = isEnabled;
        this.lastModified = lastModified;
    }
}
