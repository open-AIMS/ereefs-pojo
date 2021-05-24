package au.gov.aims.ereefs.pojo.extractionrequest;

import au.gov.aims.ereefs.pojo.Pojo;
import au.gov.aims.ereefs.pojo.utils.DateTimeConstants;
import au.gov.aims.ereefs.pojo.utils.LocalDateTimeDeserializer;
import au.gov.aims.ereefs.pojo.utils.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;

/**
 * A POJO representing a request by an end user to extract data from the system.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractionRequest extends Pojo {

    // ---------------------------------------------------------------------------------------------
    // Sites
    // ---------------------------------------------------------------------------------------------

    /**
     * Object that represents a single site within the extraction request.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class Site {

        /**
         * Human-readable name for the site.
         */
        protected String name;

        /**
         * Latitude of the site.
         */
        @JsonProperty("lat")
        protected String latitude;

        /**
         * Longitude of the site.
         */
        @JsonProperty("lon")
        protected String longitude;

        /**
         * Public creator required by Jackson library.
         */
        @JsonCreator
        public Site() {
        }

        protected Site(String name,
                       String latitude,
                       String longitude) {
            this();
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getName() {
            return this.name;
        }

        public String getLatitude() {
            return this.latitude;
        }

        public String getLongitude() {
            return this.longitude;
        }

        static public Site make(String name,
                                String latitude,
                                String longitude) {
            return new Site(name, latitude, longitude);
        }

    }


    /**
     * List of sites to extract data for.
     */
    protected Site[] sites;

    public Site[] getSites() {
        return this.sites;
    }

    /**
     * The ISO date/time the {@link ExtractionRequest} was created.
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    protected LocalDateTime createdAt = LocalDateTime.now(DateTimeConstants.DEFAULT_TIME_ZONE_ID);

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * The ISO date/time the {@link ExtractionRequest} was last updated at.
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    protected LocalDateTime updatedAt = LocalDateTime.now(DateTimeConstants.DEFAULT_TIME_ZONE_ID);

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Protected method invoked by {@link ExtractionRequestDao#persist(Object)} implementations to
     * record that an update has occurred.
     */
    protected void markAsUpdated() {
        this.updatedAt = LocalDateTime.now(DateTimeConstants.DEFAULT_TIME_ZONE_ID);
    }

    /**
     * The unique identifier of the input datasource.
     */
    protected String dataCollection;

    public String getDataCollection() {
        return this.dataCollection;
    }

    /**
     * The list of variables to extract data for.
     */
    protected String[] variables;

    public String[] getVariables() {
        return this.variables;
    }

    /**
     * The start of the date range.
     */
    protected String dateRangeFrom;

    public String getDateRangeFrom() {
        return this.dateRangeFrom;
    }

    /**
     * The end of the date range.
     */
    protected String dateRangeTo;

    public String getDateRangeTo() {
        return this.dateRangeTo;
    }

    /**
     * The time step of the output.
     */
    @JsonProperty("timeStep")
    protected String outputTimeStep;

    public String getOutputTimeStep() {
        return this.outputTimeStep;
    }

    /**
     * The list of depths to extract data for.
     */
    protected double[] depths;

    public double[] getDepths() {
        return this.depths;
    }

    /**
     * The execution context.
     */
    protected ObjectNode executionContext = new ObjectNode(new JsonNodeFactory(false));

    public ObjectNode getExecutionContext() {
        return this.executionContext;
    }

    /**
     * Constructor to capture the parameter(s).
     *
     * @param id the unique identifier to use for this {@code Pojo}. If {@code null}, a unique
     *           {@code id} will be generated by concatenating the object type
     *           ({@code this.getClass().getSimpleName()} with a {@code UUID}.
     */
    @JsonCreator
    public ExtractionRequest(@JsonProperty("_id") String id) {
        super(id);
    }

    protected ExtractionRequest(String id,
                                Site[] sites,
                                String dataCollection,
                                String[] variables,
                                String dateRangeFrom,
                                String dateRangeTo,
                                String outputTimeStep,
                                double[] depths) {
        this(id);
        this.sites = sites;
        this.dataCollection = dataCollection;
        this.variables = variables;
        this.dateRangeFrom = dateRangeFrom;
        this.dateRangeTo = dateRangeTo;
        this.outputTimeStep = outputTimeStep;
        this.depths = depths;
    }

    /**
     * {@code Factory} method for instantiating a complete {@link ExtractionRequest}.
     */
    static public ExtractionRequest make(String id,
                                         Site[] sites,
                                         String dataCollection,
                                         String[] variables,
                                         String dateRangeFrom,
                                         String dateRangeTo,
                                         String outputTimeStep,
                                         double[] depths) {
        return new ExtractionRequest(
            id,
            sites,
            dataCollection,
            variables,
            dateRangeFrom,
            dateRangeTo,
            outputTimeStep,
            depths
        );
    }

}