package au.gov.aims.ereefs.pojo.task;

import au.gov.aims.ereefs.pojo.definition.product.NcAggregateProductDefinition;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialisation of the {@link Task} base class to define a single {@code aggregation} task for
 * the {@code ncAggregate} functionality.
 *
 * @author Aaron Smith
 */
public class NcAggregateTask extends Task {

    /**
     * The unique {@code id} of the metadata representing the file to be created.
     */
    @JsonProperty
    protected String metadataId;

    public String getMetadataId() {
        return this.metadataId;
    }

    /**
     * The base name and path for any output files created. Normally a suffix will be appended to
     * this to create the full name and path of the output file, such as ".nc" or "-summary.csv".
     */
    @JsonProperty
    protected String baseUrl;

    public String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * The list of {@link TimeInstant}s that form this {@link Task}.
     */
    @JsonProperty
    protected List<TimeInstant> timeInstants;

    public List<TimeInstant> getTimeInstants() {
        return this.timeInstants;
    }

    /**
     * The outcome from the processing, normally completed by the Task handler.
     */
    @JsonProperty
    protected Outcome outcome = new Outcome();

    public Outcome getOutcome() {
        return this.outcome;
    }

    /**
     * Constructor, primarily for use by Jackson to populate {@link Task} from the repository.
     */
    @JsonCreator
    public NcAggregateTask(@JsonProperty("_id") String id) {
        super(id);
    }

    /**
     * Convenience constructor, primarily for use by the application, invoking
     * {@link Task#Task(String, String)}.
     */
    public NcAggregateTask(String jobId,
                           String productDefinitionId,
                           String metadataId,
                           String baseUrl,
                           List<TimeInstant> timeInstants) {
        super(jobId, productDefinitionId);
        this.metadataId = metadataId;
        this.baseUrl = baseUrl;
        this.timeInstants = timeInstants;
    }

    // ---------------------------------------------------------------------------------------------
    // TimeInstant
    // ---------------------------------------------------------------------------------------------
    static public class TimeInstant {

        /**
         * The time value, in days since epoch.
         */
        @JsonProperty
        protected double value;

        /**
         * Getter for the {@link #value} property.
         *
         * @return the value assigned to {@link #value}.
         */
        public double getValue() {
            return this.value;
        }

        /**
         * The list of {@link Input} objects that contribute to this {@link TimeInstant}.
         */
        @JsonProperty
        protected List<Input> inputs;

        /**
         * Getter for the {@link #inputs} property.
         *
         * @return the value assigned to {@link #inputs}.
         */
        public List<Input> getInputs() {
            return this.inputs;
        }

        @JsonCreator
        public TimeInstant() {
            super();
        }

        /**
         * Convenience constructor.
         */
        public TimeInstant(double value,
                           List<Input> inputs) {
            this.value = value;
            this.inputs = inputs;
        }

    }

    // ---------------------------------------------------------------------------------------------
    // Input
    // ---------------------------------------------------------------------------------------------
    static public class Input {

        /**
         * The {@code id} of the corresponding {@code input}. This matches {@code Product.Input.id}
         * property.
         */
        @JsonProperty
        protected String inputId;

        /**
         * Getter for the {@link #inputId} property.
         *
         * @return the value assigned to {@link #inputId}.
         */
        public String getInputId() {
            return this.inputId;
        }

        /**
         * The bounds of the input file for this {@link Input} contributing to the parent
         * {@link TimeInstant}.
         */
        @JsonProperty
        protected List<FileIndexBounds> fileIndexBounds;

        /**
         * Getter for the {@link #fileIndexBounds} property.
         *
         * @return the value assigned to {@link #fileIndexBounds}.
         */
        public List<FileIndexBounds> getFileIndexBounds() {
            return this.fileIndexBounds;
        }

        @JsonCreator
        public Input() {
        }

        /**
         * Convenience constructor.
         */
        public Input(String inputId,
                     List<FileIndexBounds> fileIndexBounds) {
            this.inputId = inputId;
            this.fileIndexBounds = fileIndexBounds;
        }

    }

    // ---------------------------------------------------------------------------------------------
    // FileIndexBounds
    // ---------------------------------------------------------------------------------------------
    static public class FileIndexBounds {

        /**
         * The unique {@code id} for the {@link au.gov.aims.ereefs.pojo.metadata.Metadata} object
         * being referenced. This points to a specific file.
         */
        @JsonProperty
        protected String metadataId;

        public String getMetadataId() {
            return this.metadataId;
        }

        /**
         * The index (position) in the input file that represents the first piece of data of
         * interest.
         */
        @JsonProperty
        protected int startIndex;

        public int getStartIndex() {
            return this.startIndex;
        }

        /**
         * The index (position) in the input file that represents the last piece of data of
         * interest.
         */
        @JsonProperty
        protected int endIndex;

        public int getEndIndex() {
            return this.endIndex;
        }

        /**
         * Constructor.
         */
        @JsonCreator
        public FileIndexBounds() {
        }

        /**
         * Convenience constructor.
         */
        public FileIndexBounds(String metadataId,
                               int startIndex,
                               int endIndex) {
            this.metadataId = metadataId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

    }

    // ---------------------------------------------------------------------------------------------
    // Outcome
    // ---------------------------------------------------------------------------------------------

    /**
     * Enumerator identifying the type of files generated by the system.
     */
    static public enum GeneratedFileType {

        /**
         * A {@code NetCDF} file.
         */
        @JsonProperty("netcdf")
        NETCDF,

        /**
         * A {@code Summary} file.
         */
        @JsonProperty("summary")
        SUMMARY;

    }

    static public class GeneratedFile {

        protected GeneratedFileType type;

        public GeneratedFileType getType() {
            return this.type;
        }

        protected String url;

        public String getUrl() {
            return this.url;
        }

        public GeneratedFile() {
        }

        public GeneratedFile(GeneratedFileType type,
                             String url) {
            this.type = type;
            this.url = url;
        }

    }

    static public class Outcome {
        protected List<GeneratedFile> generatedFiles = new ArrayList<GeneratedFile>();

        public List<GeneratedFile> getGeneratedFiles() {
            return this.generatedFiles;
        }

        public void addGeneratedFiles(List<GeneratedFile> generatedFiles) {
            this.generatedFiles.addAll(generatedFiles);
        }

        public void addGeneratedFile(GeneratedFile generatedFile) {
            this.generatedFiles.add(generatedFile);
        }
    }

}
