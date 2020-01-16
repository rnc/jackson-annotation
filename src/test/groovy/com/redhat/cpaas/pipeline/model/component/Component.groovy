package com.redhat.cpaas.pipeline.model.component

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.redhat.cpaas.pipeline.model.Project
import com.redhat.cpaas.pipeline.model.component.build.Build
import org.goots.jackson.deserializer.JsonPostDeserialize

//@Grapes([
//    @Grab(group='com.fasterxml.jackson.core', module='jackson-core', version='2.10.1'),
//    @Grab(group='com.fasterxml.jackson.core', module='jackson-annotations', version='2.10.1'),
//    @Grab(group='com.fasterxml.jackson.core', module='jackson-databind', version='2.10.1'),
//    @GrabConfig(systemClassLoader=true)
//])

/**
 * Abstract definition of the component shared by all
 * component implementations.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes([
    @JsonSubTypes.Type(value = ContainerComponent.class, name = ContainerComponent.TYPE),
    @JsonSubTypes.Type(value = MavenComponent.class, name = MavenComponent.TYPE),
    @JsonSubTypes.Type(value = RPMComponent.class, name = RPMComponent.TYPE)
])
abstract class Component implements Serializable {
    static final String TYPE = "generic"
    static final String JENKINSFILE = "product/Jenkinsfile"

    @JsonBackReference
    Project project

    // /**
    //  * Component build order.
    //  * 
    //  * TODO: This should be probably list of components, TBD
    //  */
    // List<String> depends_on

    // /**
    //  * Priority of the component. If `depends_on` will be provided, it will override
    //  * this field after computing the correct priority.
    //  */
    // int priority = 1

    /**
     * List of email addresses of component owners.
     */
    List<String> owners

    /**
     * Name of the component.
     */
    String name

    /**
     * List of builds for this component.
     */
    @JsonManagedReference
    public List<Build> builds

    @JsonPostDeserialize
    private void postDeserialize()
    {
        builds.forEach( { b -> b.postInit()})
    }

//    @Override
//    public String toString() {
//        return SchemaUtils.objectToString(this)
//    }
}
