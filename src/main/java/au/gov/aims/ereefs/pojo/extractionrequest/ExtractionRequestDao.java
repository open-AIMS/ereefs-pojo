package au.gov.aims.ereefs.pojo.extractionrequest;

import au.gov.aims.ereefs.pojo.PojoDao;
import au.gov.aims.ereefs.pojo.metadata.Metadata;
import org.json.JSONObject;

import java.util.List;

/**
 * Interface for {@code DAO} classes that access {@code ExtractionRequest} information.
 *
 * @author Aaron Smith
 */
public interface ExtractionRequestDao extends PojoDao<ExtractionRequest> {

    /**
     * Setter method for the {@code JobId}, which is stored in the {@code ExecutionContext} node of
     * the {@link ExtractionRequest}. This value is not writable via the POJO.
     *
     * @see #getJobId(String id)
     */
    void setJobId(String id, String jobId);

    /**
     * Getter method for the {@code JobId}, which is stored in the {@code ExecutionContext} node of
     * the {@link ExtractionRequest}. This value is not accessible via the POJO.
     *
     * @see #setJobId(String id, String jobId)
     */
    String getJobId(String id);

}
