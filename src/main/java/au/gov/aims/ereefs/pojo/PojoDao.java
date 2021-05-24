package au.gov.aims.ereefs.pojo;

/**
 * Interface for reading/writing {@Link Pojo}s to a database.
 *
 * @author Aaron Smith
 */
public interface PojoDao<T> {

    /**
     * Retrieve a {@link Pojo} from the repository based on the specified {@link Pojo#id}.
     */
    public T getById(String id);

    /**
     * Persist the {@code POJO} to the repository.
     */
    public T persist(T pojo);

}
