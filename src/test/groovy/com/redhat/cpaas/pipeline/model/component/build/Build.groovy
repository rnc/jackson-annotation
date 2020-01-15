package com.redhat.cpaas.pipeline.model.component.build


import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy

//@Grapes([
//    @Grab(group='com.fasterxml.jackson.core', module='jackson-core', version='2.10.1'),
//    @Grab(group='com.fasterxml.jackson.core', module='jackson-annotations', version='2.10.1'),
//    @Grab(group='com.fasterxml.jackson.core', module='jackson-databind', version='2.10.1'),
//])

import com.redhat.cpaas.pipeline.model.component.Component
import com.redhat.cpaas.pipeline.model.component.build.artifact.BuildArtifact

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes([
    @JsonSubTypes.Type(value = BrewBuild.class, name = BrewBuild.TYPE),
    @JsonSubTypes.Type(value = PNCBuild.class, name = PNCBuild.TYPE),
    @JsonSubTypes.Type(value = CEKitBuild.class, name = CEKitBuild.TYPE),
    @JsonSubTypes.Type(value = PiGBuild.class, name = PiGBuild.TYPE),
    @JsonSubTypes.Type(value = ETTBuild.class, name = ETTBuild.TYPE),
    @JsonSubTypes.Type(value = DoozerBuild.class, name = DoozerBuild.TYPE)
])
public abstract class Build implements Serializable {
    public static final String TYPE = "generic"
    public static final String JENKINSFILE = "product/Jenkinsfile"

    /**
     * If the executed build should be a scratch build (or another type that is not meant to be released).
     */
    public boolean scratch = false

    /**
     * If the build should be executed even if a build for selected source was already built.
     */
    boolean force = false

    /**
     * Timeout in minutes.
     */
    int timeout = 120
    
    /**
     * Priority of the component. If `depends_on` will be provided, it will override
     * this field after computing the correct priority.
     */
    int priority = 1

    /**
     * Build name.
     */
    public String name

    /**
     * Build description. Probably a sentence describing the build.
     */
    public String description

    /**
     * Reference to the component this build is part of.
     */
    @JsonBackReference
    public Component component

    /**
     * List of build results produced by this build execution.
     * 
     * A single build can produce multiple artifacts (results).
     * 
     */
    public List<BuildArtifact> artifacts = []

    abstract void applyBuildResult(String metadata)

    /**
     * This method could be used to compute default values
     * for properties if these need to take into account several
     * non-trivial elements.
     */
    Build init() {
        return this
    }

    @Override
    public String toString() {
        return objectToString(this)
    }

    public void postInit() {}

    static String objectToString(Object data) {
        ObjectMapper mapper = new ObjectMapper()
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE)
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data)
    }
}