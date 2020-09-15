package org.apache.ofbiz.ws.rs.model;

import java.util.ArrayList;
import java.util.List;

public class ModelApi {

    protected List<ModelResource> resources;

    protected String name;

    protected String displayName;

    protected String description;

    public List<ModelResource> getResources() {
        if (resources == null) {
            resources = new ArrayList<ModelResource>();
        }
        return this.resources;
    }

    public ModelApi addResource(ModelResource resource) {
        if (this.resources == null) {
            this.resources = new ArrayList<>();
        }
        this.resources.add(resource);
        return this;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the displayName property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
