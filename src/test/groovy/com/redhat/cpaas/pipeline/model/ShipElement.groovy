package com.redhat.cpaas.pipeline.model

class ShipElement {
    Artifact artifact
    String ref

    // Workaround required because the 'as' name is a
    // reserved keyword in Groovy. Internally we use the 'ref' name.
    // Both are possible 'object.ref' and 'object.as' to refer to the
    // value.
    String getAs() { return ref }
    void setAs(String ref) { this.ref = ref }
}