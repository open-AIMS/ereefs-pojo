package au.gov.aims.ereefs.pojo.utils;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Utilities supporting testing against MongoDB.
 */
public class MongoDbTestUtils {

    /**
     * Cached reference to the {@code MongoDB} database for data manipulation during testing.
     */
    static protected MongoDatabase mongoDatabase = null;

    static public MongoDatabase getMongoDatabase() {
        if (MongoDbTestUtils.mongoDatabase == null) {

            // Retrieve the MongoDB connection URL from the environment variable.
            String connectionUrl = System.getenv("MONGODB_URL");

            // No connection url? Default to localhost.
            if (connectionUrl == null) {
                connectionUrl = "mongodb://root:pwd@localhost:27017";
            }
            MongoDbTestUtils.mongoDatabase =
                MongoClients.create(connectionUrl).getDatabase("test-db");
        }
        return MongoDbTestUtils.mongoDatabase;
    }

    /**
     * Delete any records in the collection.
     */
    static public void clearCollection(MongoCollection<Document> collection) {
        for (Document document : collection.find()) {
            collection.deleteOne(document);
        }

    }

}
