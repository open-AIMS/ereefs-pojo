package au.gov.aims.ereefs.pojo.extractionrequest;

import au.gov.aims.ereefs.pojo.AbstractPojoDaoMongoDbImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;

/**
 * Specialisation of {@link AbstractPojoDaoMongoDbImpl} class to support {@link ExtractionRequest
 * objects.
 *
 * @author Aaron Smith
 */
public class ExtractionRequestDaoMongoDbImpl extends AbstractPojoDaoMongoDbImpl<ExtractionRequest>
    implements ExtractionRequestDao {

    /**
     * Constant identifying the name of the underlying {@code MongoDB} collection.
     */
    final static public String COLLECTION_NAME = "extraction_request";

    /**
     * Constructor to capture references relevant to this implementation.
     */
    public ExtractionRequestDaoMongoDbImpl(MongoDatabase mongoDatabase) {
        super(mongoDatabase, COLLECTION_NAME);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    protected Class<ExtractionRequest> getPojoClass() {
        return ExtractionRequest.class;
    }

    @Override
    protected String getPojoId(ExtractionRequest pojo) {
        return pojo.getId();
    }

    @Override
    public void setJobId(String id, String jobId) {
        final Document document = this.getDocumentById(id);
        ObjectNode extractionRequest = null;
        try {
            extractionRequest = (ObjectNode) this.jsonMapper.readTree(document.toJson(this.writerSettings));
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert Document to JSON (id: " + id + ").", e);
        }
        if (extractionRequest == null) {
            throw new RuntimeException("Failed to retrieve Document (id: " + id + ").");
        }
        ObjectNode executionContext = (ObjectNode) extractionRequest.get("executionContext");
        if (executionContext == null) {
            executionContext = (ObjectNode) extractionRequest.putObject("executionContext");
        }
        executionContext.put("jobId", jobId);
        this.persist(extractionRequest);
    }

    @Override
    public String getJobId(String id) {
        final Document document = this.getDocumentById(id);
        ObjectNode extractionRequest = null;
        try {
            extractionRequest = (ObjectNode) this.jsonMapper.readTree(document.toJson(this.writerSettings));
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert Document to JSON (id: " + id + ").", e);
        }
        if (extractionRequest == null) {
            throw new RuntimeException("Failed to retrieve Document (id: " + id + ").");
        }
        ObjectNode executionContext = (ObjectNode) extractionRequest.get("executionContext");
        if (executionContext == null) {
            return null;
        }
        JsonNode jobIdNode = executionContext.get("jobId");
        if (jobIdNode != null) {
            return jobIdNode.asText();
        }
        return null;
    }

    /**
     * Extends the {@code persist()} to force the {@link ExtractionRequest} to register that an
     * update has occurred.
     */
    @Override
    public ExtractionRequest persist(ExtractionRequest extractionRequest) {

        // Force update of updatedAt property.
        extractionRequest.markAsUpdated();

        // Persist the object.
        return super.persist(extractionRequest);

    }

}
