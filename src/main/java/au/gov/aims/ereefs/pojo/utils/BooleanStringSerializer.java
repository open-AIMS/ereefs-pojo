package au.gov.aims.ereefs.pojo.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * {@code Serializer} implementation to serialise a {@code Boolean} as a {@code String} (eg:
 * {@code "true"} or {@code "false"}).
 *
 * @author Aaron Smith
 */
public class BooleanStringSerializer extends JsonSerializer<Boolean> {
    @Override
    public void serialize(Boolean bool, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(bool ? "true" : "false");
    }
}
