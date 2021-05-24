package au.gov.aims.ereefs.pojo.task;

import au.gov.aims.ereefs.pojo.Stage;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Test cases for instantiating {@link Test}s and extending classes.
 */
public class TaskInstantiationTest {

    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "task";

    /**
     * Verify that {@link TaskDao} returns a {@link Task} when loading a {@code basic} JSON object.
     */
    @Test
    public void testBasicTasks() {

        final TaskDao dao = new TaskDaoFileImpl(TEST_DATA_PATH);

        final Task task1 = dao.getById("Task_basic_1");
        Assertions
            .assertThat(task1)
            .isExactlyInstanceOf(Task.class)
            .hasFieldOrPropertyWithValue("id", "Task_basic_1")
            .hasFieldOrPropertyWithValue("jobId", "Job_basic")
            .hasFieldOrPropertyWithValue("productDefinitionId", "product_definition")
            .hasFieldOrPropertyWithValue("stage", Stage.DISCONTINUED);
        Assertions
            .assertThat(task1.getDependsOnIds())
            .isEmpty();

        final Task task2 = dao.getById("Task_basic_2");
        Assertions
            .assertThat(task2)
            .isExactlyInstanceOf(Task.class)
            .hasFieldOrPropertyWithValue("id", "Task_basic_2")
            .hasFieldOrPropertyWithValue("jobId", "Job_basic")
            .hasFieldOrPropertyWithValue("productDefinitionId", "product_definition")
            .hasFieldOrPropertyWithValue("stage", Stage.PROTOTYPE);
        Assertions
            .assertThat(task2.getDependsOnIds())
            .hasSize(1)
            .contains("Task_basic_1");

    }

    /**
     * Verify that {@link TaskDao} returns an {@link NcAggregateTask} when loading an appropriate
     * JSON object.
     */
    @Test
    public void testNcAggregateTasks() {

        final TaskDao dao = new TaskDaoFileImpl(TEST_DATA_PATH);

        final Task task1 = dao.getById("Task_ncaggregate_1");
        Assertions
            .assertThat(task1)
            .isExactlyInstanceOf(NcAggregateTask.class)
            .hasFieldOrPropertyWithValue("id", "Task_ncaggregate_1")
            .hasFieldOrPropertyWithValue("jobId", "Job_ncaggregate")
            .hasFieldOrPropertyWithValue("productDefinitionId", "product/output")
            .hasFieldOrPropertyWithValue("metadataId", "product/output/3")
            .hasFieldOrPropertyWithValue("baseUrl", "baseUrl3")
            .hasFieldOrPropertyWithValue("stage", Stage.OPERATIONAL);
        Assertions
            .assertThat(task1.getDependsOnIds())
            .isEmpty();
        Assertions
            .assertThat(((NcAggregateTask) task1).getTimeInstants())
            .hasSize(2);
        Assertions
            .assertThat(((NcAggregateTask) task1).getTimeInstants().get(0))
            .hasFieldOrPropertyWithValue("value", 0.1);
        Assertions
            .assertThat(((NcAggregateTask) task1).getTimeInstants().get(1))
            .hasFieldOrPropertyWithValue("value", 0.2);

        final Task task2 = dao.getById("Task_ncaggregate_2");
        Assertions
            .assertThat(task2)
            .isExactlyInstanceOf(NcAggregateTask.class)
            .hasFieldOrPropertyWithValue("id", "Task_ncaggregate_2")
            .hasFieldOrPropertyWithValue("jobId", "Job_ncaggregate")
            .hasFieldOrPropertyWithValue("productDefinitionId", "product/output")
            .hasFieldOrPropertyWithValue("metadataId", "product/output/4")
            .hasFieldOrPropertyWithValue("baseUrl", "baseUrl4")
            .hasFieldOrPropertyWithValue("stage", Stage.OPERATIONAL);
        Assertions
            .assertThat(task2.getDependsOnIds())
            .hasSize(1)
            .contains("Task_ncaggregate_1");
        NcAggregateTask ncAggregateTask = (NcAggregateTask) task2;
        Assertions
            .assertThat(ncAggregateTask.getTimeInstants())
            .hasSize(2);
        Assertions
            .assertThat(ncAggregateTask.getTimeInstants().get(0))
            .hasFieldOrPropertyWithValue("value", 0.3);
        Assertions
            .assertThat(ncAggregateTask.getTimeInstants().get(1))
            .hasFieldOrPropertyWithValue("value", 0.4);

        Assertions
            .assertThat(ncAggregateTask.getOutcome())
            .isNotNull();
        List<NcAggregateTask.GeneratedFile> generatedFiles =
            ncAggregateTask.getOutcome().getGeneratedFiles();

        NcAggregateTask.GeneratedFile generatedFile = generatedFiles.get(0);
        Assertions
            .assertThat(generatedFile)
            .hasFieldOrPropertyWithValue("type", NcAggregateTask.GeneratedFileType.NETCDF)
            .hasFieldOrPropertyWithValue("url", "netcdfUrl");

        generatedFile = generatedFiles.get(1);
        Assertions
            .assertThat(generatedFile)
            .hasFieldOrPropertyWithValue("type", NcAggregateTask.GeneratedFileType.SUMMARY)
            .hasFieldOrPropertyWithValue("url", "summaryUrl");

    }

    /**
     * Verify that {@link TaskDao} returns an {@link NcAnimateTask} when loading an appropriate
     * JSON object.
     */
    @Test
    public void testNcAnimateTasks() {

        final TaskDao dao = new TaskDaoFileImpl(TEST_DATA_PATH);

        final Task task1 = dao.getById("Task_ncanimate_1");
        Assertions
            .assertThat(task1)
            .isExactlyInstanceOf(NcAnimateTask.class)
            .hasFieldOrPropertyWithValue("id", "Task_ncanimate_1")
            .hasFieldOrPropertyWithValue("jobId", "Job_ncanimate")
            .hasFieldOrPropertyWithValue("productDefinitionId", "product/output")
            .hasFieldOrPropertyWithValue("region", "region1")
            .hasFieldOrPropertyWithValue("stage", Stage.OPERATIONAL);
        Assertions
            .assertThat(task1.getDependsOnIds())
            .isEmpty();

    }

}
