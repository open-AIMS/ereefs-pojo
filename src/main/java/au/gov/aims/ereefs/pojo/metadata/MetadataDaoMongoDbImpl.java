package au.gov.aims.ereefs.pojo.metadata;

import au.gov.aims.ereefs.pojo.AbstractPojoDaoMongoDbImpl;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialisation of {@link AbstractPojoDaoMongoDbImpl} class to support {@link Metadata} objects.
 *
 * @author Aaron Smith
 */
public class MetadataDaoMongoDbImpl extends AbstractPojoDaoMongoDbImpl<Metadata>
    implements MetadataDao {

    /**
     * Constant identifying the name of the underlying {@code MongoDB} collection.
     */
    final static public String COLLECTION_NAME = "metadata";

    /**
     * Constructor to capture references relevant to this implementation.
     */
    public MetadataDaoMongoDbImpl(MongoDatabase mongoDatabase) {
        super(mongoDatabase, COLLECTION_NAME);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    protected Class<Metadata> getPojoClass() {
        return Metadata.class;
    }

    @Override
    protected String getPojoId(Metadata pojo) {
        return pojo.getId();
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public List<Metadata> getByDefinitionId(String id, String type) {

        // Declare the return value.
        final List<Metadata> list = new ArrayList<>();

        // Filter the collection.
        final MongoCursor<Document> iterator =
            this.collection.find(
                Filters.and(
                    Filters.eq("type", type),
                    Filters.eq("definitionId", id),
                    Filters.eq("status", "VALID")
                )
            ).iterator();
        while (iterator.hasNext()) {
            Metadata pojo = this.convertToPojo(iterator.next());
            if (pojo != null) {
                list.add(pojo);
            }
        }

        return list;

    }

    /**
     * Persist the {@code JSONObject} to the database.
     */
    @Override
    public JSONObject persist(JSONObject jsonObject) {

        this.collection.replaceOne(
            Filters.eq("_id", jsonObject.getString("_id")),
            Document.parse(jsonObject.toString()),
            new ReplaceOptions().upsert(true)
        );

        return jsonObject;

    }

}
