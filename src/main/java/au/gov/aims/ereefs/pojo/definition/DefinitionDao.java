package au.gov.aims.ereefs.pojo.definition;

import au.gov.aims.ereefs.pojo.PojoDao;

import java.util.List;

/**
 * Specialisation of the {@link PojoDao} for {@code Definition} objects that are few in number,
 * and often need to be all retrieved at once.
 *
 * @author Aaron Smith
 */
public interface DefinitionDao<T> extends PojoDao<T> {

    /**
     * Returns a list of all {@code Pojo} records where {@code isEnabled} is {@code true}.
     */
    List<T> findAllEnabled();

}
