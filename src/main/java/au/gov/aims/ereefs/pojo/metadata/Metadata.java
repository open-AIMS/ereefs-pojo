package au.gov.aims.ereefs.pojo.metadata;

import au.gov.aims.ereefs.pojo.Pojo;
import com.fasterxml.jackson.annotation.*;

import java.util.Date;

/**
 * A POJO representing the metadata of a single file that has either been downloaded or generated
 * by the system. The corresponding file may (or may not) be used as an input for other Products
 * generated by the system.
 *
 * @author Aaron Smith
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = false,
    defaultImpl = Metadata.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = NetCDFMetadata.class, name = NetCDFMetadata.TYPE),
})
@JsonIgnoreProperties({"version", "lastDownloaded"})
public class Metadata extends Pojo {

    /**
     * The unique {@code id} of the entity type that this {@code Metadata} describes. Currently
     * supports {@link au.gov.aims.ereefs.pojo.definition.download.DownloadDefinition}s and
     * {@link au.gov.aims.ereefs.pojo.definition.product.ProductDefinition}s.
     */
    @JsonProperty
    protected String definitionId;

    public String getDefinitionId() {
        return this.definitionId;
    }

    protected void setDefinitionId(String id) {
        this.definitionId = id;
    }

    /**
     * The {@code URI} via which the described file can be accessed.
     */
    @JsonProperty
    protected String fileURI;

    public String getFileURI() {
        return this.fileURI;
    }

    protected void setFileURI(String uri) {
        this.fileURI = uri;
    }

    /**
     * The last time the file (and therefore this {@link Metadata} record) was updated. This is
     * used by the {@code JobPlanner} to determine if dependent Products require updating.
     */
    @JsonProperty
    protected Date lastModified;

    public Date getLastModified() {
        return this.lastModified;
    }

    protected void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @JsonCreator
    public Metadata(@JsonProperty("_id") String id) {
        super(id);
    }

}
