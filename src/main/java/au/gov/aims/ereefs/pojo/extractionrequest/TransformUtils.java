package au.gov.aims.ereefs.pojo.extractionrequest;

import au.gov.aims.ereefs.pojo.definition.product.DataExtractionProductDefinition;
import au.gov.aims.ereefs.pojo.definition.product.ProductDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.util.*;

/**
 * Utility class for transforming a {@link ExtractionRequest} to a {@link ProductDefinition}.
 *
 * @author Aaron Smith
 */
public class TransformUtils {

    static public Map<String, DataExtractionProductDefinition.NetCDFInput> DOWNLOAD_ID_TO_INPUT_MAP =
        new HashMap<String, DataExtractionProductDefinition.NetCDFInput>() {{
            String dataCollectionId = "downloads__ereefs__gbr1_2-0";
            String targetProductDefinitionId = "products__ncaggregate__ereefs__gbr1_2-0__raw";
            put(
                dataCollectionId,
                DataExtractionProductDefinition.NetCDFInput.make(
                    targetProductDefinitionId,
                    "netcdf",
                    "hourly",
                    "daily",
                    false,
                    null
                ));

            dataCollectionId = "downloads__ereefs__GBR4_H2p0_B3p1_Cq3P_Dhnd";
            targetProductDefinitionId = "products__ncaggregate__ereefs__GBR4_H2p0_B3p1_Cq3P_Dhnd__raw";
            put(
                dataCollectionId,
                DataExtractionProductDefinition.NetCDFInput.make(
                    targetProductDefinitionId,
                    "netcdf",
                    "daily",
                    "monthly",
                    false,
                    null
                ));

            dataCollectionId = "downloads__ereefs__GBR4_H2p0_B3p1_Cq3R_Dhnd";
            targetProductDefinitionId = "products__ncaggregate__ereefs__GBR4_H2p0_B3p1_Cq3R_Dhnd__raw";
            put(
                dataCollectionId,
                DataExtractionProductDefinition.NetCDFInput.make(
                    targetProductDefinitionId,
                    "netcdf",
                    "daily",
                    "monthly",
                    false,
                    null
                ));

            dataCollectionId = "downloads__ereefs__GBR4_H2p0_B3p1_Cq3b_Dhnd";
            targetProductDefinitionId = "products__ncaggregate__ereefs__GBR4_H2p0_B3p1_Cq3b_Dhnd__raw";
            put(
                dataCollectionId,
                DataExtractionProductDefinition.NetCDFInput.make(
                    targetProductDefinitionId,
                    "netcdf",
                    "daily",
                    "monthly",
                    false,
                    null
                ));

            dataCollectionId = "downloads__ereefs__gbr4_v2";
            targetProductDefinitionId = "products__ncaggregate__ereefs__gbr4_v2__raw";
            put(
                dataCollectionId,
                DataExtractionProductDefinition.NetCDFInput.make(
                    targetProductDefinitionId,
                    "netcdf",
                    "daily",
                    "monthly",
                    false,
                    null
                ));

            dataCollectionId = "downloads__ereefs__gbr4_v4";
            targetProductDefinitionId = "products__ncaggregate__ereefs__gbr4_v4__raw";
            put(
                dataCollectionId,
                DataExtractionProductDefinition.NetCDFInput.make(
                    targetProductDefinitionId,
                    "netcdf",
                    "daily",
                    "monthly",
                    false,
                    null
                ));
            
            dataCollectionId = "downloads__ereefs__gbr1_2-0-river_tracing";
            targetProductDefinitionId = "products__ncaggregate__ereefs__gbr1_2-0__river_tracing__raw";
            put(
                    dataCollectionId,
                    DataExtractionProductDefinition.NetCDFInput.make(
                            targetProductDefinitionId,
                            "netcdf",
                            "daily",
                            "daily",
                            false,
                            null
                    ));

            dataCollectionId = "downloads__ereefs__gbr4_v2-river_tracing";
            targetProductDefinitionId = "products__ncaggregate__ereefs__gbr4_v2__river_tracing__raw";
            put(
                    dataCollectionId,
                    DataExtractionProductDefinition.NetCDFInput.make(
                            targetProductDefinitionId,
                            "netcdf",
                            "daily",
                            "monthly",
                            false,
                            null
                    ));
        }};

    /**
     * Transform the {@link ExtractionRequest} to a {@link DataExtractionProductDefinition}
     * equivalent, ignoring the {@code baseUrl}. This implementation of the overloaded method
     * signature is expected to be used when the client code already has a {@code baseUrl} (for
     * example, retrieved from a {@code Task}.
     * <p>
     * This method invokes {@link #transform(ExtractionRequest, String)} with a {@code null} for
     * the {@code targetUrl}.
     */
    static public ProductDefinition transform(ExtractionRequest extractionRequest) {
        return transform(extractionRequest, null);
    }

    /**
     * Transform the {@link ExtractionRequest} to a {@link DataExtractionProductDefinition}
     * equivalent.
     */
    static public ProductDefinition transform(ExtractionRequest extractionRequest,
                                              String targetUrl) {
        if (extractionRequest == null) {
            throw new NullPointerException("No ExtractionRequest provided.");
        }

        final String id = extractionRequest.getId();

        final ProductDefinition.Filters filters = new ProductDefinition.Filters(
            new ProductDefinition.DateRange[]{
                new ProductDefinition.DateRange(
                    extractionRequest.dateRangeFrom,
                    extractionRequest.dateRangeTo
                )
            }
        );

        final DataExtractionProductDefinition.NetCDFInput[] inputs =
            new DataExtractionProductDefinition.NetCDFInput[]{
                DOWNLOAD_ID_TO_INPUT_MAP.get(extractionRequest.getDataCollection())
            };
        if (inputs[0] == null) {
            throw new NoSuchElementException("Input source \"" +
                extractionRequest.getDataCollection() + "\" not supported.");
        }

        final List<DataExtractionProductDefinition.PreProcessingTaskDefn> preProcessingTaskDefns = new ArrayList<>();
        if (extractionRequest.getSites().length > 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode taskJson = objectMapper.createObjectNode();
            taskJson.put("type", "ExtractionSitesBuilderTask");
            taskJson.put("isRectilinearGrid", false);
            ArrayNode sitesJson = taskJson.putArray("sites");
            for (ExtractionRequest.Site site : extractionRequest.getSites()) {
                ObjectNode siteJson = objectMapper.createObjectNode();
                siteJson.put("name", site.getName());
                siteJson.put("lat", site.getLatitude());
                siteJson.put("lon", site.getLongitude());
                sitesJson.add(siteJson);
            }
            preProcessingTaskDefns.add(
                new DataExtractionProductDefinition.PreProcessingTaskDefn(taskJson)
            );
        }

        final DataExtractionProductDefinition.Action action = new DataExtractionProductDefinition.Action(
            extractionRequest.getOutputTimeStep(),
            extractionRequest.getDepths(),
            extractionRequest.getVariables(),
            new DataExtractionProductDefinition.SummaryOperator[0]
        );

        final String baseUrl = (targetUrl != null ? targetUrl + File.separator + id + File.separator: "ignored");
        final DataExtractionProductDefinition.Outputs outputs = new DataExtractionProductDefinition.Outputs(
            DataExtractionProductDefinition.OutputsStrategy.MONTHLY,
            false,
            baseUrl,
            new ArrayList<DataExtractionProductDefinition.OutputFile>() {{
                add(new DataExtractionProductDefinition.SiteBasedSummaryOutputFile());
            }}
        );

        return DataExtractionProductDefinition.make(
            id,
            "Australia/Brisbane",
            filters,
            inputs,
            preProcessingTaskDefns,
            action,
            outputs
        );

    }
}
