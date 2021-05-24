package au.gov.aims.ereefs.pojo.task;

import au.gov.aims.ereefs.pojo.job.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Utility class for building a {@link Task} for testing.
 */
public class TaskBuilder {

    static protected Random random = new Random();

    /**
     * Helper method to instantiate a random new {@link NcAggregateTask} for testing.
     */
    static public NcAggregateTask buildAggregationTask(Job job, String productDefinitionId) {
        final String outputFilename = UUID.randomUUID().toString();
        final String productMetadataId = productDefinitionId + "/" + outputFilename;

        // Add a random number of time instants.
        final List<NcAggregateTask.TimeInstant> timeInstants = new ArrayList<>();
        final int numOfTimeInstants = random.nextInt(5) + 1;
        for (int timeInstantIndex = 0; timeInstantIndex < numOfTimeInstants; timeInstantIndex++) {
            double aggregatedTime = random.nextDouble();

            // For each time instant, add a random number of input sources.
            final List<NcAggregateTask.Input> inputs = new ArrayList<>();
            final int numOfInputs = random.nextInt(5) + 1;
            for (int inputIndex = 0; inputIndex < numOfInputs; inputIndex++) {

                // For each input, add a random number of input file bounds.
                final List<NcAggregateTask.FileIndexBounds> fileIndexBounds = new ArrayList<>();
                final int numOfFileIndexBounds = random.nextInt(5) + 1;
                for (int inputFileBoundsIndex = 0; inputFileBoundsIndex < numOfFileIndexBounds; inputFileBoundsIndex++) {

                    final String metadataId = UUID.randomUUID().toString();
                    fileIndexBounds.add(
                        new NcAggregateTask.FileIndexBounds(
                            metadataId,
                            inputFileBoundsIndex,
                            inputFileBoundsIndex
                        )
                    );
                }

                // Create the Input.
                final String definitionId = UUID.randomUUID().toString();
                inputs.add(
                    new NcAggregateTask.Input(
                        definitionId,
                        fileIndexBounds
                    )
                );

            }

            timeInstants.add(
                new NcAggregateTask.TimeInstant(
                    aggregatedTime,
                    inputs
                )
            );

        }

        NcAggregateTask task = new NcAggregateTask(job.getId(), productDefinitionId,
            productMetadataId, outputFilename, timeInstants);

        return task;
    }

}
