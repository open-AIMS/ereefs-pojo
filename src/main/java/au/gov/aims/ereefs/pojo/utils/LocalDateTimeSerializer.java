package au.gov.aims.ereefs.pojo.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * {@code Serializer} implementation to serialise a {@code LocalDateTime} from an ISO8601-based
 * string representation.
 *
 * @author Aaron Smith
 */
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime dateTime,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider)
        throws IOException {
        jsonGenerator.writeObject(
            dateTime
                .atZone(DateTimeConstants.DEFAULT_TIME_ZONE_ID)
                .format(DateTimeFormatter.ISO_INSTANT)
        );
    }

}
