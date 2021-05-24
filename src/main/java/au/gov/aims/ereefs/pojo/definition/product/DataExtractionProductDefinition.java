package au.gov.aims.ereefs.pojo.definition.product;

import au.gov.aims.ereefs.pojo.Stage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * {@code NcAnimate}-specific specialisation of the {@link ProductDefinition} class.
 */
public class DataExtractionProductDefinition extends NcAggregateProductDefinition {

    /**
     * Constructor used by the {@code Jackson} library.
     */
    @JsonCreator
    public DataExtractionProductDefinition(@JsonProperty("_id") String id) {
        super(id);
    }

    /**
     * Factory method to instantiate a {@link DataExtractionProductDefinition} for convenience.
     */
    static public DataExtractionProductDefinition make(String id,
                                                       String targetTimeZone,
                                                       Filters filters,
                                                       NetCDFInput[] inputs,
                                                       List<PreProcessingTaskDefn> preProcessingTasks,
                                                       Action action,
                                                       Outputs outputs) {
        DataExtractionProductDefinition pojo = new DataExtractionProductDefinition(id);
        pojo.setTargetTimeZone(targetTimeZone);
        pojo.setFilters(filters);
        pojo.setInputs(inputs);
        pojo.setPreProcessingTasks(preProcessingTasks);
        pojo.setAction(action);
        pojo.setOutputs(outputs);
        pojo.setStage(Stage.PROTOTYPE);
        return pojo;
    }

}