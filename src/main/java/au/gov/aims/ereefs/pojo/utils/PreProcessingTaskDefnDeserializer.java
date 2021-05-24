package au.gov.aims.ereefs.pojo.utils;

import au.gov.aims.ereefs.pojo.definition.product.NcAggregateProductDefinition;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * {@code Deserializer} implementation to deserialise a
 * {@link NcAggregateProductDefinition.PreProcessingTaskDefn}.
 *
 * @author Aaron Smith
 */
public class PreProcessingTaskDefnDeserializer extends StdDeserializer<NcAggregateProductDefinition.PreProcessingTaskDefn> {

    public PreProcessingTaskDefnDeserializer() {
        super(NcAggregateProductDefinition.PreProcessingTaskDefn.class);
    }

    @Override
    public NcAggregateProductDefinition.PreProcessingTaskDefn deserialize(JsonParser jsonParser,
                                                                          DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {
        return new NcAggregateProductDefinition.PreProcessingTaskDefn(
            jsonParser.getCodec().readTree(jsonParser)
        );
    }

}
