package au.gov.aims.ereefs.pojo.task;

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
 * Concrete implementation of the {@link TaskDao} interface for a file-based repository.
 *
 * @author Aaron Smith
 */
public class TaskDaoFileImpl implements TaskDao {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Cached reference to the root directory for retrieving/persisting {@link Task} POJOs.
     */
    private String daoPath;

    /**
     * Utility object for populating a POJO from JSON.
     */
    protected ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * Constructor to capture references and initialise the object.
     */
    public TaskDaoFileImpl(String path) {
        super();

        this.daoPath = (path.endsWith(File.separator) ? path : path + File.separator);

        // Ensure the directory exists.
        final File daoDir = new File(this.daoPath);
        daoDir.mkdirs();
    }

    @Override
    public Task getById(String id) {
        final List<String> filenames = FileUtils.listFiles(this.daoPath, id + ".json");
        if (filenames.size() > 0) {
            final String filename = filenames.get(0);
            if (filename != null) {
                try {
                    return this.jsonMapper.readValue(
                        JSONFileReader.loadFileAsString(filename).toString(),
                        Task.class);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load the Task (\"" + id + "\").", e);
                }
            }
        }
        return null;
    }

    @Override
    public Task persist(Task task) {

        final String filePath = this.daoPath + task.getJobId() + File.separator + task.getId() + ".json";

        // Ensure the path exists.
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        // Write the file.
        try (final BufferedWriter bufferedWriter = new BufferedWriter(
            new OutputStreamWriter(
                new FileOutputStream(
                    new File(filePath))
            )
        )
        ) {
            bufferedWriter.write(this.jsonMapper.writeValueAsString(task));
        } catch(IOException e) {
            throw new RuntimeException("Failed to write Task object.", e);
        }

        return task;

    }

    @Override
    public List<Task> getByJobId(String jobId) {
        final List<Task> tasks = new ArrayList<>();
        final String jobPath = this.daoPath + File.separator + jobId;
        try {
            final List<JSONObject> taskJsonList = JSONFileReader.loadList(jobPath);
            for (JSONObject json : taskJsonList) {
                tasks.add(this.jsonMapper.readValue(json.toString(), Task.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load list of Tasks (\"" + jobPath + "\".", e);
        }
        return tasks;
    }
}
