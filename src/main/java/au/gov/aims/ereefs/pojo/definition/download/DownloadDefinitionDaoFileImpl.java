package au.gov.aims.ereefs.pojo.definition.download;

import au.gov.aims.ereefs.pojo.AbstractCachePojoDaoFileImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete implementation of the {@link DownloadDefinitionDao} interface for a file-based
 * repository.
 *
 * @author Aaron Smith
 */
public class DownloadDefinitionDaoFileImpl extends AbstractCachePojoDaoFileImpl<DownloadDefinition>
    implements DownloadDefinitionDao {

    /**
     * Constructor.
     */
    public DownloadDefinitionDaoFileImpl(String path) {
        super(path);
    }

    @Override
    public List<DownloadDefinition> findAllEnabled() {
        return this.getInternalCache().values().stream()
            .filter(defn -> defn.isEnabled() == true)
            .collect(Collectors.toList());
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
