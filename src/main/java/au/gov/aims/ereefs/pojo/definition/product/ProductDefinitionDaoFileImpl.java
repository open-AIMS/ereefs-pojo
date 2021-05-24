package au.gov.aims.ereefs.pojo.definition.product;

import au.gov.aims.ereefs.pojo.AbstractCachePojoDaoFileImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete implementation of the {@link ProductDefinitionDao} interface for a file-based
 * repository.
 *
 * @author Aaron Smith
 */
public class ProductDefinitionDaoFileImpl extends AbstractCachePojoDaoFileImpl<ProductDefinition>
    implements ProductDefinitionDao {

    /**
     * Constructor.
     */
    public ProductDefinitionDaoFileImpl(String path) {
        super(path);
    }

    @Override
    public List<ProductDefinition> findAllEnabled() {
        return this.getInternalCache().values().stream()
            .filter(defn -> defn.isEnabled() == true)
            .collect(Collectors.toList());
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
