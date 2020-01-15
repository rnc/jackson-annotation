package com.redhat.cpaas.pipeline.model.component

import com.redhat.cpaas.pipeline.model.ShipElement

/**
 * Component for Maven builds from the PNC builder.
 */
class PNCMavenComponent extends MavenComponent {
    List<ShipElement> ship
    
}
