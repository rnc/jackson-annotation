package com.redhat.cpaas.pipeline.model

import com.redhat.cpaas.pipeline.model.component.Component

class Project {
    String name
    List<Component> components
    List<String> owners
}