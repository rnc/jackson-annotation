package com.redhat.cpaas.pipeline.model.component

import com.redhat.cpaas.pipeline.model.component.ContainerComponent

/**
 * Container image component that uses CEKit image
 * definitions to describe the image.
 * 
 * @see <a href="https://cekit.io/">https://cekit.io/</a> 
 */
class CEKitContainerComponent extends ContainerComponent {
    static final String TYPE = "cekit-container"

}