package au.gov.aims.ereefs.pojo.definition.download;

import au.gov.aims.ereefs.pojo.Pojo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extends {@link Pojo} to represent a definition of a single {@code Download Source}. Annotation
 * has been applied to facilitate populating an object via {@code Apache Jackson}.
 *
 * <code>
 *     {
 *   "_id": "download/ereefs/gbr1_2.0",
 *   "enabled": true,
 *   "filenameTemplate": "gbr1_simple_{year}-{month}-{day}.nc",
 *   "fileDuration": "DAILY",
 *   "catalogueUrl": "http://dapds00.nci.org.au/thredds/catalog/fx3/gbr1_2.0/catalog.xml",
 *   "hoursPerTimeIncrement": 1,
 *   "output": {
 *     "destination": "s3://ereefs-nci-mirror/gbr1_v2",
 *     "downloadDir": "/tmp/netcdf"
 *   }
 * }
 * </code>
 *
 * @author Aaron Smith
 */
@JsonIgnoreProperties({"type", "fileTimezone", "catalogueUrl", "catalogueUrls"})
public class DownloadDefinition extends Pojo {

    /**
     * Child class for defining the {@link DownloadDefinition#output} field.
     */
    @JsonIgnoreProperties({"downloadDir"})
    static class Output {

        protected String destination;
        public String getDestination() {
            return destination;
        }

        @JsonCreator
        public Output(@JsonProperty("destination") String destination) {
            super();
            this.destination = destination;
        }

    }

    /**
     * Flag identifying if files should be ({@code true}) downloaded or not ({@code false}) from
     * the corresponding source.
     */
    @JsonProperty("enabled")
    protected boolean isEnabled;
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * Template of the filename, used when downloading files. Once a file has been downloaded and
     * archived to {@code S3}, its location is stored in the corresponding {@code Metadata}.
     */
    @JsonProperty("filenameTemplate")
    protected String filenameTemplate;
    public String getFilenameTemplate() {
        return this.filenameTemplate;
    }

    /**
     * Represents the {@code output} node. See {@link Output} for more details.
     */
    @JsonProperty("output")
    protected Output output;
    public Output getOutput() {
        return this.output;
    }

    /**
     * Constructor.
     *
     * @param id the unique {code id} of the {@link DownloadDefinition}.
     */
    @JsonCreator
    public DownloadDefinition(@JsonProperty("_id") String id) {
        super(id);
    }

}
