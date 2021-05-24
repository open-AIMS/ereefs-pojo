package au.gov.aims.ereefs.pojo.task;

import au.gov.aims.ereefs.pojo.AbstractPojoDaoMongoDbImpl;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialisation of {@link AbstractPojoDaoMongoDbImpl} class to support {@link Task} objects.
 *
 * @author Aaron Smith
 */
public class TaskDaoMongoDbImpl extends AbstractPojoDaoMongoDbImpl<Task> implements TaskDao {

    /**
     * Constant identifying the name of the underlying {@code MongoDB} collection.
     */
    final static public String COLLECTION_NAME = "task";

    /**
     * Constructor to capture references relevant to this implementation.
     */
    public TaskDaoMongoDbImpl(MongoDatabase mongoDatabase) {
        super(mongoDatabase, COLLECTION_NAME);
    }

    @Override
    protected Class<Task> getPojoClass() {
        return Task.class;
    }

    @Override
    protected String getPojoId(Task pojo) {
        return pojo.getId();
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public List<Task> getByJobId(String jobId) {

        // Declare the return value.
        final List<Task> list = new ArrayList<>();

        // Filter the collection.
        final MongoCursor<Document> iterator =
            this.collection.find(Filters.eq("jobId", jobId)).iterator();
        while (iterator.hasNext()) {
            Task pojo = this.convertToPojo(iterator.next());
            if (pojo != null) {
                list.add(pojo);
            }
        }

        return list;

    }

}
