package au.gov.aims.ereefs.pojo.definition;

import au.gov.aims.ereefs.pojo.AbstractPojoDaoMongoDbImpl;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract, generic implementation of the {@DefintionDao} interface for a MongoDB-based
 * repository.
 *
 * @author Aaron Smith
 */
abstract public class AbstractDefinitionDaoMongoDbImpl<T> extends AbstractPojoDaoMongoDbImpl<T>
    implements DefinitionDao<T> {

    /**
     * Constructor to capture references relevant to this implementation.
     */
    public AbstractDefinitionDaoMongoDbImpl(MongoDatabase mongoDatabase,
                                            String collectionName) {
        super(mongoDatabase, collectionName);
    }

    @Override
    public List<T> findAllEnabled() {

        // Declare the return value.
        final List<T> list = new ArrayList<>();

        // Filter the collection.
        final MongoCursor<Document> iterator =
            this.collection.find(Filters.eq("enabled", true)).iterator();
        while (iterator.hasNext()) {
            T pojo = this.convertToPojo(iterator.next());
            if (pojo != null) {
                list.add(pojo);
            }
        }

        return list;

    }

}
