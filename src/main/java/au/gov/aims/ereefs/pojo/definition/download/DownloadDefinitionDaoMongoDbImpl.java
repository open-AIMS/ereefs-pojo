package au.gov.aims.ereefs.pojo.definition.download;

import au.gov.aims.ereefs.pojo.definition.AbstractDefinitionDaoMongoDbImpl;
import com.mongodb.client.MongoDatabase;

/**
 * Specialisation of {@link AbstractDefinitionDaoMongoDbImpl} class to support
 * {@link DownloadDefinition} objects.
 *
 * @author Aaron Smith
 */
public class DownloadDefinitionDaoMongoDbImpl extends AbstractDefinitionDaoMongoDbImpl<DownloadDefinition>
    implements DownloadDefinitionDao {

    /**
     * Constant identifying the name of the underlying {@code MongoDB} collection.
     */
    final static public String COLLECTION_NAME = "download";

    public DownloadDefinitionDaoMongoDbImpl(MongoDatabase mongoDatabase) {
        super(mongoDatabase, COLLECTION_NAME);
    }

    @Override
    protected Class<DownloadDefinition> getPojoClass() {
        return DownloadDefinition.class;
    }

    @Override
    protected String getPojoId(DownloadDefinition pojo) {
        return pojo.getId();
    }

}
