package au.gov.aims.ereefs.pojo.definition.product;

import au.gov.aims.ereefs.pojo.Stage;
import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Abstract base test class that implements tests that are common to all
 * {@link ProductDefinitionDao} implementations.
 */
abstract public class AbstractProductDefinitionDaoImplTest {

    /**
     * The {@code ProductId} of the ncAggregate product.
     */
    protected String NCAGGREGATE_PRODUCT_ID = "products/ncaggregate";

    /**
     * The {@code ProductId} of the ncAnimate product.
     */
    protected String NCANIMATE_PRODUCT_ID = "products/ncanimate";

    /**
     * A list of the test product Ids.
     */
    protected String[] productIds = new String[]{
        NCAGGREGATE_PRODUCT_ID, NCANIMATE_PRODUCT_ID
    };

    /**
     * Factory method for instantiating a {@link ProductDefinitionDao} instance.
     */
    abstract protected ProductDefinitionDao makeDao();

    /**
     * Verify the {@link ProductDefinitionDao#findAllEnabled()} returns the expected product
     * definitions, based on Id.
     */
    protected void doTestFindAllEnabled() {
        final ProductDefinitionDao dao = this.makeDao();
        final List<ProductDefinition> allEnabled = dao.findAllEnabled();

        boolean isNcAggregateProductFound = false;
        boolean isNcAnimateProductFound = false;

        Assertions
            .assertThat(allEnabled)
            .hasSize(2);
        for (final ProductDefinition productDefinition : allEnabled) {
            if (productDefinition.getId().equals(NCAGGREGATE_PRODUCT_ID)) {
                this.validateNcAggregateProduct(productDefinition);
                isNcAggregateProductFound = true;
            }
            if (productDefinition.getId().equals(NCANIMATE_PRODUCT_ID)) {
                this.validateNcAnimateProduct(productDefinition);
                isNcAnimateProductFound = true;
            }
        }

        Assertions
            .assertThat(isNcAggregateProductFound)
            .isTrue();
        Assertions
            .assertThat(isNcAnimateProductFound)
            .isTrue();
    }

    /**
     * Verify that {@link ProductDefinitionDao#getById(String)} method returns the ncAggregate
     * product as expected.
     */
    protected void doTestProductNcAggregate() {
        final ProductDefinitionDao dao = this.makeDao();
        this.validateNcAggregateProduct(dao.getById("products/ncaggregate"));

    }

    /**
     * Verify that {@link ProductDefinitionDao#getById(String)} method returns the ncAnimate
     * product as expected.
     */
    protected void doTestProductNcAnimate() {
        final ProductDefinitionDao dao = this.makeDao();
        this.validateNcAnimateProduct(dao.getById("products/ncanimate"));

    }

    /**
     * Helper method that accepts a reference to the test {@code NcAggregate} product and validates
     * the properties of the object.
     */
    protected void validateNcAggregateProduct(ProductDefinition productDefinition) {

        // Validate the instance type.
        Assertions
            .assertThat(productDefinition)
            .isInstanceOf(NcAggregateProductDefinition.class);

        // Typecast for easier processing.
        final NcAggregateProductDefinition ncAggregateProduct =
            (NcAggregateProductDefinition) productDefinition;

        Assertions
            .assertThat(ncAggregateProduct)
            .hasFieldOrPropertyWithValue("id", "products/ncaggregate")
            .hasFieldOrPropertyWithValue("isEnabled", true)
            .hasFieldOrPropertyWithValue("stage", Stage.PROTOTYPE)
            .hasFieldOrPropertyWithValue("targetTimeZone", "Australia/Brisbane")
            .hasFieldOrPropertyWithValue("lastModified", DateTime.parse("2019-08-08T16:40:00.000+10:00").toDate());

        // Filters.
        final ProductDefinition.Filters filters = ncAggregateProduct.getFilters();
        Assertions
            .assertThat(filters)
            .isNotNull();
        final ProductDefinition.DateRange[] dateRanges = filters.getDateRanges();
        Assertions
            .assertThat(dateRanges)
            .isNotEmpty();
        ProductDefinition.DateRange dateRange = dateRanges[0];
        Assertions
            .assertThat(dateRange)
            .hasFieldOrPropertyWithValue("from", "2016-01-01")
            .hasFieldOrPropertyWithValue("to", "2016-01-03");
        Assertions
            .assertThat(dateRange.getToSinceEpoch() - dateRange.getFromSinceEpoch())
            .isEqualTo(3);
        dateRange = dateRanges[1];
        Assertions
            .assertThat(dateRange)
            .hasFieldOrPropertyWithValue("from", "2017-01-01")
            .hasFieldOrPropertyWithValue("to", "2017-01-10");
        Assertions
            .assertThat(dateRange.getToSinceEpoch() - dateRange.getFromSinceEpoch())
            .isEqualTo(10);

        // Inputs
        boolean isNetCdfInputFound = false;
        for (final NcAggregateProductDefinition.NetCDFInput input : ncAggregateProduct.getInputs()) {
            if (input.getType().equalsIgnoreCase("NETCDF")) {
                isNetCdfInputFound = true;
                Assertions
                    .assertThat(input)
                    .isInstanceOf(NcAggregateProductDefinition.NetCDFInput.class)
                    .hasFieldOrPropertyWithValue("id", "products/ncaggregate/input1")
                    .hasFieldOrPropertyWithValue("type", "netcdf")
                    .hasFieldOrPropertyWithValue("timeIncrement", "hourly")
                    .hasFieldOrPropertyWithValue("fileDuration", "daily")
                    .hasFieldOrPropertyWithValue("completeFilesOnly", true);
                Assertions
                    .assertThat(input.getVariables())
                    .containsOnly("variable1", "variable2");
            }
        }
        Assertions
            .assertThat(isNetCdfInputFound)
            .isTrue();

        // PreProcessingTasks.
        boolean isFileBasedPreprocessingTaskFound = false;
        boolean isSiteListBasedPreprocessingTaskFound = false;
        for (final NcAggregateProductDefinition.PreProcessingTaskDefn task : ncAggregateProduct.getPreProcessingTasks()) {
            if ("FileBased".equals(task.getType())) {
                isFileBasedPreprocessingTaskFound = true;
                final JsonNode rootJson = (JsonNode) task.getJson();
                final JsonNode propertiesJson = rootJson.get("properties");
                Assertions
                    .assertThat(propertiesJson.get("stringProperty").asText())
                    .isEqualTo("FileBasedStringProperty");
                Assertions
                    .assertThat(propertiesJson.get("doubleProperty").asDouble())
                    .isEqualTo(0.01);
                Assertions
                    .assertThat(propertiesJson.get("booleanPropertyTrue").asBoolean())
                    .isEqualTo(true);
                Assertions
                    .assertThat(propertiesJson.get("booleanPropertyFalse").asBoolean())
                    .isEqualTo(false);
                Assertions
                    .assertThat(propertiesJson.get("intProperty").asInt())
                    .isEqualTo(1);

                final JsonNode filesJson = rootJson.get("files");
                Assertions
                    .assertThat(filesJson)
                    .hasSize(3);

                JsonNode fileJson = filesJson.get(0);
                Assertions
                    .assertThat(fileJson.get("url").asText())
                    .isEqualTo("file:file1.csv");
                Assertions
                    .assertThat(fileJson.get("bindName").asText())
                    .isEqualTo("file1BindName");
                JsonNode filePropertiesJson = fileJson.get("properties");
                Assertions
                    .assertThat(filePropertiesJson.get("stringProperty").asText())
                    .isEqualTo("file1StringValue");
                Assertions
                    .assertThat(filePropertiesJson.get("doubleProperty").asDouble())
                    .isEqualTo(0.01);
                Assertions
                    .assertThat(filePropertiesJson.get("booleanPropertyTrue").asBoolean())
                    .isEqualTo(true);
                Assertions
                    .assertThat(filePropertiesJson.get("booleanPropertyFalse").asBoolean())
                    .isEqualTo(false);
                Assertions
                    .assertThat(filePropertiesJson.get("intProperty").asInt())
                    .isEqualTo(1);

                fileJson = filesJson.get(1);
                Assertions
                    .assertThat(fileJson.get("url").asText())
                    .isEqualTo("file:file2.csv");
                Assertions
                    .assertThat(fileJson.get("bindName").asText())
                    .isEqualTo("file2BindName");
                filePropertiesJson = fileJson.get("properties");
                Assertions
                    .assertThat(filePropertiesJson.get("stringProperty").asText())
                    .isEqualTo("file2StringValue");
                Assertions
                    .assertThat(filePropertiesJson.get("doubleProperty").asDouble())
                    .isEqualTo(0.02);
                Assertions
                    .assertThat(filePropertiesJson.get("booleanPropertyTrue").asBoolean())
                    .isEqualTo(true);
                Assertions
                    .assertThat(filePropertiesJson.get("booleanPropertyFalse").asBoolean())
                    .isEqualTo(false);
                Assertions
                    .assertThat(filePropertiesJson.get("intProperty").asInt())
                    .isEqualTo(2);

            }

            if ("SiteListBased".equals(task.getType())) {
                isSiteListBasedPreprocessingTaskFound = true;
                final JsonNode rootJson = (JsonNode) task.getJson();
                Assertions
                    .assertThat(rootJson.get("bindName").asText())
                    .isEqualTo("siteListBindName");

                final JsonNode sitesJson = rootJson.get("sites");
                Assertions
                    .assertThat(sitesJson)
                    .hasSize(2);

                JsonNode siteJson = sitesJson.get(0);
                Assertions
                    .assertThat(siteJson.get("name").asText())
                    .isEqualTo("Site 1");
                Assertions
                    .assertThat(siteJson.get("lat").asDouble())
                    .isEqualTo(1.1);
                Assertions
                    .assertThat(siteJson.get("lon").asDouble())
                    .isEqualTo(1.2);

                siteJson = sitesJson.get(1);
                Assertions
                    .assertThat(siteJson.get("name").asText())
                    .isEqualTo("Site 2");
                Assertions
                    .assertThat(siteJson.get("lat").asDouble())
                    .isEqualTo(2.1);
                Assertions
                    .assertThat(siteJson.get("lon").asDouble())
                    .isEqualTo(2.2);

            }
        }
        Assertions
            .assertThat(isFileBasedPreprocessingTaskFound)
            .isTrue();
        Assertions
            .assertThat(isSiteListBasedPreprocessingTaskFound)
            .isTrue();

        // Output.
        Assertions
            .assertThat(ncAggregateProduct.getOutputs())
            .hasFieldOrPropertyWithValue("strategy", NcAggregateProductDefinition.OutputsStrategy.DAILY)
            .hasFieldOrPropertyWithValue("baseUrl", "file://baseUrl")
            .hasFieldOrPropertyWithValue("completeFilesOnly", false);
        Assertions
            .assertThat(ncAggregateProduct.getOutputs().getOutputFiles())
            .hasSize(2);
        Assertions
            .assertThat(ncAggregateProduct.getOutputs().getNetcdfOutputFile())
            .hasFieldOrPropertyWithValue("type", NcAggregateProductDefinition.OutputFileType.NETCDF)
            .hasFieldOrPropertyWithValue("regularGridMapperCacheBindName", "regularGridMapper:0.03");
        Assertions
            .assertThat(ncAggregateProduct.getOutputs().getNetcdfOutputFile().getGlobalAttributes())
            .containsOnly(Assertions.entry("firstAttribute", "firstValue"));
        Assertions
            .assertThat(ncAggregateProduct.getOutputs().getZoneBasedSummaryOutputFile())
            .hasFieldOrPropertyWithValue("type", NcAggregateProductDefinition.OutputFileType.ZONE_SUMMARY)
            .hasFieldOrPropertyWithValue("zoneNamesBindName", "csv:gbr_zone_names")
            .hasFieldOrPropertyWithValue("indexToZoneIdMapBindName", "geojson:indexToZoneIdMap");

        // Action.
        final NcAggregateProductDefinition.Action action = ncAggregateProduct.getAction();
        Assertions
            .assertThat(action)
            .hasFieldOrPropertyWithValue("period", "none");
        Assertions
            .assertThat(action.getDepths())
            .containsOnly(1.5, 0, -1.5);
        Assertions
            .assertThat(action.getVariables())
            .containsOnly("variable1", "variable2");

        // Operators
        boolean isMeanTempOperatorFound = false;
        boolean isSpeedOperatorFound = false;
        boolean isThresholdValueExceedanceCountOperatorFound = false;
        boolean isThresholdZonalExceedanceCountOperatorFound = false;
        for (final NcAggregateProductDefinition.SummaryOperator summaryOperator : action.getSummaryOperators()) {
            if (summaryOperator.getName().equalsIgnoreCase("MEAN_TEMP")) {
                isMeanTempOperatorFound = true;
                Assertions
                    .assertThat(summaryOperator)
                    .hasFieldOrPropertyWithValue("name", "MEAN_TEMP")
                    .hasFieldOrPropertyWithValue("operatorType", "MEAN")
                    .hasFieldOrPropertyWithValue("isEnabled", true);
                Assertions
                    .assertThat(summaryOperator.getInputVariables())
                    .containsOnly("temp");
                Assertions
                    .assertThat(summaryOperator.getOutputVariables())
                    .hasSize(1);
                Assertions
                    .assertThat(summaryOperator.getOutputVariables().get(0).getAttributes())
                    .containsEntry("short_name", "mean_temp")
                    .containsEntry("long_name", "mean_temp")
                    .containsEntry("standard_name", "mean_temp");
            }
            if (summaryOperator.getName().equalsIgnoreCase("WIND_SPEED")) {
                isSpeedOperatorFound = true;
                Assertions
                    .assertThat(summaryOperator)
                    .hasFieldOrPropertyWithValue("name", "WIND_SPEED")
                    .hasFieldOrPropertyWithValue("operatorType", "SPEED")
                    .hasFieldOrPropertyWithValue("isEnabled", true);
                Assertions
                    .assertThat(summaryOperator.getInputVariables())
                    .containsOnly("wspeed_u", "wspeed_v");
                Assertions
                    .assertThat(summaryOperator.getOutputVariables())
                    .hasSize(3);
                Assertions
                    .assertThat(summaryOperator.getOutputVariables().get(0).getAttributes())
                    .containsEntry("short_name", "min_wspeed")
                    .containsEntry("long_name", "minimum_wind_speed")
                    .containsEntry("standard_name", "minimum_wind_speed");
                Assertions
                    .assertThat(summaryOperator.getOutputVariables().get(1).getAttributes())
                    .containsEntry("short_name", "mean_wspeed")
                    .containsEntry("long_name", "mean_wind_speed")
                    .containsEntry("standard_name", "mean_wind_speed");
                Assertions
                    .assertThat(summaryOperator.getOutputVariables().get(2).getAttributes())
                    .containsEntry("short_name", "max_wspeed")
                    .containsEntry("long_name", "maximum_wind_speed")
                    .containsEntry("standard_name", "maximum_wind_speed");
            }
            if (summaryOperator.getName().equalsIgnoreCase("Threshold Value Exceedance Count")) {
                isThresholdValueExceedanceCountOperatorFound = true;
                Assertions
                    .assertThat(summaryOperator)
                    .isInstanceOf(NcAggregateProductDefinition.ThresholdValueSummaryOperator.class)
                    .hasFieldOrPropertyWithValue("name", "Threshold Value Exceedance Count")
                    .hasFieldOrPropertyWithValue("operatorType", "THRESHOLD_VALUE_EXCEEDANCE_COUNT")
                    .hasFieldOrPropertyWithValue("isEnabled", true)
                    .hasFieldOrPropertyWithValue("accumulationTimeSlices", 1)
                    .hasFieldOrPropertyWithValue("threshold", 12.3);
                Assertions
                    .assertThat(summaryOperator.getInputVariables())
                    .containsOnly("temp");
                Assertions
                    .assertThat(summaryOperator.getOutputVariables())
                    .hasSize(1);
                Assertions
                    .assertThat(summaryOperator.getOutputVariables().get(0).getAttributes())
                    .containsEntry("short_name", "temp_above_threshold");
            }
            if (summaryOperator.getName().equalsIgnoreCase("Threshold Zonal Exceedance Count")) {
                isThresholdZonalExceedanceCountOperatorFound = true;
                Assertions
                    .assertThat(summaryOperator)
                    .isInstanceOf(NcAggregateProductDefinition.ThresholdZonalSummaryOperator.class)
                    .hasFieldOrPropertyWithValue("name", "Threshold Zonal Exceedance Count")
                    .hasFieldOrPropertyWithValue("operatorType", "THRESHOLD_ZONAL_EXCEEDANCE_COUNT")
                    .hasFieldOrPropertyWithValue("isEnabled", true)
                    .hasFieldOrPropertyWithValue("accumulationTimeSlices", 1)
                    .hasFieldOrPropertyWithValue("zonesInputId", "zone_id")
                    .hasFieldOrPropertyWithValue("thresholdsInputId", "threshold_id");
                Assertions
                    .assertThat(summaryOperator.getInputVariables())
                    .containsOnly("temp");
                Assertions
                    .assertThat(summaryOperator.getOutputVariables())
                    .hasSize(1);
                Assertions
                    .assertThat(summaryOperator.getOutputVariables().get(0).getAttributes())
                    .containsEntry("short_name", "temp_above_threshold");
            }
        }
        Assertions
            .assertThat(isMeanTempOperatorFound)
            .isTrue();
        Assertions
            .assertThat(isSpeedOperatorFound)
            .isTrue();
        Assertions
            .assertThat(isThresholdValueExceedanceCountOperatorFound)
            .isTrue();
        Assertions
            .assertThat(isThresholdZonalExceedanceCountOperatorFound)
            .isTrue();
    }

    /**
     * Helper method that accepts a reference to the test {@code NcAnimate} product and validates
     * the properties of the object.
     */
    protected void validateNcAnimateProduct(ProductDefinition productDefinition) {

        // Validate the instance type.
        Assertions
            .assertThat(productDefinition)
            .isInstanceOf(NcAnimateProductDefinition.class);

        // Typecast for easier processing.
        final NcAnimateProductDefinition ncAnimateProduct =
            (NcAnimateProductDefinition) productDefinition;

        Assertions
            .assertThat(ncAnimateProduct)
            .hasFieldOrPropertyWithValue("id", "products/ncanimate")
            .hasFieldOrPropertyWithValue("isEnabled", true)
            .hasFieldOrPropertyWithValue("stage", Stage.OPERATIONAL)
            .hasFieldOrPropertyWithValue("lastModified", DateTime.parse("2019-08-08T16:40:00.000+10:00").toDate());
        Assertions
            .assertThat(ncAnimateProduct.getRegions())
            .containsExactly("region1", "region2");

    }

}
