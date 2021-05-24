package au.gov.aims.ereefs.pojo.task;

import au.gov.aims.ereefs.pojo.PojoDao;

import java.util.List;

/**
 * {@link Task}-based specialisation of the {@link PojoDao} interface.
 *
 * @author Aaron Smith
 */
public interface TaskDao extends PojoDao<Task> {

    /**
     * Retrieve the {@code Task}s for the specified {@code JobId}.
     */
    public List<Task> getByJobId(String jobId);

}
