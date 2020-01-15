package com.redhat.cpaas.pipeline.model.component.build.artifact

class MavenBuildArtifact extends BuildArtifact {
    /**
     * The artifactId of the result.
     */
    String artifact
    
    /**
     * The groupId of the result.
     */
    String group
    
    /**
     * Type of the artifact: zip, jar, etc.
     */
    String type

    /**
     * URL from where the artifact could be downloaded.
     */
    String url
}