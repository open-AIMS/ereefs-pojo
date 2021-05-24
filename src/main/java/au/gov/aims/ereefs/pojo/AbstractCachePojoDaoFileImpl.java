package au.gov.aims.ereefs.pojo;

import au.gov.aims.ereefs.pojo.utils.JSONFileReader;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract specialisation of the {@link AbstractPojoDaoFileImpl} to add generic caching
 * functionality.
 *
 * @author Aaron Smith
 */
public abstract class AbstractCachePojoDaoFileImpl<T> extends AbstractPojoDaoFileImpl<T> {

    /**
     * Internal cache of records populated on the first invocation of {@link #getInternalCache()}.
     */
    protected Map<String, T> internalCache = new TreeMap<>();

    /**
     * Internal flag to determine if the {@link #internalCache internal cache} has been populated.
     */
    protected boolean isCached = false;

    /**
     * Constructor.
     */
    public AbstractCachePojoDaoFileImpl(String path) {
        super(path);
    }

    /**
     * Provides access to the {@link #internalCache internal cache}. The internal cache is
     * automatically populated if not already (see {@link #isCached}).
     */
    protected Map<String, T> getInternalCache() {
        if (!this.isCached) {
            this.internalCache.clear();
            try {
                for (JSONObject json : JSONFileReader.loadList(this.path)) {
                    T pojo = this.convertToPojo(json.toString());
                    if (pojo != null) {
                        this.internalCache.put(this.getPojoId(pojo), pojo);
                    }
                }
            } catch (IOException ignore) {
                System.err.println("Failed to load records at \"" + this.path + "\".");
                ignore.printStackTrace(System.err);
            }
            this.isCached = true;
        }
        return this.internalCache;
    }

    @Override
    public T getById(String id) {
        return this.getInternalCache().get(id);
    }

    @Override
    public T persist(T pojo) {
        throw new RuntimeException("Not implemented.");
    }

}
