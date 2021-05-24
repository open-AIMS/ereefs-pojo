package au.gov.aims.ereefs.pojo.metadata;

import org.assertj.core.api.Assertions;

import java.util.List;

/**
 * Abstract base test class that implements tests that are common to all
 * {@link MetadataDao} implementations.
 */
abstract public class AbstractMetadataDaoImplTest {

    /**
     * The {@code definitionId} for the {@code Downloads} of interest.
     */
    final static protected String DOWNLOAD_DEFINITIONS_ID = "download/ereefs/gbr4_v2";

    /**
     * A list of {@code Ids} for {@link Metadata} relating to {@link #DOWNLOAD_DEFINITIONS_ID}.
     */
    final static protected String[] DOWNLOAD_METADATA_IDS = new String[] {
        "download/ereefs/gbr4_v2/gbr4_simple_2010-09",
        "download/ereefs/gbr4_v2/gbr4_simple_2010-10",
        "download/ereefs/gbr4_v2/gbr4_simple_2010-11",
        "download/ereefs/gbr4_v2/gbr4_simple_2010-12",
        "download/ereefs/gbr4_v2/gbr4_simple_2011-01"
    };

    /**
     * The {@code definitionId} for the {@code Products} of interest.
     */
    final static protected String PRODUCT_DEFINITIONS_ID = "products/netcdf/gbr4_v2/daily-monthly";

    /**
     * A list of {@code Ids} for {@link Metadata} relating to {@link #PRODUCT_DEFINITIONS_ID}.
     */
    final static protected String[] PRODUCT_METADATA_IDS = new String[] {
        "products/netcdf/gbr4_v2/daily-monthly/daily-monthly-2010-09",
        "products/netcdf/gbr4_v2/daily-monthly/daily-monthly-2010-10",
        "products/netcdf/gbr4_v2/daily-monthly/daily-monthly-2010-11",
        "products/netcdf/gbr4_v2/daily-monthly/daily-monthly-2010-12",
        "products/netcdf/gbr4_v2/daily-monthly/daily-monthly-2011-01"
    };

    /**
     * Factory method for instantiating a {@link MetadataDao} instance.
     */
    abstract protected MetadataDao makeDao();

    /**
     * Verify the {@link MetadataDao#getByDefinitionId(String, String)} returns the expected data
     * for a valid {@code definitionId}.
     */
    protected void doTestGetByDefinitionIdValid() {
        final MetadataDao dao = this.makeDao();

        // Verify all Download metadata is retrieved.
        final List<Metadata> downloadMetadataList = dao.getByDefinitionId(
            DOWNLOAD_DEFINITIONS_ID,
            NetCDFMetadata.TYPE
        );

        for (final Metadata metadata : downloadMetadataList) {
            Assertions
                .assertThat(DOWNLOAD_METADATA_IDS)
                .contains(metadata.getId());
            Assertions
                .assertThat(metadata)
                .isInstanceOf(NetCDFMetadata.class);
            Assertions
                .assertThat(((NetCDFMetadata)metadata).getVariables())
                .isNotEmpty();
        }

        for (final String id : DOWNLOAD_METADATA_IDS) {
            boolean isFound = false;
            for (final Metadata metadata : downloadMetadataList) {
                if (metadata.getId().equalsIgnoreCase(id)) {
                    isFound = true;
                }
            }
            Assertions
                .assertThat(isFound)
                .isTrue();
        }

        // Verify all Product metadata is retrieved.
        final List<Metadata> productMetadataList = dao.getByDefinitionId(
            PRODUCT_DEFINITIONS_ID,
            NetCDFMetadata.TYPE
        );

        for (final Metadata metadata : productMetadataList) {
            Assertions
                .assertThat(PRODUCT_METADATA_IDS)
                .contains(metadata.getId());
        }

        for (final String id : PRODUCT_METADATA_IDS) {
            boolean isFound = false;
            for (final Metadata metadata : productMetadataList) {
                if (metadata.getId().equalsIgnoreCase(id)) {
                    isFound = true;
                }
            }
            Assertions
                .assertThat(isFound)
                .isTrue();
        }

    }

    /**
     * Verify the {@link MetadataDao#getByDefinitionId(String, String)} returns the expected data
     * for an invalid {@code definitionId}.
     */
    protected void doTestGetByDefinitionIdInvalid() {
        final MetadataDao dao = this.makeDao();

        final List<Metadata> metadataList = dao.getByDefinitionId(
            "invalid_definition_id",
            "invalid_type"
        );
        Assertions
            .assertThat(metadataList)
            .isEmpty();
    }

    /**
     * Verify the {@link MetadataDao#getById(String)} method returns a {@link Metadata} object.
     */
    public void doTestGetByIdValid() {
        final MetadataDao dao = this.makeDao();
        final String METADATA_ID = DOWNLOAD_METADATA_IDS[0];
        final Metadata metadata = dao.getById(METADATA_ID);
        Assertions
            .assertThat(metadata.getId())
            .isEqualTo(METADATA_ID);

    }

    /**
     * Verify the {@link MetadataDao#getById(String)} method returns a null.
     */
    public void doTestGetByIdInvalid() {
        final MetadataDao dao = this.makeDao();
        final Metadata metadata = dao.getById("INVALID_METADATA_ID");
        Assertions
            .assertThat(metadata)
            .isNull();

    }

}
