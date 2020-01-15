package com.redhat.cpaas.pipeline.model.component.build

class ETTBuild extends BrewBuild {
    public static final String TYPE = "ett"

    String task
    String version
    String scmUrl
    String queue
    String regen

    @Override
    void applyBuildResult(String metadata) {
    }
}