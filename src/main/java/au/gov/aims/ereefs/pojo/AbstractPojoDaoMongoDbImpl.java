package au.gov.aims.ereefs.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.io.IOException;

/**
 * Extends {@link AbstractPojoDaoImpl} to support a MongoDB-based repository.
 *
 * @author Aaron Smith
 */
abstract public class AbstractPojoDaoMongoDbImpl<T> extends AbstractPojoDaoImpl<T> {

    /**
     * Cached reference to the connection to {@code MongoDB}.
     */
    protected MongoDatabase mongoDatabase;

    /**
     * Cached reference to the underlying {@code Collection} in {@code MongoDB}.
     */
    protected MongoCollection<Document> collection;

    /**
     * Settings to use for MongoDB API when converting a Document to {@code String}.
     */
    protected JsonWriterSettings writerSettings = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();

    /**
     * Constructor to capture references relevant to this implementation.
     */
    public AbstractPojoDaoMongoDbImpl(MongoDatabase mongoDatabase,
                                      String collectionName) {
        this.mongoDatabase = mongoDatabase;
        this.collection = this.mongoDatabase.getCollection(collectionName, Document.class);
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Helper method to retrieve the {@code Document} from the database.
     *
     * @param id the unique {@code id} of the {@code Document}.
     * @return the {@code Document} matching the {@code id}, or {@code null}.
     */
    protected Document getDocumentById(String id) {
        return this.collection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public T getById(String id) {
        final Document document = this.getDocumentById(id);
        if (document != null) {
            return this.convertToPojo(document);
        }
        return null;
    }

    /**
     * Overload the {@link #convertToPojo(String)} method to standardise converting a Document from
     * {@code MongoDB} to a {@code String} representation of JSON.
     */
    protected T convertToPojo(final Document document) {
        return this.convertToPojo(document.toJson(this.writerSettings));
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Persist the {@code POJO} to the database.
     *
     * @param pojo
     */
    @Override
    public T persist(T pojo) {

        try {
            this.collection.replaceOne(
                Filters.eq("_id", this.getPojoId(pojo)),
                Document.parse(this.jsonMapper.writeValueAsString(pojo)),
                new ReplaceOptions().upsert(true)
            );
        } catch (IOException e) {
            throw new RuntimeException(
                "Failed to persist " + this.getPojoClass().getSimpleName() + " (" +
                    this.getPojoId(pojo) + ")."
            );
        }
        return pojo;

    }

    /**
     * Persist the {@code JsonNode} to the database.
     *
     * @param jsonNode
     */
    protected JsonNode persist(JsonNode jsonNode) {

        final String id = jsonNode.get("_id").asText();
        this.collection.replaceOne(
            Filters.eq("_id", id),
            Document.parse(jsonNode.toString()),
            new ReplaceOptions().upsert(true)
        );
        return jsonNode;

    }

}
