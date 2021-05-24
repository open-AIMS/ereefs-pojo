package au.gov.aims.ereefs.pojo.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * {@code Deserializer} implementation to deserialise a {@code String} (eg: {@code "true"} or
 * {@code "false"}) as a {@code Boolean}.
 *
 * @author Aaron Smith
 */
public class BooleanStringDeserializer extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return "true".equalsIgnoreCase(jsonParser.getText());
    }
}
