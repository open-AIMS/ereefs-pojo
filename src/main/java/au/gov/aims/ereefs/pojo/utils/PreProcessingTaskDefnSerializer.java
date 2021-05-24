package au.gov.aims.ereefs.pojo.utils;

import au.gov.aims.ereefs.pojo.definition.product.NcAggregateProductDefinition;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * {@code Serializer} implementation to serialise a
 * {@link NcAggregateProductDefinition.PreProcessingTaskDefn}.
 *
 * @author Aaron Smith
 */
public class PreProcessingTaskDefnSerializer extends StdSerializer<NcAggregateProductDefinition.PreProcessingTaskDefn> {

    public PreProcessingTaskDefnSerializer() {
        super(NcAggregateProductDefinition.PreProcessingTaskDefn.class);
    }

    @Override
    public void serialize(NcAggregateProductDefinition.PreProcessingTaskDefn task,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider)
        throws IOException {
        jsonGenerator.writeObject(task.getJson());
    }

}
