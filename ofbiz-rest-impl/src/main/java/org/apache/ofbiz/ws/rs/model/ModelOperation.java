package org.apache.ofbiz.ws.rs.model;

public class ModelOperation {

    protected String service;
    protected String verb;
    protected String produces;
    protected String consumes;
    protected String path;
    protected String description;

    /**
     * Gets the value of the service property.
     *
     * @return possible object is {@link Service }
     *
     */
    public String getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     *
     * @param value allowed object is {@link Service }
     *
     */
    public void setService(String value) {
        this.service = value;
    }

    /**
     * @param value
     * @return
     */
    public ModelOperation service(String value) {
        this.service = value;
        return this;
    }

    /**
     * Gets the value of the verb property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getVerb() {
        return verb;
    }

    /**
     * Sets the value of the verb property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setVerb(String value) {
        this.verb = value;
    }

    /**
     * @param value
     * @return
     */
    public ModelOperation verb(String value) {
        this.verb = value;
        return this;
    }

    /**
     * Gets the value of the produces property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getProduces() {
        return produces;
    }

    /**
     * Sets the value of the produces property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setProduces(String value) {
        this.produces = value;
    }

    /**
     * @param value
     * @return
     */
    public ModelOperation produces(String value) {
        this.produces = value;
        return this;
    }

    /**
     * Gets the value of the consumes property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getConsumes() {
        return consumes;
    }

    /**
     * Sets the value of the consumes property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setConsumes(String value) {
        this.consumes = value;
    }

    /**
     * @param value
     * @return
     */
    public ModelOperation consumes(String value) {
        this.consumes = value;
        return this;
    }

    /**
     * Gets the value of the path property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * @param value
     * @return
     */
    public ModelOperation path(String value) {
        this.path = value;
        return this;
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

    /**
     * @param value
     * @return
     */
    public ModelOperation description(String value) {
        this.description = value;
        return this;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "service: "+service+", path: "+path+", verb: "+verb+", description: "+description+", produces: "+produces;
    }



}
