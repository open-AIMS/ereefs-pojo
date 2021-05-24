package au.gov.aims.ereefs.pojo.definition.download;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;

/**
 * Test cases for {@link DownloadDefinitionDaoFileImpl}.
 */
public class DownloadDefinitionDaoFileImplTest {

    /**
     * Constant identifying the path to the test data.
     */
    final public String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator + "src" +
        File.separator + "test" + File.separator + "resources" + File.separator + "definitions" +
        File.separator + "downloads";

    /**
     * Factory method for instantiating a {@link DownloadDefinitionDao} instance.
     */
    protected DownloadDefinitionDao makeDao() {
        return new DownloadDefinitionDaoFileImpl(TEST_DATA_PATH);
    }

    /**
     * Verify the {@link DownloadDefinitionDao#findAllEnabled()} method returns expected download(s).
     */
    @Test
    public void testFindAllEnabled() {
        final DownloadDefinitionDao dao = this.makeDao();
        boolean isFound = false;
        for (DownloadDefinition downloadDefinition : dao.findAllEnabled()) {
            if (downloadDefinition.getId().equalsIgnoreCase("downloads__ereefs__gbr4_v2")) {
                isFound = true;
            }
        }
        Assertions
            .assertThat(isFound)
            .isTrue();
    }

    /**
     * Verify the {@link DownloadDefinitionDao#getById(String)} method returns expected download(s).
     */
    @Test
    public void testGetById() {
        final DownloadDefinitionDao dao = this.makeDao();

        final DownloadDefinition downloadDefinition = dao.getById("downloads__ereefs__gbr4_v2");
        Assertions
            .assertThat(downloadDefinition.getId())
            .isEqualTo("downloads__ereefs__gbr4_v2");
        Assertions
            .assertThat(downloadDefinition.isEnabled())
            .isTrue();
        Assertions
            .assertThat(downloadDefinition.getFilenameTemplate())
            .isEqualToIgnoringCase("gbr4_simple_{year}-{month}.nc");
    }

    /**
     * Verify the {@link DownloadDefinitionDao#persist(Object)} method throws a
     * {@code RuntimeException}.
     */
    @Test
    public void getPersist() {
        final DownloadDefinitionDao dao = this.makeDao();
        Assertions
            .assertThatThrownBy(() -> dao.persist(null))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Not implemented.");
    }

}
