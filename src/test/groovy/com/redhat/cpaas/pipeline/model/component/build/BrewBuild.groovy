package com.redhat.cpaas.pipeline.model.component.build

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSetter
import com.redhat.cpaas.pipeline.model.Repository
import com.redhat.cpaas.pipeline.model.component.ContainerComponent
import com.redhat.cpaas.pipeline.model.component.build.artifact.ContainerBuildArtifact

class BrewBuild extends Build {
    public static final String TYPE = "brew"

    /**
     * After the build is finished, this field is populated with the correct value
     * which is the Brew build ID.
     * 
     * TODO: Not used
     */
    @JsonIgnore
    public Integer buildId

    public String brewPackage

    public String brewTarget

    public String brewFlags

    /**
     * Dist-git repository information.
     * 
     * TODO: Is Gerrit supported?
     */
    public Repository brewSource


    @Override
    void postInit()
    {
//        System.out.println ("### DEBUG: postInit package is " + brewPackage + " and target " + brewTarget +
//                " and component " + component + " and brewSource " + brewSource)

        // Compute default Brew target name if not provided
        if (!this.brewTarget) {
            this.brewTarget = this.defaultBrewTarget()
        }
        // Compute Brew pacakge name, if not provided
        if (!this.brewPackage) {
            def (String repoType, String repoName) = this.parseDistGitRepo(this.brewSource.repo)
            this.brewPackage = "${repoName}${this.defaultPackageSuffix()}"
        }
    }

    /**
     * We assume here that source for execution in Brew is located in dist-git
     * and the dist-git repository is provide in following format:
     * {repo-type}/{repo-name}.
     * 
     * Returns a array with two elements:
     * 
     * [{repo-type}, {repo-name}]
     */
    String[] parseDistGitRepo(String ) {
        return brewSource.repo.split("/")
    }

    /**
     * Returns default package suffix for given component type.
     * 
     * If the component type is a container, then the package
     * suffix is "-container". In other cases suffix is an empty string.
     */
    String defaultPackageSuffix() {
        switch(this.component.TYPE) {
            case ContainerComponent.TYPE:
                return "-container"
            break
        }

        return ""
    }

    /**
     * Returns default Brew target name for the specific
     * component type.
     */
    String defaultBrewTarget() {
        switch(this.component.TYPE) {
            case ContainerComponent.TYPE:
                return "${brewSource.ref}-candidate"
            break
            default:
                throw new Exception("Could not generate default Brew target name for a '${this.component.TYPE}' component")
            break
        }
    }

    /**
     * Returns Brew command used to execute a task in Brew.
     */
    String buildCommand() {
        List buildCmd = []
        
        switch(this.component.TYPE) {
            // TODO Add APB
            case ContainerComponent.TYPE:
                buildCmd.addAll(['container-build', "--git-branch=${brewSource.ref}"])
            break
            default:
                buildCmd << 'build'
            break
        }

        if (this.scratch) {
           buildCmd << '--scratch'
        }
    
        buildCmd.addAll([
            this.brewTarget,
            "git://pkgs.devel.redhat.com/${brewSource.repo}#origin/${brewSource.ref}",
            '--wait' // TODO: we should not wait here, instead we should pool the status afterwards
        ])

        return buildCmd.join(' ')
    }

    @Override
    void applyBuildResult(String metadata) {
        switch(this.component.TYPE) {
            case ContainerComponent.TYPE:
                this.artifacts << new ContainerBuildArtifact(metadata)
            break
        }
    }

    @JsonSetter("brew-package")
    void setBrewPackage() {
        // // Compute default Brew target name if not provided
        // if (!this.brewTarget) {
        //     this.brewTarget = defaultBrewTarget()
        // }

        // Compute Brew pacakge name, if not provided
        if (!this.brewPackage) {
            def (String repoType, String repoName) = parseDistGitRepo(this.brewSource.repo)
            this.brewPackage = "${repoName}${defaultPackageSuffix()}"
        }
    }
}
