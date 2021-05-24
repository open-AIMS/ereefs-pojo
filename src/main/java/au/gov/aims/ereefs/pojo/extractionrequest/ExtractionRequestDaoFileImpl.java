package au.gov.aims.ereefs.pojo.extractionrequest;

import au.gov.aims.ereefs.pojo.AbstractPojoDaoFileImpl;
import au.gov.aims.ereefs.pojo.utils.JSONFileReader;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Concrete implementation of the {@link ExtractionRequestDao} interface for a file-based
 * repository.
 *
 * @author Aaron Smith
 */
public class ExtractionRequestDaoFileImpl extends AbstractPojoDaoFileImpl<ExtractionRequest>
    implements ExtractionRequestDao {

    /**
     * Internal cache of all {@link ExtractionRequest} retrieved from the database/repository,
     * keyed by their unique Id.
     */
    private Map<String, ExtractionRequest> internalCache = new TreeMap<>();

    /**
     * Internal flag to determine if the {@link #internalCache internal cache} has been populated.
     */
    private boolean isCached = false;

    /**
     * Constructor.
     */
    public ExtractionRequestDaoFileImpl(String path) {
        super(path);
    }

    /**
     * Provides access to the {@link #internalCache internal cache}. The internal cache is
     * automatically populated if not already (see {@link #isCached}).
     */
    private Map<String, ExtractionRequest> getInternalCache() {
        if (!this.isCached) {
            this.internalCache.clear();
            try {
                for (JSONObject json : JSONFileReader.loadList(this.path)) {

                    // Identify the ProductContext it belongs to.
                    String id = json.getString("_id");

                    // Add the object to the cache.
                    try {
                        this.internalCache.put(
                            id,
                            this.jsonMapper.readValue(
                                json.toString(),
                                ExtractionRequest.class)
                        );
                    } catch (Exception e) {
                        throw new Exception("Failed to transform the JSON (\"" + id + "\").", e);
                    }

                }
            } catch (Exception ignore) {
                this.logger.warn("Failed to load extraction request at \"" + this.path + "\".", ignore);
            }
            this.isCached = true;
        }
        return this.internalCache;
    }

    @Override
    public ExtractionRequest getById(String id) {
        return this.getInternalCache().get(id);
    }

    @Override
    public ExtractionRequest persist(ExtractionRequest extractionRequest) {
        try {
            // Force update of updatedAt property.
            extractionRequest.markAsUpdated();

            // Persist the object.
            this.persist(
                extractionRequest.getId(),
                this.jsonMapper.writeValueAsString(extractionRequest)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to write extraction request object.", e);
        }
        return extractionRequest;
    }

    @Override
    public void setJobId(String id, String jobId) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public String getJobId(String id) {
        throw new RuntimeException("Not implemented.");
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
            throw new RuntimeException("Failed to write extraction request object.", e);
        }

        return pojo;

    }

    @Override
    protected Class<ExtractionRequest> getPojoClass() {
        return ExtractionRequest.class;
    }

    @Override
    protected String getPojoId(ExtractionRequest pojo) {
        return pojo.getId();
    }

}
