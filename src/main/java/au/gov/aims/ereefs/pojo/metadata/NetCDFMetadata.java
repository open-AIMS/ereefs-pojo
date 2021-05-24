package au.gov.aims.ereefs.pojo.metadata;

import au.gov.aims.ereefs.pojo.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Specialisation of {@link Metadata} to support {@code NetCDF} files.
 *
 * @author Aaron Smith
 */
@JsonIgnoreProperties({"version", "lastDownloaded", "stacktrace", "errorMessage"})
public class NetCDFMetadata extends Metadata {

    final static public String TYPE = "NETCDF";

    // ---------- TemporalDomain ----------
    @JsonIgnoreProperties({"internalTimezone", "maxDate", "minDate", "name"})
    static public class TemporalDomain {

        protected String timezone = "Australia/Brisbane";

        public String getTimezone() {
            return timezone;
        }

        @JsonIgnore
        protected Double[] timeValuesSinceEpoch;

        public Double[] getTimeValuesSinceEpoch() {
            return this.timeValuesSinceEpoch;
        }

        /**
         * Utility method for converting {@code timeValues} to an array of {@code String} values.
         */
        public String[] getTimeValues() {
            final ZoneId zoneId = ZoneId.of(this.getTimezone());
            final int size = this.timeValuesSinceEpoch.length;
            final String[] timeValues = new String[size];
            for (int index = 0; index < size; index++) {
                final LocalDateTime localDateTime = DateTimeUtils.toDateTime(this.timeValuesSinceEpoch[index]);
                timeValues[index] = DateTimeUtils.isoDateTimeFormatter.format(localDateTime.atZone(zoneId));
            }
            return timeValues;
        }

        @JsonCreator
        public TemporalDomain(@JsonProperty("timeValues") String[] timeValues) {
            this.timeValuesSinceEpoch = new Double[timeValues.length];
            for (int index = 0; index < timeValues.length; index++) {
                String value = timeValues[index];
                final LocalDateTime timeValue = (value.endsWith("Z")) ?
                    DateTimeUtils.instantDateTimeFormatter.parse(value, LocalDateTime::from) :
                    DateTimeUtils.isoDateTimeFormatter.parse(value, LocalDateTime::from);
                this.timeValuesSinceEpoch[index] = DateTimeUtils.sinceEpochInDays(timeValue);
            }
        }

        // Convenience constructor when creating manually.
        public TemporalDomain(Double[] timeValuesSinceEpoch) {
            this.timeValuesSinceEpoch = timeValuesSinceEpoch;
        }
    }

    // ---------- Variable ----------
    @JsonIgnoreProperties({"attributes", "horizontalDomain", "parameter", "parentId", "role", "scalar", "verticalDomain"})
    static public class Variable {
        protected String id;

        public String getId() {
            return this.id;
        }

        protected TemporalDomain temporalDomain;

        public TemporalDomain getTemporalDomain() {
            return temporalDomain;
        }

        @JsonCreator
        public Variable(@JsonProperty("id") String id,
                        @JsonProperty("temporalDomain") TemporalDomain temporalDomain) {
            super();
            this.id = id;
            this.temporalDomain = temporalDomain;
        }
    }

    // ---------- Metadata ----------
    @JsonProperty
    protected String datasetId;

    public String getDatasetId() {
        return this.datasetId;
    }

    protected void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    @JsonProperty
    protected String status;

    public String getStatus() {
        return this.status;
    }

    protected void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty(required = false)
    protected String checksum;

    public String getChecksum() {
        return this.checksum;
    }

    protected void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @JsonProperty("variables")
    protected Map<String, Variable> variableMap = new TreeMap<>();

    public Map<String, Variable> getVariables() {
        return this.variableMap;
    }

    protected void setVariableMap(Map<String, Variable> map) {
        this.variableMap.clear();
        if (map != null) {
            this.variableMap.putAll(map);
        }
    }

    @JsonProperty("attributes")
    protected Map<String, String> attributesMap = new TreeMap<>();

    public Map<String, String> getAttributes() {
        return this.attributesMap;
    }

    protected void setAttributes(Map<String, String> map) {
        this.attributesMap.clear();
        if (map != null) {
            this.attributesMap.putAll(map);
        }
    }

    @JsonCreator
    public NetCDFMetadata(@JsonProperty("_id") String id) {
        super(id);
    }

    /**
     * Convenience {@code Factory} method.
     */
    static public NetCDFMetadata make(String id,
                                      String definitionId,
                                      String fileUri,
                                      Date lastModified,
                                      String datasetId,
                                      String status,
                                      String checksum,
                                      Map<String, Variable> variableMap,
                                      Map<String, String> attributesMap) {
        NetCDFMetadata metadata = new NetCDFMetadata(id);
        metadata.setDefinitionId(definitionId);
        metadata.setFileURI(fileUri);
        metadata.setLastModified(lastModified);
        metadata.setDatasetId(datasetId);
        metadata.setStatus(status);
        metadata.setChecksum(checksum);
        metadata.setVariableMap(variableMap);
        metadata.setAttributes(attributesMap);
        return metadata;
    }
}
