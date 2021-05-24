package au.gov.aims.ereefs.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A base implementation of the {@link PojoDao} interface that implements methods shared between
 * the concrete implementations.
 *
 * @author Aaron Smith
 */
abstract public class AbstractPojoDaoImpl<T> implements PojoDao<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Utility from the {@code Jackson} library for converting between JSON and a POJO.
     */
    protected ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * Factory method to convert the {@code JSON} (as represented by a {@code String}) to a POJO
     * using the {@link #jsonMapper}.
     */
    protected T convertToPojo(final String json) {
        try {
            return this.jsonMapper.readValue(
                json,
                this.getPojoClass()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to instantiate \"" + this.getPojoClass() + "\".", e);
        }
    }

    /**
     * Returns the {@code Class} of the entity defined by {@code T} to provide the
     * {@link #jsonMapper} with the information it needs to instantiate the correct POJO.
     */
    abstract protected Class<T> getPojoClass();

    /**
     * Returns the unique {@code id} for the entity.
     */
    abstract protected String getPojoId(T pojo);

}
