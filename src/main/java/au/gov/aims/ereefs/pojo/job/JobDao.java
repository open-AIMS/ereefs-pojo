package au.gov.aims.ereefs.pojo.job;

import au.gov.aims.ereefs.pojo.PojoDao;

import java.util.List;

/**
 * {@link Job}-based specialisation of the {@link PojoDao} interface.
 *
 * @author Aaron Smith
 */
public interface JobDao extends PojoDao<Job> {

    /**
     * Retrieve a list of all {@link Job}s that have an {@code active} {@link Job#status}. An
     * {@code active} status is any status that is not a {@code final} status.
     *
     * @see JobStatus
     */
    List<Job> findAllActive();

}
