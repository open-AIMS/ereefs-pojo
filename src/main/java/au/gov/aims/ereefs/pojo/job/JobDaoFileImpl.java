package au.gov.aims.ereefs.pojo.job;

import au.gov.aims.ereefs.pojo.task.Task;
import au.gov.aims.ereefs.pojo.task.TaskDao;
import au.gov.aims.ereefs.pojo.utils.FileUtils;
import au.gov.aims.ereefs.pojo.utils.JSONFileReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the {@link JobDao} interface for a file-based repository.
 *
 * @author Aaron Smith
 */
public class JobDaoFileImpl implements JobDao {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Cached reference to the root directory for retrieving/persisting {@link Job} POJOs.
     */
    private String jobDaoPath;

    /**
     * Cached reference to the root directory for retrieving/persisting {@link Task} POJOs.
     */
    private String taskDaoPath;

    /**
     * Utility object for populating a POJO from JSON.
     */
    protected ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * Cached reference to the {@link TaskDao} implementation for use when reading/writing the
     * {@link Task}s associated with a {@link Job}.
     */
    protected TaskDao taskDao;

    /**
     * Constructor to capture references and initialise the object.
     */
    public JobDaoFileImpl(String jobDaoPath,
                          TaskDao taskDao) {
        super();

        this.taskDao = taskDao;

        this.jobDaoPath = (jobDaoPath.endsWith("/") ? jobDaoPath : jobDaoPath + File.separator);

        // Ensure the directories exist.
        final File jobDir = new File(this.jobDaoPath);
        jobDir.mkdirs();

    }

    @Override
    public Job getById(String id) {
        try {

            // Retrieve the Job.
            final JSONObject jobJson = JSONFileReader.loadFileAsJSONObject(this.jobDaoPath + id + ".json");
            if (jobJson != null) {
                final Job job = this.jsonMapper.readValue(jobJson.toString(), Job.class);

                // Populate the Task list.
                job.addTasks(this.taskDao.getByJobId(id));

                return job;
            }

        } catch(Exception e) {
            throw new RuntimeException("Failed to load the Job (\"" + id + "\").", e);
        }
        return null;
    }

    @Override
    public List<Job> findAllActive() {
        final List<Job> activeJobs = new ArrayList<>();

        // Retrieve a list of all Job files.
        final List<String> filenames = FileUtils.listFiles(this.jobDaoPath, ".json");

        // Load each Job file and determine if it matches the search criteria (ie: is active).
        for (String filename : filenames) {
            try {
                final JSONObject json = JSONFileReader.loadFileAsJSONObject(filename);
                if (json != null) {
                    final Job job = this.jsonMapper.readValue(json.toString(), Job.class);
                    if (JobStatusHelper.ACTIVE_JOB_STATUSES.contains(job.status)) {
                        activeJobs.add(job);

                        // Populate the Task list.
                        job.addTasks(this.taskDao.getByJobId(job.getId()));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to load/parse JSON for \"" + filename + "\"", e);
            }
        }

        return activeJobs;
    }

    @Override
    public Job persist(Job job) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("persist: " + job.getId());
        }

        // Write the file.
        try (final BufferedWriter bufferedWriter = new BufferedWriter(
            new OutputStreamWriter(
                new FileOutputStream(new File(this.jobDaoPath + job.getId() + ".json"))
            )
        )
        ) {
            bufferedWriter.write(this.jsonMapper.writeValueAsString(job));
        } catch(IOException e) {
            throw new RuntimeException("Failed to write Job object.", e);
        }

        // Persist the Tasks.
        for (Task task : job.getTasks()) {
            this.taskDao.persist(task);
        }

        return job;

    }

}
