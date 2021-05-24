package au.gov.aims.ereefs.pojo.metadata;

import au.gov.aims.ereefs.pojo.AbstractPojoDaoFileImpl;
import au.gov.aims.ereefs.pojo.utils.JSONFileReader;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Concrete implementation of the {@link MetadataDao} interface for a file-based
 * repository.
 *
 * @author Aaron Smith
 */
public class MetadataDaoFileImpl extends AbstractPojoDaoFileImpl<Metadata>
    implements MetadataDao {

    /**
     * Internal cache of all {@link Metadata} retrieved from the database/repository. This
     * cache is populated on the first invocation of {@link #getInternalCache()}. The key of this
     * cache is the unique {@code DefinitionId}, which defines either a {@code Download Source} or
     * a {@code ProductContext}.
     */
    private Map<String, List<Metadata>> internalCache = new TreeMap<>();

    /**
     * Internal flag to determine if the {@link #internalCache internal cache} has been populated.
     */
    private boolean isCached = false;

    /**
     * Constructor.
     */
    public MetadataDaoFileImpl(String path) {
        super(path);
    }

    /**
     * Provides access to the {@link #internalCache internal cache}. The internal cache is
     * automatically populated if not already (see {@link #isCached}).
     */
    private Map<String, List<Metadata>> getInternalCache() {
        if (!this.isCached) {
            this.internalCache.clear();
            try {
                for (JSONObject json : JSONFileReader.loadList(this.path)) {

                    // Identify the ProductContext it belongs to.
                    String id = json.getString("_id");

                    // Add the entry to the cache if it doesn't already exist.
                    String definitionId = json.getString("definitionId");
                    String type = json.getString("type");
                    String key = type + "::" + definitionId;
                    this.internalCache.putIfAbsent(key, new ArrayList<>());

                    // Add the metadata to the cache.
                    try {
                        this.internalCache.get(key).add(
                            this.jsonMapper.readValue(
                                json.toString(),
                                Metadata.class)
                        );
                    } catch (Exception e) {
                        throw new Exception("Failed to transform the JSON (\"" + id + "\").", e);
                    }

                }
            } catch (Exception ignore) {
                this.logger.warn("Failed to load metadata at \"" + this.path + "\".", ignore);
            }
            this.isCached = true;
        }
        return this.internalCache;
    }

    @Override
    public List<Metadata> getByDefinitionId(String id, String type) {
        // Retrieve the results, or an empty list if no results.
        final List<Metadata> list = this.getInternalCache().get(type + "::" + id);
        return (list != null ? list : new ArrayList<>());
    }

    @Override
    public Metadata getById(String id) {
        for (List<Metadata> metadataList : this.getInternalCache().values()) {
            for (Metadata metadata : metadataList) {
                if (metadata.getId().equals(id)) {
                    return metadata;
                }
            }
        }
        return null;
    }

    @Override
    public Metadata persist(Metadata metadata) {
        try {
            this.persist(
                metadata.getId(),
                this.jsonMapper.writeValueAsString(metadata)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Metadata object.", e);
        }
        return metadata;
    }

    @Override
    public JSONObject persist(JSONObject jsonObject) {
        this.persist(
            jsonObject.getString("_id"),
            jsonObject.toString(4)
        );
        return jsonObject;
    }

    /**
     * Helper method to persist the POJO.
     */
    protected String persist(String id, String pojo) {

        final String filePath = this.path + File.separator + id + ".json";

        // Ensure the path exists.
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        // Write the file.
        try (final BufferedWriter bufferedWriter = new BufferedWriter(
            new OutputStreamWriter(
                new FileOutputStream(
                    new File(filePath))
            )
        )
        ) {
            bufferedWriter.write(pojo);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Metadata object.", e);
        }

        return pojo;

    }

    @Override
    protected Class<Metadata> getPojoClass() {
        return Metadata.class;
    }

    @Override
    protected String getPojoId(Metadata pojo) {
        return pojo.getId();
    }
}
