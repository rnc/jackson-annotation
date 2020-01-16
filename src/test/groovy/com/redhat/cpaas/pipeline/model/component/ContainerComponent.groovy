package com.redhat.cpaas.pipeline.model.component

/**
 * Container image component that uses Dockerfile
 * format to build the image.
 */
class ContainerComponent extends Component {
    public static final String TYPE = "container"

    String imageType
    String hostLevelAccess = "unprivileged"
    String summary
    String description
    String displayName
}