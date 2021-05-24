package au.gov.aims.ereefs.pojo.definition.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@code NcAnimate}-specific specialisation of the {@link ProductDefinition} class.
 */
public class NcAnimateProductDefinition extends ProductDefinition {

    protected String[] regions;
    public String[] getRegions() {
        return this.regions;
    }

    /**
     * Constructor used by the {@code Jackson} library.
     */
    @JsonCreator
    public NcAnimateProductDefinition(@JsonProperty("_id") String id) {
        super(id);
    }

}