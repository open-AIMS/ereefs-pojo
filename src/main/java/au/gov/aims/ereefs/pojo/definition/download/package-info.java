/**
 * Provides interfaces and concrete implementations for defining {@code Download sources} (see
 * {@link au.gov.aims.ereefs.pojo.definition.download.DownloadDefinition}) used within the system.
 * A specialised {@code Dao} interface (see
 * {@link au.gov.aims.ereefs.pojo.definition.download.DownloadDefinitionDao}) provides abstract
 * access for interacting with the records in the repository, along with both a file-based
 * implementation
 * ({@link au.gov.aims.ereefs.pojo.definition.download.DownloadDefinitionDaoFileImpl}) and a
 * {@code MongoDB}-based implementation
 * ({@link au.gov.aims.ereefs.pojo.definition.download.DownloadDefinitionDaoMongoDbImpl}).
 */
package au.gov.aims.ereefs.pojo.definition.download;