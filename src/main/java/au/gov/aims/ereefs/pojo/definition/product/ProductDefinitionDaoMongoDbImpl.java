package au.gov.aims.ereefs.pojo.definition.product;

import au.gov.aims.ereefs.pojo.definition.AbstractDefinitionDaoMongoDbImpl;
import com.mongodb.client.MongoDatabase;

/**
 * Specialisation of {@link AbstractDefinitionDaoMongoDbImpl} class to support
 * {@link ProductDefinition} objects.
 *
 * @author Aaron Smith
 */
public class ProductDefinitionDaoMongoDbImpl extends AbstractDefinitionDaoMongoDbImpl<ProductDefinition>
    implements ProductDefinitionDao {

    /**
     * Constant identifying the name of the underlying {@code MongoDB} collection.
     */
    final static public String COLLECTION_NAME = "product";

    public ProductDefinitionDaoMongoDbImpl(MongoDatabase mongoDatabase) {
        super(mongoDatabase, COLLECTION_NAME);
    }

    @Override
    protected Class<ProductDefinition> getPojoClass() {
        return ProductDefinition.class;
    }

    @Override
    protected String getPojoId(ProductDefinition pojo) {
        return pojo.getId();
    }

}
