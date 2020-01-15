package com.redhat.cpaas.pipeline.model.component.build

class PNCBuild extends Build {
    public static final String TYPE = "pnc"
    public  static final String JENKINSFILE = "product/JenkinsfilePnc"

    String environment

    @Override
    void applyBuildResult(String metadata) {
    }
}