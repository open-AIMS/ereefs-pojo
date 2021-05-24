package au.gov.aims.ereefs.pojo.definition.product;

import au.gov.aims.ereefs.pojo.utils.PreProcessingTaskDefnDeserializer;
import au.gov.aims.ereefs.pojo.utils.PreProcessingTaskDefnSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Aggregation-specific specialisation of the {@link ProductDefinition} class.
 */
public class NcAggregateProductDefinition extends ProductDefinition {


    // ---------------------------------------------------------------------------------------------
    // Inputs
    // ---------------------------------------------------------------------------------------------

    /**
     * Abstract base class extending {@link ProductDefinition.Input} to define sub-types for use by
     * the Jackson framework for serialisation without polluting the {@link ProductDefinition}
     * class.
     */
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = NetCDFInput.class, name = "netcdf")
    })
    abstract static public class Input extends ProductDefinition.Input {
    }


    /**
     * Specialisation of the {@link Input} class for {@code NetCDF} input files. These files
     * normally represent an input dataset, or stream of input datasets, created by:
     *
     * <ul>
     *     <li>{@link ProductDefinition}</li> - the {@link #id} property must match the
     *     corresponding {@link ProductDefinition#id}.
     *     <li>{@link au.gov.aims.ereefs.pojo.definition.download.DownloadDefinition} - the
     *     {@link #id} property must match the corresponding
     *     {@link au.gov.aims.ereefs.pojo.definition.download.DownloadDefinition#id}.</li>
     * </ul>
     */
    static public class NetCDFInput extends Input {

        /**
         * The duration of the file. eg: {@code Monthly} or {@code Daily}.
         */
        protected String fileDuration;

        /**
         * Getter for the {@link #fileDuration} property.
         *
         * @return the value assigned to {@link #fileDuration}.
         */
        public String getFileDuration() {
            return this.fileDuration;
        }

        /**
         * The length of each time increment for the input file. Supported values are:
         * {@code hourly}, {@code daily}, {@code monthly}, and {@code yearly}.
         */
        protected String timeIncrement;

        /**
         * Getter for the {@link #timeIncrement} property.
         *
         * @return the value assigned to {@link #timeIncrement}.
         */
        public String getTimeIncrement() {
            return this.timeIncrement;
        }

        /**
         * Flag that determines if only complete files will be processed. For example, a file that
         * is meant to contain all of the data for a month, instead only contains 20 days of data.
         * If this flag is set ({@code true}), that file will be ignored.
         */
        protected boolean completeFilesOnly;

        /**
         * Getter for the {@link #completeFilesOnly} property.
         *
         * @return the value assigned to {@link #completeFilesOnly}.
         */
        public boolean isCompleteFilesOnly() {
            return this.completeFilesOnly;
        }

        /**
         * The names of the variables that can be processed from this input.
         */
        protected String[] variables;

        /**
         * Getter for the {@link #variables} property.
         *
         * @return the value assigned to {@link #variables}.
         */
        public String[] getVariables() {
            return this.variables;
        }

        /**
         * Constructor with no parameters for use by Jackson framework.
         */
        public NetCDFInput() {
        }

        /**
         * Private constructor for use by {@link #make(String, String, String, String, boolean, String[])}
         */
        private NetCDFInput(String id,
                            String type,
                            String timeIncrement,
                            String fileDuration,
                            boolean completeFilesOnly,
                            String[] variables) {
            this.id = id;
            this.type = type;
            this.timeIncrement = timeIncrement;
            this.fileDuration = fileDuration;
            this.completeFilesOnly = completeFilesOnly;
            this.variables = variables;
        }

        /**
         * Convenience factory method.
         */
        static public NetCDFInput make(String id,
                                       String type,
                                       String timeIncrement,
                                       String fileDuration,
                                       boolean completeFilesOnly,
                                       String[] variables) {
            return new NetCDFInput(
                id,
                type,
                timeIncrement,
                fileDuration,
                completeFilesOnly,
                variables
            );
        }

    }

    // ---------------------------------------------------------------------------------------------
    // StaticFiles
    // ---------------------------------------------------------------------------------------------

    /**
     * Base class representing a static file used by the system. Once loaded, this file can be
     * referenced via the {@link #id} property.
     */
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(name = "csv", value = CsvFile.class),
        @JsonSubTypes.Type(name = "geojson", value = StaticFile.class),
        @JsonSubTypes.Type(name = "regulargridmapper", value = RegularGridMapperFile.class)
    })
    static public class StaticFile {

        final static public String CSV = "csv";
        final static public String GEOJSON = "geojson";
        final static public String REGULAR_GRID_MAPPER = "regulargridmapper";

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
         * Identifies the type of file, allowing for file-type specific processing within the
         * application.
         *
         * @see #CSV
         * @see #GEOJSON
         * @see #REGULAR_GRID_MAPPER
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

        /**
         * The {@code url} of the static file.
         */
        protected String url;

        /**
         * Getter method for the {@link #url} property.
         *
         * @return the value assigned to the {@link #url}.
         */
        public String getUrl() {
            return this.url;
        }

    }

    /**
     * Specialisation of {@link StaticFile} to support CSV files.
     */
    static public class CsvFile extends StaticFile {

        /**
         * The list of field names of interest in the {@code CSV} file.
         */
        protected String[] fieldNames;

        public String[] getFieldNames() {
            return this.fieldNames;
        }

    }


    /**
     * Specialisation of {@link StaticFile} to support RegularGridMappers.
     */
    static public class RegularGridMapperFile extends StaticFile {

        /**
         * The resolution to use when building a {@code RegularGridMapper} from an input dataset,
         * which occurs if the file referenced by {@link #url} does not exist.
         */
        protected double resolution;

        /**
         * Getter for the {@link #resolution} property.
         *
         * @return the value assigned to {@link #resolution}.
         */
        public double getResolution() {
            return resolution;
        }

    }

    // ---------------------------------------------------------------------------------------------
    // PreProcessingTasks
    // ---------------------------------------------------------------------------------------------

    /**
     * The actual format of a {@code PreProcessingTask} is dependent on the application it is being
     * used with, and is therefore kept as a JSON object for interpretation by the application.
     */
    @JsonSerialize(using = PreProcessingTaskDefnSerializer.class)
    @JsonDeserialize(using = PreProcessingTaskDefnDeserializer.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class PreProcessingTaskDefn {

        /**
         * The raw underlying JSON
         */
        protected JsonNode json = null;

        public JsonNode getJson() {
            return this.json;
        }

        public PreProcessingTaskDefn(JsonNode json) {
            this.json = json;
        }

        /**
         * Getter method that returns the {@code Type} of the {@code PreProcessingTaskDefn}. This
         * value has relevance for the executing application only.
         */
        @JsonIgnore
        public String getType() {
            return (
                this.json == null ?
                    "unspecified" :
                    json.get("type").asText()
            );
        }

    }


    // ---------------------------------------------------------------------------------------------
    // Outputs
    // ---------------------------------------------------------------------------------------------

    /**
     * Enumerator identifying the {@link OutputFile} {@code Types} supported.
     */
    static public enum OutputFileType {

        /**
         * Indicates that the {@link OutputFile} references a {@code NetCDF} file.
         */
        @JsonProperty("netcdf")
        NETCDF,

        /**
         * Indicates that the {@link OutputFile} references a {@code Summary} file based on Zones.
         */
        @JsonProperty("zone_summary")
        ZONE_SUMMARY,

        /**
         * Indicates that the {@link OutputFile} references a {@code Summary} file based on Sites.
         */
        @JsonProperty("site_summary")
        SITE_SUMMARY;

    }

    /**
     * Represents a single type of output file generated by the system.
     */
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(name = "netcdf", value = NetcdfOutputFile.class),
        @JsonSubTypes.Type(name = "zone_summary", value = ZoneBasedSummaryOutputFile.class),
        @JsonSubTypes.Type(name = "site_summary", value = SimpleDateFormat.class),
    })
    static abstract public class OutputFile {

        protected OutputFileType type;

        public OutputFileType getType() {
            return this.type;
        }

        public OutputFile() {
        }

        public OutputFile(OutputFileType type) {
            this.type = type;
        }

    }

    /**
     * {@link OutputFile} specialisation to define {@code NetCDF} file generation.
     */
    static public class NetcdfOutputFile extends OutputFile {

        /**
         * The name to which the corresponding {@code RegularGridMapper} is bound to the cache.
         * Binding is normally performed by a {@link PreProcessingTaskDefn}.
         */
        protected String regularGridMapperCacheBindName;

        public String getRegularGridMapperCacheBindName() {
            return this.regularGridMapperCacheBindName;
        }

        /**
         * Map of attributes to add to the output. These values will override any values copied from
         * the input datasets. If the value is blank, the attribute will not appear in the output
         * dataset.
         */
        @JsonProperty(value = "globalAttributes", required = false)
        protected Map<String, String> globalAttributes = null;

        /**
         * Getter method for {@link #globalAttributes} map.
         *
         * @return the references assigned to the {@link #globalAttributes} map.
         */
        public Map<String, String> getGlobalAttributes() {
            return this.globalAttributes;
        }

        public NetcdfOutputFile() {
            super();
        }

        public NetcdfOutputFile(OutputFileType type,
                                String regularGridMapperCacheBindName,
                                Map<String, String> globalAttributes) {
            super(type);
            this.regularGridMapperCacheBindName = regularGridMapperCacheBindName;
            this.globalAttributes = globalAttributes;
        }

    }

    /**
     * {@link OutputFile} specialisation to define {@code Zone}-based {@code Summary} file
     * generation.
     */
    static public class ZoneBasedSummaryOutputFile extends OutputFile {

        /**
         * The name used to retrieve a {@code CsvDataset}, which contains the list of all
         * {@code ZoneNames}, from the system cache.
         */
        protected String zoneNamesBindName;

        public String getZoneNamesBindName() {
            return this.zoneNamesBindName;
        }

        /**
         * The name used to retrieve a {@code Map}, which links pixel index to {@code Zone}, from
         * the system cache.
         */
        protected String indexToZoneIdMapBindName;

        public String getIndexToZoneIdMapBindName() {
            return this.indexToZoneIdMapBindName;
        }

        @JsonCreator
        public ZoneBasedSummaryOutputFile() {
            super();
            this.type = OutputFileType.ZONE_SUMMARY;
        }

        public ZoneBasedSummaryOutputFile(String zoneNamesBindName,
                                          String indexToZoneIdMapBindName) {
            this();
            this.zoneNamesBindName = zoneNamesBindName;
            this.indexToZoneIdMapBindName = indexToZoneIdMapBindName;
        }

    }

    /**
     * {@link OutputFile} specialisation to define {@code Site}-based {@code Summary} file
     * generation.
     */
    static public class SiteBasedSummaryOutputFile extends OutputFile {

        @JsonCreator
        public SiteBasedSummaryOutputFile() {
            super();
            this.type = OutputFileType.SITE_SUMMARY;
        }

    }

    /**
     * Enumerator identifying the {@link Outputs#strategy strategies} supported.
     */
    static public enum OutputsStrategy {

        /**
         * A single file will be created.
         */
        @JsonProperty("one")
        ONE,

        /**
         * A file will be created for each data of data.
         */
        @JsonProperty("daily")
        DAILY,

        /**
         * A file will be created for each month of data.
         */
        @JsonProperty("monthly")
        MONTHLY,

        /**
         * A file will be created for each year of data.
         */
        @JsonProperty("annual")
        ANNUAL,

        /**
         * A file will be created for each season of data.
         */
        @JsonProperty("seasonal")
        SEASONAL,

    }

    /**
     * Class containing properties defining the outputs for the {@code Product}.
     */
    static public class Outputs {

        protected OutputsStrategy strategy;

        public OutputsStrategy getStrategy() {
            return this.strategy;
        }

        /**
         * The {@code url} that acts as a template for the location and name of the output file,
         * to which a prefix can be added (eg: {@code .nc} or {@code -summary.csv}).
         */
        protected String baseUrl;

        public String getBaseUrl() {
            return this.baseUrl;
        }

        protected boolean completeFilesOnly;

        public boolean isCompleteFilesOnly() {
            return this.completeFilesOnly;
        }

        @JsonProperty("files")
        protected List<OutputFile> outputFiles;

        public List<OutputFile> getOutputFiles() {
            return this.outputFiles;
        }

        /**
         * Convenience method for retrieving the first {@link NetcdfOutputFile} defined.
         */
        @JsonIgnore
        public NetcdfOutputFile getNetcdfOutputFile() {
            for (OutputFile outputFile : this.outputFiles) {
                if (outputFile instanceof NetcdfOutputFile) {
                    return (NetcdfOutputFile) outputFile;
                }
            }
            return null;
        }

        /**
         * Convenience method for retrieving the first {@link ZoneBasedSummaryOutputFile} defined.
         */
        @JsonIgnore
        public ZoneBasedSummaryOutputFile getZoneBasedSummaryOutputFile() {
            for (OutputFile outputFile : this.outputFiles) {
                if (outputFile instanceof ZoneBasedSummaryOutputFile) {
                    return (ZoneBasedSummaryOutputFile) outputFile;
                }
            }
            return null;
        }

        /**
         * Convenience method for retrieving the first {@link SiteBasedSummaryOutputFile} defined.
         */
        @JsonIgnore
        public SiteBasedSummaryOutputFile getSiteBasedSummaryOutputFile() {
            for (OutputFile outputFile : this.outputFiles) {
                if (outputFile instanceof SiteBasedSummaryOutputFile) {
                    return (SiteBasedSummaryOutputFile) outputFile;
                }
            }
            return null;
        }

        /**
         * Constructor with no parameters for use by {@code Jackson} framework.
         */
        @JsonCreator
        public Outputs() {
        }

        public Outputs(OutputsStrategy strategy,
                       boolean completeFilesOnly,
                       String baseUrl,
                       List<OutputFile> outputFiles) {
            super();
            this.strategy = strategy;
            this.completeFilesOnly = completeFilesOnly;
            this.baseUrl = baseUrl;
            this.outputFiles = outputFiles;
        }
    }


    // ---------------------------------------------------------------------------------------------
    // Action
    // ---------------------------------------------------------------------------------------------

    /**
     * A class containing definition of an aggregation-specific {@code Action} to perform.
     */
    @JsonIgnoreProperties({"seasons", "tasks"})
    static public class Action {

        /**
         * The period over which to aggregate the data. Supported values are:
         *
         * <ul>
         * <li>daily</li>
         * <li>monthly</li>
         * <li>annual</li>
         * <li>seasonal</li>
         * <li>all</li>
         * </ul>
         */
        protected String period;

        /**
         * Getter for the {@link #period} property.
         *
         * @return the value assigned to {@link #period}.
         */
        public String getPeriod() {
            return this.period;
        }

        /**
         * An array of depth values to process.
         */
        protected double[] depths;

        /**
         * Getter for the {@link #depths} property.
         *
         * @return the value assigned to {@link #depths}.
         */
        public double[] getDepths() {
            return depths;
        }

        /**
         * An array of variable names to calculate the {@code mean} for.
         */
        protected String[] variables;

        /**
         * Getter for the {@link #variables} property.
         *
         * @return the values assigned to {@link #variables}.
         */
        public String[] getVariables() {
            return variables;
        }

        /**
         * An array of more complex operations to perform.
         */
        protected SummaryOperator[] summaryOperators;

        /**
         * Getter for the {@link #summaryOperators} property.
         *
         * @return the references assigned to {@link #summaryOperators}.
         */
        public SummaryOperator[] getSummaryOperators() {
            return summaryOperators;
        }

        /**
         * Constructor to capture the references.
         */
        @JsonCreator
        public Action(@JsonProperty("period") String period,
                      @JsonProperty("depths") double[] depths,
                      @JsonProperty("variables") String[] variables,
                      @JsonProperty("operators") SummaryOperator[] summaryOperators) {
            super();
            this.period = period;
            this.depths = depths;
            this.variables = variables;
            this.summaryOperators = summaryOperators;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Summary Operator
    // ---------------------------------------------------------------------------------------------
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "operatorType",
        visible = true,
        defaultImpl = SummaryOperator.class
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = SummaryOperator.class, name = "MEAN"),
        @JsonSubTypes.Type(value = SummaryOperator.class, name = "SPEED"),
        @JsonSubTypes.Type(value = ThresholdValueSummaryOperator.class, name = "THRESHOLD_VALUE_EXCEEDANCE_COUNT"),
        @JsonSubTypes.Type(value = ThresholdZonalSummaryOperator.class, name = "THRESHOLD_ZONAL_EXCEEDANCE_COUNT"),
        @JsonSubTypes.Type(value = ThresholdValueSummaryOperator.class, name = "THRESHOLD_VALUE_EXCEEDANCE_FREQUENCY"),
        @JsonSubTypes.Type(value = ThresholdZonalSummaryOperator.class, name = "THRESHOLD_ZONAL_EXCEEDANCE_FREQUENCY"),
        @JsonSubTypes.Type(value = ThresholdValueSummaryOperator.class, name = "THRESHOLD_VALUE_EXCEEDANCE_VALUE_ACCUMULATION"),
        @JsonSubTypes.Type(value = ThresholdZonalSummaryOperator.class, name = "THRESHOLD_ZONAL_EXCEEDANCE_VALUE_ACCUMULATION"),
        @JsonSubTypes.Type(value = ThresholdValueSummaryOperator.class, name = "THRESHOLD_VALUE_EXCEEDANCE_VALUE_SQUARED_ACCUMULATION"),
        @JsonSubTypes.Type(value = ThresholdZonalSummaryOperator.class, name = "THRESHOLD_ZONAL_EXCEEDANCE_VALUE_SQUARED_ACCUMULATION")
    })
    static public class SummaryOperator {

        /**
         * Property identifying which {@link SummaryOperator} to instantiate. Currently supported
         * values are:
         *
         * <ul>
         * <li>{@code MEAN}</li>
         * <li>{@code SPEED}</li>
         * <li>{@code THRESHOLD_VALUE_EXCEEDANCE_COUNT}</li>
         * <li>{@code THRESHOLD_ZONAL_EXCEEDANCE_COUNT}</li>
         * <li>{@code THRESHOLD_VALUE_EXCEEDANCE_FREQUENCY}</li>
         * <li>{@code THRESHOLD_ZONAL_EXCEEDANCE_FREQUENCY}</li>
         * <li>{@code THRESHOLD_VALUE_EXCEEDANCE_VALUE_ACCUMULATION}</li>
         * <li>{@code THRESHOLD_ZONAL_EXCEEDANCE_VALUE_ACCUMULATION}</li>
         * <li>{@code THRESHOLD_VALUE_EXCEEDANCE_VALUE_SQUARED_ACCUMULATION}</li>
         * <li>{@code THRESHOLD_ZONAL_EXCEEDANCE_VALUE_SQUARED_ACCUMULATION}</li>
         * </ul>
         */
        @JsonProperty("operatorType")
        protected String operatorType;

        public String getOperatorType() {
            return this.operatorType;
        }

        public void setOperatorType(String operatorType) {
            this.operatorType = operatorType;
        }

        /**
         * User-friendly name for display in the log files.
         */
        @JsonProperty("name")
        protected String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * Enable ({@code true}) / disable ({@code false}) this operator. Useful for testing when
         * operators can be disabled to save time.
         */
        @JsonProperty("enabled")
        protected boolean isEnabled = true;

        public boolean isEnabled() {
            return this.isEnabled;
        }

        /**
         * A list of {@code String}s identifying the name(s) of the input variable(s).
         */
        @JsonProperty("inputVariables")
        protected List<String> inputVariables = new ArrayList<>();

        public List<String> getInputVariables() {
            return inputVariables;
        }

        /**
         * A list of {@link OutputVariable} objects for creation and population of an output variable.
         */
        @JsonProperty("outputVariables")
        protected List<OutputVariable> outputVariables = new ArrayList<>();

        public List<OutputVariable> getOutputVariables() {
            return outputVariables;
        }

        /**
         * Override the standard {@code toString()} to provide more useful information for logging.
         *
         * @return a {@code String} description of the object.
         */
        @Override
        public String toString() {
            StringBuilder value = new StringBuilder();
            value.append("SummaryOperatorConfig{name='" + name + "'");
            value.append(", inputVariables=" + inputVariables);
            if (this.getOutputVariables().size() > 0) {
                value.append(", outputVariables=[");
                for (OutputVariable outputVariable : this.getOutputVariables()) {
                    value.append(outputVariable.getAttributes().get("short_name") + ",");
                }
                value.append("]");
            }
            value.append("}");
            return value.toString();
        }

        /**
         * Factory method to instantiate and populate a {@link SummaryOperator} for a {@code MEAN}
         * calculation.
         *
         * @param variableName the name of the variable to assign to the {@link #inputVariables}
         *                     property.
         * @return the populated {@link SummaryOperator}.
         */
        static public SummaryOperator makeMean(String variableName) {
            OutputVariable outputVariable = new OutputVariable();
            outputVariable.setAttributes(new TreeMap<String, String>() {{
                put("short_name", variableName);
            }});
            return new SummaryOperator(
                "Mean of " + variableName,
                "MEAN",
                new ArrayList<String>() {{
                    add(variableName);
                }},
                new ArrayList<OutputVariable>() {{
                    add(outputVariable);
                }}
            );
        }

        /**
         * Factory method to instantiate and populate a {@link SummaryOperator}.
         *
         * @param operatorType    the value to assign to the {@link #operatorType} property.
         * @param variableNames   the names of variables to assign to the {@link #inputVariables}
         *                        property.
         * @param outputVariables the output variables to add to the {@link #outputVariables} property.
         * @return the populated {@link SummaryOperator}.
         */
        static public SummaryOperator make(String operatorType,
                                           List<String> variableNames,
                                           List<OutputVariable> outputVariables) {
            SummaryOperator summaryOperator = new SummaryOperator(
                operatorType,
                operatorType,
                new ArrayList<String>() {{
                    addAll(variableNames);
                }},
                new ArrayList<OutputVariable>() {{
                    addAll(outputVariables);
                }}
            );
            return summaryOperator;
        }

        @JsonCreator
        public SummaryOperator() {
        }

        public SummaryOperator(String name,
                               String operatorType,
                               List<String> inputVariables,
                               List<OutputVariable> outputVariables) {
            this.operatorType = operatorType;
            this.name = name;
            this.inputVariables.addAll(inputVariables);
            this.outputVariables.addAll(outputVariables);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Threshold Summary Operator
    // ---------------------------------------------------------------------------------------------
    abstract static public class ThresholdSummaryOperator extends SummaryOperator {

        /**
         * The {@code comparator} to use when comparing a value to the {@code threshold}. This
         * field supports {@code "less"} and {@code "greater"} only.
         */
        @JsonProperty("comparator")
        protected String comparator;

        /**
         * Getter method for the {@link #comparator} property.
         *
         * @return the value assigned to the {@link #comparator} property.
         */
        public String getComparator() {
            return this.comparator;
        }

        /**
         * The number of time slices to accumulate (calculate the MEAN for) before comparing the value
         * to a {@code threshold}.
         */
        @JsonProperty("accumulationTimeSlices")
        protected int accumulationTimeSlices;

        /**
         * Getter method for the {@link #accumulationTimeSlices} property.
         *
         * @return the value assigned to the {@link #accumulationTimeSlices} property.
         */
        public int getAccumulationTimeSlices() {
            return this.accumulationTimeSlices;
        }

        public ThresholdSummaryOperator() {
        }

        public ThresholdSummaryOperator(String name,
                                        String operatorType,
                                        List<String> inputVariables,
                                        List<OutputVariable> outputVariables,
                                        String comparator,
                                        int accumulationTimeSlices) {
            super(
                name,
                operatorType,
                inputVariables,
                outputVariables
            );
            this.comparator = comparator;
            this.accumulationTimeSlices = accumulationTimeSlices;
        }
    }

    static public class ThresholdValueSummaryOperator extends ThresholdSummaryOperator {

        /**
         * The threshold value to apply.
         */
        @JsonProperty("threshold")
        protected Double threshold;

        /**
         * Getter method for the {@link #threshold} property.
         *
         * @return the value assigned to the {@link #threshold} property.
         */
        public Double getThreshold() {
            return this.threshold;
        }

        public ThresholdValueSummaryOperator() {
        }

        public ThresholdValueSummaryOperator(String name,
                                             String operatorType,
                                             List<String> inputVariables,
                                             List<OutputVariable> outputVariables,
                                             String comparator,
                                             int accumulationTimeSlices,
                                             Double threshold) {
            super(
                name,
                operatorType,
                inputVariables,
                outputVariables,
                comparator,
                accumulationTimeSlices
            );
            this.threshold = threshold;
        }

    }

    static public class ThresholdZonalSummaryOperator extends ThresholdSummaryOperator {

        /**
         * Boolean flag to identify if the input source is a rectilinear grid ({@code true} or
         * curvilinear grid ({@code false}. This value is {@code false} by default.
         */
        protected boolean isRectilinearGrid = false;

        @JsonProperty(value = "isRectilinearGrid", required = false)
        public boolean isRectilinearGrid() {
            return this.isRectilinearGrid;
        }

        public void setRectilinearGrid(boolean flag) {
            this.isRectilinearGrid = flag;
        }


        /**
         * The {@code id} to which the {@code ZoneIds} are bound.
         */
        @JsonProperty("zonesInputId")
        protected String zonesInputId;

        /**
         * Getter method for the {@link #zonesInputId} property.
         *
         * @return the value assigned to the {@link #zonesInputId} property.
         */
        public String getZonesInputId() {
            return this.zonesInputId;
        }

        /**
         * The {@code id} to which the {@code Thresholds} are bound.
         */
        @JsonProperty("thresholdsInputId")
        protected String thresholdsInputId;

        /**
         * Getter method for the {@link #thresholdsInputId} property.
         *
         * @return the value assigned to the {@link #thresholdsInputId} property.
         */
        public String getThresholdsInputId() {
            return this.thresholdsInputId;
        }

        public ThresholdZonalSummaryOperator() {
        }

        public ThresholdZonalSummaryOperator(String name,
                                             String operatorType,
                                             List<String> inputVariables,
                                             List<OutputVariable> outputVariables,
                                             String comparator,
                                             int accumulationTimeSlices,
                                             String thresholdsInputId,
                                             String zonesInputId,
                                             boolean isRectilinearGrid) {
            super(
                name,
                operatorType,
                inputVariables,
                outputVariables,
                comparator,
                accumulationTimeSlices
            );
            this.thresholdsInputId = thresholdsInputId;
            this.zonesInputId = zonesInputId;
            this.isRectilinearGrid = isRectilinearGrid;
        }


    }

    // ---------------------------------------------------------------------------------------------
    // OutputVariable
    // ---------------------------------------------------------------------------------------------
    static public class OutputVariable {

        /**
         * Internal map populated with the attributes to use.
         */
        protected Map<String, String> attributes = new HashMap<>();

        /**
         * Getter method for {@link #attributes} map.
         *
         * @return the references assigned to the {@link #attributes} map.
         */
        public Map<String, String> getAttributes() {
            return this.attributes;
        }

        public void setAttributes(Map<String, String> attributes) {
            this.attributes.putAll(attributes);
        }

    }
    // ---------------------------------------------------------------------------------------------
    // Definition
    // ---------------------------------------------------------------------------------------------

    protected String targetTimeZone;

    public String getTargetTimeZone() {
        return this.targetTimeZone;
    }

    protected void setTargetTimeZone(String timeZone) {
        this.targetTimeZone = timeZone;
    }

    /**
     * The list of primary {@link StaticFile}s for the {@code Product}. This currently supports the
     * following types: {@link NetCDFInput}.
     */
    protected NetCDFInput[] inputs;

    protected void setInputs(NetCDFInput[] inputs) {
        this.inputs = inputs;
    }

    /**
     * Getter for the {@link #inputs} property.
     *
     * @return the value assigned to {@link #inputs}.
     */
    public NetCDFInput[] getInputs() {
        return this.inputs;
    }

    /**
     * The list of other {@link StaticFile}s required by the {@code Product}.
     */
    protected List<StaticFile> staticFiles = new ArrayList<>();

    protected void setStaticFiles(List<StaticFile> staticFiles) {
        this.staticFiles.addAll(staticFiles);
    }

    /**
     * A list of {@link PreProcessingTaskDefn}s defined by the {@code Product}.
     */
    protected List<PreProcessingTaskDefn> preProcessingTasks = new ArrayList<>();

    public List<PreProcessingTaskDefn> getPreProcessingTasks() {
        return this.preProcessingTasks;
    }

    protected void setPreProcessingTasks(List<PreProcessingTaskDefn> preProcessingTasks) {
        this.preProcessingTasks = preProcessingTasks;
    }

    /**
     * Getter for the {@link #staticFiles} property.
     *
     * @return the value assigned to {@link #staticFiles}.
     */
    public List<StaticFile> getStaticFiles() {
        return this.staticFiles;
    }

    /**
     * The {@link Action} to be performed for the {@code Product}.
     */
    protected Action action;

    protected void setAction(Action action) {
        this.action = action;
    }

    /**
     * Getter for the {@link #action} property.
     *
     * @return the value assigned to the {@link #action}.
     */
    public Action getAction() {
        return this.action;
    }

    protected Outputs outputs;

    protected void setOutputs(Outputs outputs) {
        this.outputs = outputs;
    }

    public Outputs getOutputs() {
        return this.outputs;
    }


    /**
     * Constructor used by the {@code Jackson} library. This is required to specify {@code _id} as
     * the identifier.
     */
    @JsonCreator
    public NcAggregateProductDefinition(@JsonProperty("_id") String id) {
        super(id);
    }

    /**
     * Factory method to instantiate a {@link NcAggregateProductDefinition} for convenience.
     */
    static public NcAggregateProductDefinition make(String id,
                                                    String targetTimeZone,
                                                    Filters filters,
                                                    NetCDFInput[] inputs,
                                                    List<PreProcessingTaskDefn> preProcessingTasks,
                                                    Action action,
                                                    Outputs outputs) {
        NcAggregateProductDefinition pojo = new NcAggregateProductDefinition(id);
        pojo.setTargetTimeZone(targetTimeZone);
        pojo.setFilters(filters);
        pojo.setInputs(inputs);
        pojo.setPreProcessingTasks(preProcessingTasks);
        pojo.setAction(action);
        pojo.setOutputs(outputs);
        return pojo;
    }

}