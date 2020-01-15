package com.redhat.cpaas.pipeline.model.component.build

import com.redhat.cpaas.pipeline.model.Repository

class PiGBuild extends Build {
    public  static final String TYPE = "pig"
    public static final String JENKINSFILE = "product/JenkinsfilePig"

    String pigFlags
    String stagingDir

    boolean brewPush
    boolean skipStaging

    Repository pigSource

    @Override
    void applyBuildResult(String metadata) {
    }
}