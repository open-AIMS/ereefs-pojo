package au.gov.aims.ereefs.pojo.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * {@code Deserializer} implementation to deserialise a {@code LocalDateTime} to an ISO8601-based
 * string representation.
 *
 * @author Aaron Smith
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    public LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {
        return ZonedDateTime
            .parse(jsonParser.getText())
            .withZoneSameInstant(DateTimeConstants.DEFAULT_TIME_ZONE_ID)
            .toLocalDateTime();
    }

}
