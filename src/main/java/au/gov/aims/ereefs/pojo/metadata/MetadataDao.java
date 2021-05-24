package au.gov.aims.ereefs.pojo.metadata;

import au.gov.aims.ereefs.pojo.PojoDao;
import org.json.JSONObject;

import java.util.List;

/**
 * Interface for {@code DAO} classes that access {@code Metadata} information.
 *
 * @author Aaron Smith
 */
public interface MetadataDao extends PojoDao<Metadata> {

    /**
     * Retrieve the {@code Metadata} of all files for the specified {@code DefinitionId}. The
     * {@code DefinitionId} could reference a downloadable data source, or a product definition.
     */
    public List<Metadata> getByDefinitionId(String id, String type);

    /**
     * Persist the {@code JSONObject} to the repository.
     */
    public JSONObject persist(JSONObject jsonObject);

}
