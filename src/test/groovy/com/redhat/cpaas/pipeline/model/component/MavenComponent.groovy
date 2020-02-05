package com.redhat.cpaas.pipeline.model.component

import org.goots.jackson.deserializer.JsonPostDeserialize

/**
 * Component for Maven builds.
 */
public class MavenComponent extends Component {
    public static final String TYPE = "maven"

    @Override
    @JsonPostDeserialize
    private void postDeserialize()
    {
        builds.forEach( { b -> b.postInit()})
    }
}
