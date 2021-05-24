package au.gov.aims.ereefs.pojo.job;

import au.gov.aims.ereefs.pojo.AbstractPojoDaoMongoDbImpl;
import au.gov.aims.ereefs.pojo.task.Task;
import au.gov.aims.ereefs.pojo.task.TaskDao;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialisation of {@link AbstractPojoDaoMongoDbImpl} class to support {@link Job} objects.
 *
 * @author Aaron Smith
 */
public class JobDaoMongoDbImpl extends AbstractPojoDaoMongoDbImpl<Job> implements JobDao {

    /**
     * Constant identifying the name of the underlying {@code MongoDB} collection.
     */
    final static public String COLLECTION_NAME = "job";

    /**
     * Cached reference to the {@link TaskDao} implementation for use when reading/writing the
     * {@link Task}s associated with a {@link Job}.
     */
    protected TaskDao taskDao;

    /**
     * Constructor to capture references relevant to this implementation.
     */
    public JobDaoMongoDbImpl(MongoDatabase mongoDatabase,
                             TaskDao taskDao) {
        super(mongoDatabase, COLLECTION_NAME);
        this.taskDao = taskDao;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Override the {@link #getById(String)} method to force loading child {@code Tasks}.
     */
    @Override
    public Job getById(String id) {

        // Populate the Job.
        Job job = super.getById(id);
        if (job != null) {
            // Populate the Task list.
            final List<Task> taskList = this.taskDao.getByJobId(id);
            if (taskList != null) {
                job.addTasks(taskList);
            }
        }

        return job;
    }

    @Override
    protected Class<Job> getPojoClass() {
        return Job.class;
    }

    @Override
    protected String getPojoId(Job pojo) {
        return pojo.getId();
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Override the {@link #persist(Job)} method to force persist of child {@code Tasks}.
     */
    @Override
    public Job persist(Job job) {

        // Persist the Job.
        final Job persistedJob = super.persist(job);

        // Persist the Tasks.
        for (Task task : job.getTasks()) {
            this.taskDao.persist(task);
        }

        return persistedJob;

    }

    @Override
    public List<Job> findAllActive() {

        // Declare the return value.
        final List<Job> list = new ArrayList<>();

        // Run a query search for each JobStatus of interest, adding the results to the list.
        for (JobStatus jobStatus : JobStatusHelper.ACTIVE_JOB_STATUSES) {

            final MongoCursor<Document> iterator =
                this.collection.find(Filters.eq("status", jobStatus.toString())).iterator();
            while (iterator.hasNext()) {
                Job job = this.convertToPojo(iterator.next());
                if (job != null) {
                    list.add(job);

                    // Populate the Task list.
                    final List<Task> taskList = this.taskDao.getByJobId(job.getId());
                    if (taskList != null) {
                        job.addTasks(taskList);
                    }
                }
            }

        }

        return list;

    }

}
