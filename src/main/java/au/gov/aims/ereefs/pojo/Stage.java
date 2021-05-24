package au.gov.aims.ereefs.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enumerator identifying the lifecycle {@code Stage} of the entity.
 */
public enum Stage {

    /**
     * Lifecycle {@code Stage} meaning that the entity is actively used in an operational
     * environment. For example, a {@code Product} that is {@code operational} is one that is
     * generated regularly.
     */
    @JsonProperty("operational")
    OPERATIONAL,

    /**
     * Lifecycle {@code Stage} meaning that the entity is not actively used. For example, a
     * {@code Product} that is {@code prototype} might still be undergoing refinement, and is
     * therefore generated manually.
     */
    @JsonProperty("prototype")
    PROTOTYPE,

    /**
     * Lifecycle {@code Stage} meaning that the entity is no longer actively used, and not intended
     * to be returned to {@code operational} status. The entity, such as a {@code Product}, should
     * also be disabled or deleted.
     */
    @JsonProperty("discontinued")
    DISCONTINUED;

}
