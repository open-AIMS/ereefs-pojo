package au.gov.aims.ereefs.pojo;

import java.io.File;

/**
 * Extends {@link AbstractPojoDaoImpl} to support a {@code file-based} {@link PojoDao} repository.
 *
 * @author Aaron Smith
 */
abstract public class AbstractPojoDaoFileImpl<T> extends AbstractPojoDaoImpl<T> {

    /**
     * Cached reference to the parent directory. Implementations should recursively search sub-
     * directories.
     */
    protected File path;

    /**
     * Constructor to capture references to the parent directory containing the metadata.
     *
     * @param path the path to the file-based repository.
     */
    public AbstractPojoDaoFileImpl(String path) {
        super();
        this.path = new File(path);
        if (!this.path.exists()) {
            this.path.mkdirs();
        }
        if (!this.path.canRead()) {
            throw new RuntimeException("Cannot read from \"" + path + "\".");
        }
    }

}
